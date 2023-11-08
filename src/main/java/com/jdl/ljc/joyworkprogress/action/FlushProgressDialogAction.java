package com.jdl.ljc.joyworkprogress.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.jdl.ljc.joyworkprogress.ui.panel.WorkProgressPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class FlushProgressDialogAction extends AnAction {
    private final WorkProgressPanel panel;

    public FlushProgressDialogAction(Icon icon, WorkProgressPanel panel) {
        super(icon);
        this.panel = panel;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        this.panel.refreshTableData();
    }
}
