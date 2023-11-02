package com.jdl.ljc.joyworkprogress.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.jdl.ljc.joyworkprogress.ui.panel.WorkProgressPanel;
import com.jdl.ljc.joyworkprogress.util.ProjectUtils;

import javax.swing.*;

public class LocateWorkProgressDialogAction extends AnAction {
    private WorkProgressPanel panel;

    public LocateWorkProgressDialogAction(Icon icon, WorkProgressPanel panel) {
        super(icon);
        this.panel = panel;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        String currentBranchName = ProjectUtils.getCurrentBranchName(project);

        panel.refreshTableData(currentBranchName);
    }
}
