package com.jdl.ljc.joyworkprogress.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.ProjectLevelVcsManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.jdl.ljc.joyworkprogress.ui.dialog.JDWorkProgressFormDialog;
import com.jdl.ljc.joyworkprogress.ui.panel.WorkProgressPanel;
import git4idea.GitUtil;
import git4idea.repo.GitRepository;

import javax.swing.*;

public class EditWorkProgressDialogAction extends AnAction {
    private WorkProgressPanel panel;

    public EditWorkProgressDialogAction(Icon icon, WorkProgressPanel panel) {
        super(icon);
        this.panel = panel;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        JDWorkProgressFormDialog dialog=new JDWorkProgressFormDialog(project,this.panel.getSelectRow());
        if (dialog.showAndGet()) {
            this.panel.refreshTableData();
        }
    }
}
