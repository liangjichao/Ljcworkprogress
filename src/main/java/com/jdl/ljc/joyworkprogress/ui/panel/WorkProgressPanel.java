package com.jdl.ljc.joyworkprogress.ui.panel;

import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import com.jdl.ljc.joyworkprogress.enums.WorkProgressStatusEnum;
import com.jdl.ljc.joyworkprogress.ui.JDComboBox;
import com.jdl.ljc.joyworkprogress.ui.StatusCellEditor;
import com.jdl.ljc.joyworkprogress.ui.TitleCellEditor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class WorkProgressPanel extends JBPanel {
    private JBTable table;
    private Vector tableData;

    public WorkProgressPanel() {
        super(new BorderLayout());
        // 创建表头和表格数据

        Vector<String> columnNames = new Vector<>();
        columnNames.add("标题");
        columnNames.add("状态");
        columnNames.add("描述");

        tableData = new Vector();
        Vector bean = new Vector();
        bean.add("John Doe");
        bean.add(WorkProgressStatusEnum.DEFAULT);
        bean.add("john.doe@example.com");
        tableData.add(bean);
        bean = new Vector();
        bean.add("Jane Smith");
        bean.add(WorkProgressStatusEnum.UN_DEV);
        bean.add("jane.smith@example.com");
        tableData.add(bean);

        bean = new Vector();
        bean.add("Bob Johnson");
        bean.add(WorkProgressStatusEnum.DEV_ING);
        bean.add("bob.johnson@example.com");
        tableData.add(bean);


        // 创建默认的表格模型
        DefaultTableModel model = new DefaultTableModel(tableData, columnNames);

        // 创建JBTable并设置模型
        table = new JBTable(model);

        //为状态添加下拉框编辑器
        JDComboBox<WorkProgressStatusEnum> comboBox = new JDComboBox<>();
        for (WorkProgressStatusEnum value : WorkProgressStatusEnum.values()) {
            comboBox.addItem(value);
        }
        StatusCellEditor statusCellEditor = new StatusCellEditor(comboBox, table, tableData);
        table.getColumnModel().getColumn(1).setCellEditor(statusCellEditor);
        TitleCellEditor titleCellEditor = new TitleCellEditor(new JTextField(), table, model, tableData);
        table.getColumnModel().getColumn(0).setCellEditor(titleCellEditor);

        JBScrollPane jbScrollPane = new JBScrollPane(table);
        jbScrollPane.setBorder(JBUI.Borders.empty());
        jbScrollPane.getViewport().setBackground(JBUI.CurrentTheme.TabbedPane.ENABLED_SELECTED_COLOR);

        add(jbScrollPane, BorderLayout.CENTER);
    }

    public Vector getSelectRow() {
        return (Vector) tableData.get(table.getSelectedRow());
    }
}
