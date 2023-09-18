package com.jdl.ljc.joyworkprogress.ui.panel;

import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;

import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class WorkProgressPanel extends JBPanel {

    public WorkProgressPanel() {
        super(new BorderLayout());
        // 创建表头和表格数据
        String[] columnNames = {"Name", "Age", "Email", "Action"};
        Object[][] rowData = {
                {"John Doe", 30, "john.doe@example.com", "Edit"},
                {"Jane Smith", 25, "jane.smith@example.com", "Edit"},
                {"Bob Johnson", 35, "bob.johnson@example.com", "Edit"}
        };

        // 创建默认的表格模型
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);

        // 创建JBTable并设置模型
        JBTable table = new JBTable(model);

        JBScrollPane jbScrollPane = new JBScrollPane(table);
        jbScrollPane.setBorder(JBUI.Borders.empty());
        jbScrollPane.getViewport().setBackground(JBUI.CurrentTheme.TabbedPane.ENABLED_SELECTED_COLOR);

        add(jbScrollPane, BorderLayout.CENTER);
    }
}
