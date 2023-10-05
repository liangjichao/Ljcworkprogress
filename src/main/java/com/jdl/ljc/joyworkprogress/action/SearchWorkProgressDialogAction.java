package com.jdl.ljc.joyworkprogress.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.NlsActions;
import com.intellij.openapi.vfs.VirtualFile;
import com.jdl.ljc.joyworkprogress.ui.panel.WorkProgressPanel;
import git4idea.GitUtil;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SearchWorkProgressDialogAction extends AnAction {
    private WorkProgressPanel panel;

    public SearchWorkProgressDialogAction(Icon icon, WorkProgressPanel panel) {
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
