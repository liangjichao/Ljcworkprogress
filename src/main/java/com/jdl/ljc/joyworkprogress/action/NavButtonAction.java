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
public class NavButtonAction extends AnAction {
    private WorkProgressNavPanel navPanel;
    private String uid;
    public NavButtonAction(String uid, Icon icon, WorkProgressNavPanel navPanel) {
        super(icon);
        this.uid=uid;
        this.navPanel=navPanel;
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        if (this.uid.equals("next")) {
            navPanel.setCpage(navPanel.getCpage()+1);
            navPanel.updateNav();
        }
    }
    @Override
    public boolean displayTextInToolbar() {
        return false;
    }
    @Override
    public void update(@NotNull AnActionEvent e) {
        if (!WorkProgressNavPanel.place.equals(e.getPlace())) {
            return;
        }
        if (this.uid.equals("next")) {
            if (navPanel.getPageDto() == null || navPanel.getPageDto().getTotalPage() <= 1||navPanel.getPageDto().getTotalPage()==navPanel.getCpage()) {
                e.getPresentation().setEnabled(false);
            } else if (navPanel.getPageDto() != null && navPanel.getPageDto().getTotalPage() >= navPanel.getCpage() + 1) {
                e.getPresentation().setEnabled(true);
            }
        }
    }
}
