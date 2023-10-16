package com.jdl.ljc.joyworkprogress.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.jdl.ljc.joyworkprogress.ui.panel.WorkProgressPanel;

import javax.swing.*;

public class SearchWorkProgressDialogAction extends AnAction {
    private WorkProgressPanel panel;

    public SearchWorkProgressDialogAction(Icon icon, WorkProgressPanel panel) {
//        super(icon);
        super("搜索",null,icon);
        this.panel = panel;

    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);

    }

    @Override
    public boolean displayTextInToolbar() {
        return true;
    }


}
