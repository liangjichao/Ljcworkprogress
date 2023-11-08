package com.jdl.ljc.joyworkprogress.util;

import javax.swing.*;
import java.awt.*;

/**
 * @author liangjichao
 * @date 2023/11/8 10:46 AM
 */
public class FormUtils {
    public static int addRowForm(JPanel formPanel, GridBagConstraints constraints, JLabel label, JComponent textField, int y) {
        addInnerRow(formPanel, constraints, label, y);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        formPanel.add(textField, constraints);
        y++;
        return y;
    }

    public static int addRowFormUnFill(JPanel formPanel, GridBagConstraints constraints, JLabel label, JComponent textField, int y) {
        addInnerRow(formPanel, constraints, label, y);
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        formPanel.add(textField, constraints);
        y++;
        return y;
    }

    private static void addInnerRow(JPanel formPanel, GridBagConstraints constraints, JLabel label, int y) {
        constraints.gridx = 0;
        constraints.gridy = y;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        formPanel.add(label, constraints);
        constraints.gridx = 1;
    }
}
