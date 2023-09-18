package com.jdl.ljc.joyworkprogress.ui.panel;

import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;

import java.awt.*;

public class WorkProgressPanel extends JBScrollPane {

    public WorkProgressPanel(Component component) {
        super(component);
        setBorder(JBUI.Borders.empty());
        getViewport().setBackground(JBUI.CurrentTheme.TabbedPane.ENABLED_SELECTED_COLOR);
    }
}
