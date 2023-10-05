package com.jdl.ljc.joyworkprogress.ui.panel;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.util.ui.JBUI;
import com.jdl.ljc.joyworkprogress.ui.DisabledMenuItem;
import com.jdl.ljc.joyworkprogress.ui.UserMenu;
import com.jdl.ljc.joyworkprogress.ui.UserMenuItem;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class SearchComboBoxPanel extends JPanel {
    private JMenuBar menuBar;
    private UserMenu userMenu;
    private ActionButton iconBtn;


    public SearchComboBoxPanel() {
        super(new FlowLayout());

        menuBar = new JMenuBar();
        menuBar.setBorder(null);

        UserMenu menu = new UserMenu("User",AllIcons.General.ArrowDown);
        userMenu = menu;
        menu.setSelectedRun(new Runnable() {
            @Override
            public void run() {
                iconBtn.setVisible(true);
            }
        });
        menuBar.add(menu);
        menu.add(new UserMenuItem("Select...",menu));
        menu.add(new UserMenuItem("me",menu));
        menu.addSeparator();
        menu.add(new DisabledMenuItem("最近"));
        menu.add(new UserMenuItem("me",menu));
        menu.add(new UserMenuItem("liangjichao",menu));
        iconBtn = new CancelActionButton(new CancelAction(),"CANCEL_SELECTED_WORK_USER");
        iconBtn.setVisible(false);
        add(menuBar);
        add(iconBtn);
    }
    private class CancelActionButton extends ActionButton{

        public CancelActionButton(@NotNull AnAction action, String place) {
            super(action, action.getTemplatePresentation().clone(), place, JBUI.size(20, 20));
        }
    }
    private class CancelAction extends AnAction{
        public CancelAction() {
            super(AllIcons.Actions.DeleteTag);
        }
        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            iconBtn.setVisible(false);
            userMenu.setText("User");
            userMenu.setShowMenuIcon(true);
        }
    }
}
