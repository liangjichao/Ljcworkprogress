package com.jdl.ljc.joyworkprogress.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class JDComboBox<T> extends JComboBox {
    private Vector rowData;
    private int times;

    public JDComboBox() {
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("comboBoxChanged")) {
                    if (getRowData() != null) {
                        if (getTimes() > 1) {
                            System.out.println("已选中:" + getRowData().get(1));
                            setTimes(0);
                        }
                        setRowData(null);
                    }
                }
            }
        });
    }


    public Vector getRowData() {
        return rowData;
    }

    public void setRowData(Vector rowData) {
        this.rowData = rowData;
    }

    public int getTimes() {
        return times;
    }

    public void addTimes() {
        this.times++;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
