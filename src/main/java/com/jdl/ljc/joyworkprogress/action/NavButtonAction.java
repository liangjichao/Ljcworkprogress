package com.jdl.ljc.joyworkprogress.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.jdl.ljc.joyworkprogress.domain.dto.WpsPageDto;
import com.jdl.ljc.joyworkprogress.enums.NavButtonEnum;
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
        this.uid = uid;
        this.navPanel = navPanel;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        if (NavButtonEnum.NEXT.name().equals(uid)) {
            navPanel.setCpage(navPanel.getCpage() + 1);
            navPanel.updateNav();
        } else if (NavButtonEnum.PREV.name().equals(uid)) {
            navPanel.setCpage(navPanel.getCpage() - 1);
            navPanel.updateNav();
        } else if (NavButtonEnum.FIRST.name().equals(uid)) {
            navPanel.setCpage(1L);
            navPanel.updateNav();
        } else if (NavButtonEnum.LAST.name().equals(uid)) {
            navPanel.setCpage(navPanel.getPageDto().getTotalPage());
            navPanel.updateNav();
        }
        updatePageNavBtn(e);
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
        updatePageNavBtn(e);
    }

    private void updatePageNavBtn(@NotNull AnActionEvent e) {
        Long cpage = navPanel.getCpage();
        WpsPageDto pageDto = navPanel.getPageDto();
        if (NavButtonEnum.NEXT.name().equals(uid) || NavButtonEnum.LAST.name().equals(uid)) {
            if (pageDto == null || pageDto.getTotalPage() <= 1 || pageDto.getTotalPage().equals(cpage)) {
                e.getPresentation().setEnabled(false);
            } else if (pageDto != null && pageDto.getTotalPage() >= cpage + 1) {
                e.getPresentation().setEnabled(true);
            }
        } else if (NavButtonEnum.PREV.name().equals(uid) || NavButtonEnum.FIRST.name().equals(uid)) {
            if (pageDto == null || cpage <= 1) {
                e.getPresentation().setEnabled(false);
            } else if (pageDto != null && cpage > 1) {
                e.getPresentation().setEnabled(true);
            }
        }
    }
}
