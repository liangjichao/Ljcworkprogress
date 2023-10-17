package com.jdl.ljc.joyworkprogress.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.jdl.ljc.joyworkprogress.ui.panel.WorkProgressNavPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author liangjichao
 * @date 2023/10/17 12:09 PM
 */
public class NavPageButtonAction extends AnAction {

    public NavPageButtonAction(String title, Icon icon) {
        super(title,null,icon);


    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
    }
    @Override
    public boolean displayTextInToolbar() {
        return true;
    }


    @Override
    public void update(@NotNull AnActionEvent e) {
        if (WorkProgressNavPanel.place.equals(e.getPlace())) {

        }
    }
}
