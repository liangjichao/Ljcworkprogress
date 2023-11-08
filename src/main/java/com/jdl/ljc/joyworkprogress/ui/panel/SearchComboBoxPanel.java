package com.jdl.ljc.joyworkprogress.ui.panel;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.ui.ExperimentalUI;
import com.intellij.ui.IconManager;
import com.intellij.ui.SearchTextField;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;
import com.jdl.ljc.joyworkprogress.domain.WpsConfig;
import com.jdl.ljc.joyworkprogress.domain.vo.WpsQueryDto;
import com.jdl.ljc.joyworkprogress.toolwindow.HomeToolWindowPanel;
import com.jdl.ljc.joyworkprogress.ui.UserMenu;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class SearchComboBoxPanel extends JBPanel implements Disposable {
    private JMenuBar menuBar;
    private UserMenu userMenu;
    private ActionButton iconBtn;
    private SearchTextField searchArea;

    private HomeToolWindowPanel rootPanel;

    public SearchComboBoxPanel(HomeToolWindowPanel rootPanel) {
        super(new FlowLayout());
        this.rootPanel = rootPanel;
        menuBar = new JMenuBar();
        menuBar.setBorder(JBUI.Borders.empty());

        iconBtn = new CancelActionButton(new CancelAction(), "CANCEL_SELECTED_WORK_USER");
        iconBtn.setVisible(false);

        userMenu = new UserMenu(AllIcons.General.ArrowDown,iconBtn);
        userMenu.setSelectedRun(new Runnable() {
            @Override
            public void run() {
                String selectedText = userMenu.getSelectedText();
                selectedMenu(selectedText);
            }
        });
        menuBar.add(userMenu);

        searchArea = new SearchTextField(false) {
            @Override
            protected boolean preprocessEventForTextField(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    rootPanel.getGridPanel().refreshTableData();
                    return true;
                }
                return super.preprocessEventForTextField(e);
            }

            @Override
            protected void onFieldCleared() {
                super.onFieldCleared();
                rootPanel.getGridPanel().refreshTableData();
            }
        };
        add(searchArea);
        add(menuBar);
        add(iconBtn);
    }

    public void selectedMenu(String selectedText) {
        if (StringUtils.isNotBlank(selectedText)) {
            rootPanel.getGridPanel().refreshTableData();
        }
    }

    public WpsQueryDto getQueryDto() {
        WpsQueryDto queryDto = new WpsQueryDto();
        queryDto.setProjectName(searchArea.getText());
        String selectedText = userMenu.getSelectedText();
        if (selectedText != null && selectedText.equals("me")) {
            selectedText = WpsConfig.getInstance().getCurrentUserCode();
        }
        queryDto.setUserCode(selectedText);
        return queryDto;
    }


    @Override
    public void dispose() {

    }

    private class CancelActionButton extends ActionButton {

        public CancelActionButton(@NotNull AnAction action, String place) {
            super(action, action.getTemplatePresentation().clone(), place, JBUI.size(20, 20));
        }
    }

    private class CancelAction extends AnAction {

        public CancelAction() {
            super(AllIcons.Actions.Close);
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            iconBtn.setVisible(false);
            userMenu.setShowMenuIcon(true);
            userMenu.setText("User");
            userMenu.setSelectedText("");

            rootPanel.getGridPanel().refreshTableData();
        }
    }

    public UserMenu getUserMenu() {
        return userMenu;
    }
}
