package com.jdl.ljc.joyworkprogress.ui.panel;

import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import com.jdl.ljc.joyworkprogress.domain.WpsConfig;
import com.jdl.ljc.joyworkprogress.domain.dto.ResultDto;
import com.jdl.ljc.joyworkprogress.domain.dto.WpsDto;
import com.jdl.ljc.joyworkprogress.domain.vo.WorkProgressGridData;
import com.jdl.ljc.joyworkprogress.domain.vo.WpsQueryDto;
import com.jdl.ljc.joyworkprogress.enums.WorkProgressStatusEnum;
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
    private Vector<Vector<String>> tableData;
    private final JDTableModel model;
    private final Vector<String> columnNames;
    private List<WpsDto> dataList;

    public WorkProgressPanel() {
        super(new BorderLayout(),true);
        // 创建表头和表格数据
        columnNames = new Vector<>();
        columnNames.add("进度");
        columnNames.add("项目名称");
        columnNames.add("计划工时");
        columnNames.add("用户");
        // 创建默认的表格模型
        model = new JDTableModel(tableData, columnNames);
        // 创建JBTable并设置模型
        table = new JBTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setMaxWidth(70);
        table.getColumnModel().getColumn(2).setMinWidth(138);
        table.getColumnModel().getColumn(2).setMaxWidth(138);
        table.getColumnModel().getColumn(3).setMinWidth(80);
        table.getColumnModel().getColumn(3).setMaxWidth(80);

        JBScrollPane jbScrollPane = new JBScrollPane(table);
        jbScrollPane.setBorder(JBUI.Borders.empty());
        jbScrollPane.getViewport().setBackground(JBUI.CurrentTheme.TabbedPane.ENABLED_SELECTED_COLOR);

        WorkProgressNavPanel navPanel = new WorkProgressNavPanel(this);

        add(jbScrollPane, BorderLayout.CENTER);
        add(navPanel, BorderLayout.SOUTH);
    }

    public void setData(List<WpsDto> wpsDtoList) {
        this.dataList = wpsDtoList;
        List<WorkProgressGridData> gridDataList = new ArrayList<>();
        WorkProgressGridData gridData;
        for (WpsDto wpsDto : wpsDtoList) {
            gridData = new WorkProgressGridData();
            gridDataList.add(gridData);
            gridData.setTitle(wpsDto.getProjectName());
            gridData.setProgressStatus(WorkProgressStatusEnum.queryStatusEnum(wpsDto.getProgressStatus()).toString());
            String planWorkHours = convertDate(wpsDto.getPlanStartTime());
            if (wpsDto.getPlanEndTime() != null) {
                planWorkHours += "-" + convertDate(wpsDto.getPlanEndTime());
            }
            gridData.setPlanWorkHours(planWorkHours);
            gridData.setUserCode(wpsDto.getUserCode());
        }
        tableData = new Vector<Vector<String>>();
        Vector<String> bean;
        for (WorkProgressGridData data : gridDataList) {
            bean = new Vector<String>();
            bean.add(data.getProgressStatus());
            bean.add(data.getTitle());
            bean.add(data.getPlanWorkHours());
            bean.add(data.getUserCode());
            tableData.add(bean);
        }

        model.setDataVector(tableData, columnNames);

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

    public void clear() {
        model.setRowCount(0);
    }

    public void refreshTableData(WpsQueryDto queryDto) {
        clear();
        if (queryDto == null) {
            queryDto=new WpsQueryDto();
        }
        AsyncTableWorker asyncTableWorker = new AsyncTableWorker(queryDto);
        asyncTableWorker.execute();
    }

    private void responseTableData(ResultDto<List<WpsDto>> resultDto) {
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
        return dataList.get(selectedRow);
    }

    private class AsyncTableWorker extends SwingWorker<ResultDto<List<WpsDto>>, Void> {
        private final WpsQueryDto queryDto;
        public AsyncTableWorker(WpsQueryDto queryDto) {
            this.queryDto=queryDto;
        }
        @Override
        protected ResultDto<List<WpsDto>> doInBackground(){

            return RestUtils.postList(WpsDto.class, "/wps/list", queryDto);
        }

        @Override
        protected void done() {
            ResultDto<List<WpsDto>> resultDto = null;
            try {
                resultDto = get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            responseTableData(resultDto);
        }
    }
}
