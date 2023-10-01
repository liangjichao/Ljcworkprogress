package com.jdl.ljc.joyworkprogress.ui.panel;

import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.table.JBListTable;
import com.intellij.util.ui.table.JBTableRowEditor;
import com.intellij.util.ui.table.JBTableRowRenderer;
import com.jdl.ljc.joyworkprogress.ui.JDTableModel;
import com.jdl.ljc.joyworkprogress.vo.WorkProgressGridData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class WorkProgressPanel extends JBPanel {
    private JBTable table;
    private Vector tableData;
    private JDTableModel model;

    private Vector<String> columnNames;
    public WorkProgressPanel(List<WorkProgressGridData> gridDataList) {
        super(new BorderLayout());
        // 创建表头和表格数据

        columnNames = new Vector<>();
        columnNames.add("进度");
        columnNames.add("标题");
        columnNames.add("计划工时");

        tableData = new Vector();
        Vector bean=null;
        for (WorkProgressGridData data : gridDataList) {
            bean = new Vector();
            bean.add(data.getProgressStatus());
            bean.add(data.getTitle());
            bean.add(data.getPlanWorkHours());
            tableData.add(bean);
        }

        // 创建默认的表格模型
        model = new JDTableModel(tableData, columnNames);


        // 创建JBTable并设置模型
        table = new JBTable(model);


        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setMaxWidth(70);
        table.getColumnModel().getColumn(2).setMinWidth(138);
        table.getColumnModel().getColumn(2).setMaxWidth(138);

        JBScrollPane jbScrollPane = new JBScrollPane(table);
        jbScrollPane.setBorder(JBUI.Borders.empty());
        jbScrollPane.getViewport().setBackground(JBUI.CurrentTheme.TabbedPane.ENABLED_SELECTED_COLOR);

        add(jbScrollPane, BorderLayout.CENTER);


    }

    public void refreshTable(List<WorkProgressGridData> gridDataList) {
        tableData = new Vector();
        Vector bean=null;
        for (WorkProgressGridData data : gridDataList) {
            bean = new Vector();
            bean.add(data.getProgressStatus());
            bean.add(data.getTitle());
            bean.add(data.getPlanWorkHours());
            tableData.add(bean);
        }
        model.setDataVector(tableData,columnNames);
        model.fireTableDataChanged();

    }

    public Vector getSelectRow() {
        return (Vector) tableData.get(table.getSelectedRow());
    }
}