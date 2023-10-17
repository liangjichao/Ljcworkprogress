package com.jdl.ljc.joyworkprogress.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.jdl.ljc.joyworkprogress.domain.dto.WpsPageDto;
import com.jdl.ljc.joyworkprogress.ui.panel.WorkProgressNavPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author liangjichao
 * @date 2023/10/17 12:09 PM
 */
public class NavPageButtonAction extends AnAction {
    private static final String DEFAULT_FORMAT_TEXT = "%s/%s 总计：%s";
    private final WorkProgressNavPanel navPanel;
    public NavPageButtonAction(WorkProgressNavPanel navPanel) {
        super(String.format(DEFAULT_FORMAT_TEXT,1,1,0),null,null);
        this.navPanel = navPanel;

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
            String title = String.format(DEFAULT_FORMAT_TEXT,1,1,0);
            WpsPageDto pageDto = navPanel.getPageDto();
            if (pageDto != null) {
                title=String.format(DEFAULT_FORMAT_TEXT,pageDto.getCpage(),pageDto.getTotalPage(),pageDto.getRows());
            }
            System.out.println("Page Nav:"+title);
            e.getPresentation().setText(title);
        }
    }
}
