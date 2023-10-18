package com.jdl.ljc.joyworkprogress.ui.panel;

import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import com.jdl.ljc.joyworkprogress.domain.WpsConfig;
import com.jdl.ljc.joyworkprogress.domain.dto.ResultDto;
import com.jdl.ljc.joyworkprogress.domain.dto.WpsDto;
import com.jdl.ljc.joyworkprogress.domain.dto.WpsPageDto;
import com.jdl.ljc.joyworkprogress.domain.vo.WorkProgressGridData;
import com.jdl.ljc.joyworkprogress.domain.vo.WpsQueryDto;
import com.jdl.ljc.joyworkprogress.enums.WorkProgressStatusEnum;
import com.jdl.ljc.joyworkprogress.toolwindow.HomeToolWindowPanel;
import com.jdl.ljc.joyworkprogress.ui.JDTableModel;
import com.jdl.ljc.joyworkprogress.util.RestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.swing.*;
import java.awt.*;
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

    public WorkProgressPanel(HomeToolWindowPanel rootPanel) {
        super(new BorderLayout(), true);
        this.rootPanel=rootPanel;
        // 创建表头和表格数据
        Vector<String> columnNames = new Vector<>();
        columnNames.add("进度");
        columnNames.add("项目名称");
        columnNames.add("计划工时");
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

        JBScrollPane jbScrollPane = new JBScrollPane(table);
        jbScrollPane.setBorder(JBUI.Borders.empty());
        jbScrollPane.getViewport().setBackground(JBUI.CurrentTheme.TabbedPane.ENABLED_SELECTED_COLOR);

        navPanel = new WorkProgressNavPanel(this);

        add(jbScrollPane, BorderLayout.CENTER);
        add(navPanel, BorderLayout.SOUTH);
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
            gridData.setUserCode(wpsDto.getUserCode());
        }
        model.setRowCount(0);
        Vector<String> bean;
        for (WorkProgressGridData data : gridDataList) {
            bean = new Vector<String>();
            bean.add(data.getProgressStatus());
            bean.add(data.getTitle());
            bean.add(data.getPlanWorkHours());
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
        setData(resultDto.getResultValue());
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
