package com.ljc.workprogress.ui;

import com.intellij.util.ui.JBUI;

import javax.swing.*;

/**
 * @author liangjichao
 * @date 2023/11/8 9:51 AM
 */
public class JDIconButton extends JButton {
    public JDIconButton(Icon icon) {
        super(icon);
        setPreferredSize(JBUI.size(30, 30));
    }
}
