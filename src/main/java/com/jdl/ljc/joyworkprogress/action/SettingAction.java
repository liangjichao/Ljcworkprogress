package com.jdl.ljc.joyworkprogress.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.jdl.ljc.joyworkprogress.config.WpsPluginSetting;
import org.jetbrains.annotations.NotNull;

/**
 * @author liangjichao
 * @date 2023/10/18 5:35 PM
 */
public class SettingAction extends AnAction {

    public SettingAction() {
        super(AllIcons.General.Settings);
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Messages.showInfoMessage("配置信息："+WpsPluginSetting.getInstance().getState().domain,"提示");
        WpsPluginSetting.getInstance().getState().domain = "localhost:8080";
    }
}
