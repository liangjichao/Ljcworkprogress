package com.jdl.ljc.joyworkprogress.ui;

import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class TitleCellEditor extends DefaultCellEditor {
    public TitleCellEditor(JTextField textField, JBTable table, DefaultTableModel model, Vector tableData) {
        super(textField);
        textField.addActionListener(e -> {
            int row = table.getSelectedRow();
            int column = table.getSelectedColumn();
            if (column == 0) {
                Vector rowData = (Vector) tableData.get(row);
                String newValue = textField.getText();
                System.out.println("旧值：" + rowData.get(0) + "设置新标题：" + newValue);
                model.setValueAt(newValue, row, column);
            }
        });
    }
}
