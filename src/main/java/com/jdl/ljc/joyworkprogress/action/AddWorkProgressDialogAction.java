package com.jdl.ljc.joyworkprogress.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;

public class AddWorkProgressDialogAction extends AnAction {

    public AddWorkProgressDialogAction(Icon icon) {
        super(icon);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        Messages.showInputDialog(
                project,
                "What is your name?",
                "Input your name",
                Messages.getQuestionIcon());
    }
}
