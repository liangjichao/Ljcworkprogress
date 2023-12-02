package com.ljc.workprogress.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.ljc.workprogress.domain.dto.WpsPageDto;
import com.ljc.workprogress.ui.panel.WorkProgressNavPanel;
import org.jetbrains.annotations.NotNull;

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
            e.getPresentation().setText(title);
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return super.getActionUpdateThread();
    }
}
