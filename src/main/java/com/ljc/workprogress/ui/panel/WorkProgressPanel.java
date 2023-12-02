package com.ljc.workprogress.ui.panel;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import com.ljc.workprogress.domain.WpsConfig;
import com.ljc.workprogress.domain.dto.ResultDto;
import com.ljc.workprogress.domain.dto.WpsDto;
import com.ljc.workprogress.domain.dto.WpsPageDto;
import com.ljc.workprogress.domain.vo.WorkProgressGridData;
import com.ljc.workprogress.domain.vo.WpsQueryDto;
import com.ljc.workprogress.enums.WorkProgressStatusEnum;
import com.ljc.workprogress.toolwindow.HomeToolWindowPanel;
import com.ljc.workprogress.ui.JDTableModel;
import com.ljc.workprogress.ui.dialog.JDWorkProgressFormDialog;
import com.ljc.workprogress.util.RestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class WorkProgressPanel extends JBPanel<WorkProgressPanel> {
    private final JBTable table;
    private final JDTableModel model;
    private WpsPageDto pageData;
    private final WorkProgressNavPanel navPanel;

    private HomeToolWindowPanel rootPanel;
    private Project project;

    public WorkProgressPanel(HomeToolWindowPanel rootPanel, Project project) {
        super(new BorderLayout(), true);
        this.rootPanel=rootPanel;
        this.project=project;
        // 创建表头和表格数据
        Vector<String> columnNames = new Vector<>();
        columnNames.add("进度");
        columnNames.add("项目名称");
        columnNames.add("计划工时");
        columnNames.add("产品经理");
        columnNames.add("用户");
        // 创建默认的表格模型
        model = new JDTableModel(null, columnNames);
        // 创建JBTable并设置模型
        table = new JBTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setMaxWidth(70);
        table.getColumnModel().getColumn(2).setMinWidth(178);
        table.getColumnModel().getColumn(2).setMaxWidth(178);
        table.getColumnModel().getColumn(3).setMinWidth(80);
        table.getColumnModel().getColumn(3).setMaxWidth(80);
        table.getColumnModel().getColumn(4).setMinWidth(80);
        table.getColumnModel().getColumn(4).setMaxWidth(80);

        JBScrollPane jbScrollPane = new JBScrollPane(table);
        jbScrollPane.setBorder(JBUI.Borders.empty());
        jbScrollPane.getViewport().setBackground(JBUI.CurrentTheme.TabbedPane.ENABLED_SELECTED_COLOR);

        navPanel = new WorkProgressNavPanel(this);

        add(jbScrollPane, BorderLayout.CENTER);
        add(navPanel, BorderLayout.SOUTH);
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    WpsDto wpsDto = getSelectRow();
                    JDWorkProgressFormDialog dialog=new JDWorkProgressFormDialog(project, wpsDto,WorkProgressPanel.this);
                    dialog.show();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public void setData(WpsPageDto pageData) {
        clear();
        this.pageData = pageData;
        navPanel.setPageDto(pageData);
        List<WorkProgressGridData> gridDataList = new ArrayList<>();
        WorkProgressGridData gridData;
        for (WpsDto wpsDto : pageData.getPageData()) {
            gridData = new WorkProgressGridData();
            gridDataList.add(gridData);
            gridData.setTitle(wpsDto.getProjectName());
            gridData.setProgressStatus(WorkProgressStatusEnum.queryStatusEnum(wpsDto.getProgressStatus()).toString());
            String planWorkHours = convertDate(wpsDto.getPlanStartTime());
            if (wpsDto.getPlanEndTime() != null) {
                planWorkHours += "至" + convertDate(wpsDto.getPlanEndTime());
            }
            gridData.setPlanWorkHours(planWorkHours);
            gridData.setProductManager(wpsDto.getProductManager());
            gridData.setUserCode(wpsDto.getUserCode());
        }
        model.setRowCount(0);
        Vector<String> bean;
        for (WorkProgressGridData data : gridDataList) {
            bean = new Vector<String>();
            bean.add(data.getProgressStatus());
            bean.add(data.getTitle());
            bean.add(data.getPlanWorkHours());
            bean.add(data.getProductManager());
            bean.add(data.getUserCode());
            model.addRow(bean);
        }

//        model.setDataVector(tableData, columnNames);

    }

    private static String convertDate(String time) {
        if (StringUtils.isBlank(time)) {
            return "";
        }
        String viewDate;
        try {
            Date date = DateUtils.parseDate(time, WpsConfig.dateFormatPattern);
            viewDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return viewDate;
    }

    public WpsPageDto getPageData() {
        return pageData;
    }

    public void clear() {
        model.setRowCount(0);
    }

    public void refreshTableData() {
        refreshTableData(null);
    }
    public void resetTableData() {
        navPanel.setCpage(1L);
        refreshTableData(null);
    }
    public void refreshTableData(String devBranName) {
        WpsQueryDto queryDto;
        if (StringUtils.isNotBlank(devBranName)) {
            navPanel.setCpage(1L);
            queryDto=new WpsQueryDto();
            String currentUserCode = WpsConfig.getInstance().getCurrentUserCode();
            queryDto.setUserCode(currentUserCode);
            queryDto.setDevBranchName(devBranName);
        }else{
            queryDto = rootPanel.getSearchComboBoxPanel().getQueryDto();
        }
        queryDto.setCpage(navPanel.getCpage());
        queryDto.setPageSize(navPanel.getPageSize());
        AsyncTableWorker asyncTableWorker = new AsyncTableWorker(queryDto);
        asyncTableWorker.execute();
    }

    private void responseTableData(ResultDto<WpsPageDto> resultDto) {
        if (resultDto == null) {
            return;
        }
        if (!resultDto.isSuccess()) {
            clear();
            Messages.showInfoMessage(resultDto.getResultMessage(),"错误提示");
        }else{
            setData(resultDto.getResultValue());
        }
    }

    public WpsDto getSelectRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }
        return pageData.getPageData().get(selectedRow);
    }

    private class AsyncTableWorker extends SwingWorker<ResultDto<WpsPageDto>, Void> {
        private final WpsQueryDto queryDto;

        public AsyncTableWorker(WpsQueryDto queryDto) {
            this.queryDto = queryDto;
        }

        @Override
        protected ResultDto<WpsPageDto> doInBackground() {

            return RestUtils.post(WpsPageDto.class, "/wps/list", queryDto);
        }

        @Override
        protected void done() {
            ResultDto<WpsPageDto> resultDto = null;
            try {
                resultDto = get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            responseTableData(resultDto);
        }
    }
}
