package com.jdl.ljc.joyworkprogress.ui.panel;

import com.intellij.find.SearchTextArea;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.ui.ExperimentalUI;
import com.intellij.ui.IconManager;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import com.jdl.ljc.joyworkprogress.domain.WpsConfig;
import com.jdl.ljc.joyworkprogress.domain.vo.WpsQueryDto;
import com.jdl.ljc.joyworkprogress.ui.UserMenu;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SearchComboBoxPanel extends JBPanel implements Disposable {
    private JMenuBar menuBar;
    private UserMenu userMenu;
    private ActionButton iconBtn;
    private WorkProgressPanel panel;
    private SearchTextArea searchArea;

    public SearchComboBoxPanel(WorkProgressPanel panel) {
        super(new FlowLayout());
        this.panel=panel;
        menuBar = new JMenuBar();
        menuBar.setBorder(JBUI.Borders.empty());


        UserMenu menu = new UserMenu("User", AllIcons.General.ArrowDown);
        userMenu = menu;
        userMenu.setSelectedRun(new Runnable() {
            @Override
            public void run() {
                iconBtn.setVisible(true);
                if (StringUtils.isNotBlank(userMenu.getSelectedText())) {
                    panel.refreshTableData(getQueryDto());
                }
            }
        });
        menuBar.add(userMenu);
        iconBtn = new CancelActionButton(new CancelAction(), "CANCEL_SELECTED_WORK_USER");
        iconBtn.setVisible(false);


        JBTextArea myTextArea = new JBTextArea(1, 10);
        searchArea = new SearchTextArea(myTextArea, false);
        searchArea.setMultilineEnabled(false);
        myTextArea.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (SearchTextArea.JUST_CLEARED_KEY.equals(evt.getPropertyName())) {
                    Object v = evt.getNewValue();
                    if (v != null) {
                        myTextArea.setText("");
                        panel.refreshTableData(getQueryDto());
                    }
                }
            }
        });
        searchArea.getTextArea().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    panel.refreshTableData(getQueryDto());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        add(searchArea);

        add(menuBar);
        add(iconBtn);
    }

    private WpsQueryDto getQueryDto() {
        WpsQueryDto queryDto = new WpsQueryDto();
        queryDto.setProjectName(searchArea.getTextArea().getText());
        String selectedText = userMenu.getSelectedText();
        if (selectedText!=null&&selectedText.equals("me")) {
            selectedText= WpsConfig.getInstance().getCurrentUserCode();
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
        private static final Icon CLOSE_ICON = ExperimentalUI.isNewUI() ?
                IconManager.getInstance().getIcon("expui/general/closeSmall.svg", AllIcons.class) :
                AllIcons.Actions.Close;

        public CancelAction() {
            super(CLOSE_ICON);
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            iconBtn.setVisible(false);
            userMenu.setShowMenuIcon(true);
            userMenu.setText("User");
            userMenu.setSelectedText("");

            panel.refreshTableData(getQueryDto());
        }
    }
}
