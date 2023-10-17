package com.jdl.ljc.joyworkprogress.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author liangjichao
 * @date 2023/10/17 12:09 PM
 */
public class NavButtonAction extends AnAction {

    private String uid;
    public NavButtonAction(String uid, Icon icon) {
        super(icon);

        this.uid=uid;
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
    }
    @Override
    public boolean displayTextInToolbar() {
        return false;
    }
    @Override
    public void update(@NotNull AnActionEvent e) {

    }
}
