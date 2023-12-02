package com.ljc.workprogress.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.ljc.workprogress.ui.dialog.JDWorkProgressFormDialog;
import com.ljc.workprogress.ui.panel.WorkProgressPanel;

import javax.swing.*;

public class AddWorkProgressDialogAction extends AnAction {
    private final WorkProgressPanel panel;

    public AddWorkProgressDialogAction(Icon icon, WorkProgressPanel panel) {
        super(icon);
        this.panel = panel;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);

        JDWorkProgressFormDialog dialog=new JDWorkProgressFormDialog(project,null,panel);
        dialog.show();
    }
}
