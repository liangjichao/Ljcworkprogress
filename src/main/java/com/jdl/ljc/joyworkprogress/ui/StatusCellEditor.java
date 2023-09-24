package com.jdl.ljc.joyworkprogress.ui;

import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import java.util.Vector;

public class StatusCellEditor extends DefaultCellEditor {
    private JBTable table;
    private Vector tableData;

    public StatusCellEditor(JComboBox comboBox, JBTable table, Vector tableData) {
        super(comboBox);
        this.table = table;
        this.tableData = tableData;
        addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                // 单元格编辑完成时的处理逻辑
                int row = table.getSelectedRow();
                DefaultCellEditor ce = (DefaultCellEditor) e.getSource();
                JDComboBox cb = (JDComboBox) ce.getComponent();
                if (row < 0) {
                    cb.setTimes(1);
                }
                if (row >= 0) {
                    cb.setRowData((Vector) tableData.get(row));
                    cb.addTimes();
                }
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
                // 单元格编辑取消时的处理逻辑
            }
        });
    }

}
