package com.jdl.ljc.joyworkprogress.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.jdl.ljc.joyworkprogress.ui.panel.WorkProgressPanel;

import javax.swing.*;
import java.util.Vector;

public class EditWorkProgressDialogAction extends AnAction {
    private WorkProgressPanel panel;

    public EditWorkProgressDialogAction(Icon icon, WorkProgressPanel panel) {
        super(icon);
        this.panel = panel;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        Vector data = panel.getSelectRow();
        Messages.showInputDialog(
                project,
                "我是编辑信息" + data.get(0),
                "编辑",
                Messages.getQuestionIcon());
    }
}
