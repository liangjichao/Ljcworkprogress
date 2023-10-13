package com.jdl.ljc.joyworkprogress.ui;

import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

public class DisabledMenuItem extends JBMenuItem {
    public DisabledMenuItem(String text) {
        super(text);
        setEnabled(false);
        setFont(new Font("微软雅黑", Font.BOLD, getFont().getSize()));
    }
}
