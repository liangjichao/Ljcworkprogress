package com.jdl.ljc.joyworkprogress.ui;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class JDTableModel extends DefaultTableModel {

    public JDTableModel(Vector data, Vector columnNames) {
        setDataVector(data, columnNames);
    }
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
