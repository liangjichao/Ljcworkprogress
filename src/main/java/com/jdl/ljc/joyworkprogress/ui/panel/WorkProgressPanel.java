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

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

public class WorkProgressPanel extends JBPanel {
    private JBTable table;
    private Vector tableData;
    private JDTableModel model;

    private Vector<String> columnNames;

    private List<WorkProgressGridData> gridDataList;

    public WorkProgressPanel() {
        super(new BorderLayout());
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

        add(jbScrollPane, BorderLayout.CENTER);


    }


    public void setData(List<WorkProgressGridData> gridDataList) {
        this.gridDataList = gridDataList;
        tableData = new Vector();
        Vector bean;
        for (WorkProgressGridData data : gridDataList) {
            bean = new Vector();
            bean.add(data.getProgressStatus());
            bean.add(data.getTitle());
            bean.add(data.getPlanWorkHours());
            bean.add(data.getUserCode());
            tableData.add(bean);
        }

        model.setDataVector(tableData, columnNames);

    }

    public void clear() {
        model.setRowCount(0);
    }

    public void refreshTableData() {
        clear();
        AsyncTableWorker asyncTableWorker = new AsyncTableWorker();
        asyncTableWorker.execute();
    }

    private void responseTableData(ResultDto<List<WpsDto>> resultDto) {
        if (resultDto == null) {
            return;
        }
        List<WorkProgressGridData> gridDataList = new ArrayList<>();
        WorkProgressGridData data;
        for (WpsDto wpsDto : resultDto.getResultValue()) {
            data = new WorkProgressGridData();
            gridDataList.add(data);
            data.setTitle(wpsDto.getProjectName());
            data.setProgressStatus(WorkProgressStatusEnum.queryStatusEnum(wpsDto.getProgressStatus()).toString());
            String planWorkHours = wpsDto.getPlanStartTime();
            if (wpsDto.getPlanEndTime() != null) {
                planWorkHours += "-" + wpsDto.getPlanEndTime();
            }
            data.setPlanWorkHours(planWorkHours);
            data.setUserCode(wpsDto.getUserCode());
        }
        setData(gridDataList);
    }

    public WorkProgressGridData getSelectRow() {
        return gridDataList.get(table.getSelectedRow());
    }

    private class AsyncTableWorker extends SwingWorker<ResultDto<List<WpsDto>>, Void> {

        @Override
        protected ResultDto<List<WpsDto>> doInBackground() throws Exception {
            WpsQueryDto queryDto = new WpsQueryDto();
            String currentUserCode = WpsConfig.getInstance().getCurrentUserCode();
            queryDto.setUserCode(currentUserCode);

            ResultDto<List<WpsDto>> resultDto = RestUtils.postList(WpsDto.class, "/wps/list", queryDto);
            return resultDto;
        }

        @Override
        protected void done() {
            ResultDto<List<WpsDto>> resultDto = null;
            try {
                resultDto = get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            responseTableData(resultDto);
        }
    }
}
