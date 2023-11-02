package com.jdl.ljc.joyworkprogress.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.jdl.ljc.joyworkprogress.domain.dto.WpsDto;
import com.jdl.ljc.joyworkprogress.ui.dialog.JDWorkProgressFormDialog;
import com.jdl.ljc.joyworkprogress.ui.panel.WorkProgressPanel;

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
        WpsDto wpsDto = this.panel.getSelectRow();
        if (wpsDto == null) {
            Messages.showInfoMessage("请选择一条记录!","提示");
            return;
        }
        JDWorkProgressFormDialog dialog=new JDWorkProgressFormDialog(project, wpsDto,panel);
        dialog.show();

    }
}
