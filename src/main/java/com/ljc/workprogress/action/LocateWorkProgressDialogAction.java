package com.ljc.workprogress.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.ljc.workprogress.ui.panel.WorkProgressPanel;
import com.ljc.workprogress.util.ProjectUtils;

import javax.swing.*;

public class LocateWorkProgressDialogAction extends AnAction {
    private final WorkProgressPanel panel;

    public LocateWorkProgressDialogAction(Icon icon, WorkProgressPanel panel) {
        super(icon);
        this.panel = panel;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        if (project != null) {
            String currentBranchName = ProjectUtils.getCurrentBranchName(project);
            panel.refreshTableData(currentBranchName);
        }
    }
}
