package com.jdl.ljc.joyworkprogress.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.jdl.ljc.joyworkprogress.config.WpsPluginSetting;
import com.jdl.ljc.joyworkprogress.ui.dialog.WpsSettingDialog;
import org.jetbrains.annotations.NotNull;

/**
 * @author liangjichao
 * @date 2023/10/18 5:35 PM
 */
public class SettingAction extends AnAction {
    private Project project;
    public SettingAction(Project project) {
        super(AllIcons.General.Settings);
        this.project=project;
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        WpsSettingDialog dialog = new WpsSettingDialog(project);
        dialog.show();
    }
}
