package com.jdl.ljc.joyworkprogress.ui;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.popup.AbstractPopup;
import com.intellij.util.ui.JBUI;
import com.jdl.ljc.joyworkprogress.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.PanelUI;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class UserMenuItem extends JMenuItem {

    private UserMenu userMenu;
    private String originalText;

    public UserMenuItem(String text, UserMenu userMenu) {
        super(String.format("<html>&nbsp;&nbsp;%s</html>", text));
        this.originalText = text;
        this.userMenu = userMenu;
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("已选菜单：" + originalText);
                if (originalText.equals("Select...")) {
                    SelectUserPanel selectUserPanel = new SelectUserPanel();
                    JBPopup ourPopup = JBPopupFactory.getInstance().createComponentPopupBuilder(selectUserPanel, selectUserPanel)
                            .setRequestFocus(true).setFocusable(true).setResizable(false).setMovable(false)
                            .setModalContext(false).setShowShadow(true).setShowBorder(false)
                            .setCancelKeyEnabled(true).setCancelOnClickOutside(true)
                            .setCancelOnOtherWindowOpen(true).createPopup();
                    RelativePoint loc = new RelativePoint(userMenu, new Point(0, 0+userMenu.getSize().height));
                    ourPopup.show(loc);
                    selectUserPanel.textFocus();
                }else{
                    userMenu.editSelectedMenuItem(originalText);
                }
            }
        });
    }

    private class UserListPanel extends JBPanel{
        private JList<String> list;
        private DefaultListModel<String> listModel;
        public UserListPanel() {
            super(new BorderLayout());
            listModel = new DefaultListModel<>();
            list = new JBList<>(listModel);
            add(new JScrollPane(list), BorderLayout.CENTER);
        }

        public void add(List<String> eleList) {
            listModel.clear();
            for (String s : eleList) {
                listModel.addElement(s);
            }
            list.setSelectedIndex(0);

        }


    }

    private class SelectUserPanel extends JBPanel{

        private JTextArea textArea;
        private JLabel label;

        private Timer timer;
        private List<String> suggestions = new ArrayList<>();
        private UserListPanel listPanel;
        private JBPopup listPop;
        public SelectUserPanel() {
            super(new BorderLayout(3, 3));
            setBorder(JBUI.Borders.empty(3,3,3,3));
            textArea = new JTextArea(3, 20);
            label = new JLabel("请选择一个或多个用户,间隔符为|或换行，Ctrl+Enter完成选择,ESC退出");
            add(new JScrollPane(textArea), BorderLayout.CENTER);
            add(label, BorderLayout.SOUTH);


            listPanel = new UserListPanel();

            suggestions.add("apple");
            suggestions.add("banana");
            suggestions.add("banxyz");
            suggestions.add("cherry");


            Action enterAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("确认内容：" + textArea.getText());
                }
            };
            KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);
            textArea.getInputMap(JComponent.WHEN_FOCUSED).put(keyStroke, "ctrlEnter");
            textArea.getActionMap().put("ctrlEnter", enterAction);

            ActionListener popMenuListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JMenuItem item = (JMenuItem) e.getSource();
                    String itemText = item.getText();
                    textArea.insert(itemText,textArea.getCaretPosition());

                }
            };

            textArea.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateSuggestions();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    closePop();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {

                }

                private void updateSuggestions() {

                    String text = StringUtils.lastSplitText( textArea.getText());
                    if (StringUtil.isNotEmpty(text)) {

                        timer = new Timer(200, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                List<String> its = new ArrayList<>();
                                for (String suggestion : suggestions) {
                                    if (suggestion.startsWith(text)) {

                                        its.add(suggestion);
                                    }
                                }
                                if (its.size() > 0) {
                                    listPanel.add(its);
                                    int x = textArea.getCaret().getMagicCaretPosition().x;
                                    int y = textArea.getCaret().getMagicCaretPosition().y;
                                    FontMetrics fontMetrics = textArea.getFontMetrics(textArea.getFont());
                                    int tw = fontMetrics.stringWidth(text);
                                    x=x-tw;
                                    y=y + textArea.getFont().getSize();

                                    listPop = JBPopupFactory.getInstance().createComponentPopupBuilder(listPanel, listPanel)
                                            .setRequestFocus(true).setFocusable(true).setResizable(false).setMovable(false)
                                            .setModalContext(false).setShowShadow(true).setShowBorder(false)
                                            .setCancelKeyEnabled(true).setCancelOnClickOutside(true)
                                            .setCancelOnOtherWindowOpen(true).createPopup();

                                    RelativePoint loc = new RelativePoint(textArea, new Point(x, y));
                                    listPop.show(loc);

                                    textArea.requestFocus();
                                }else{
                                    closePop();
                                }
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                }

                private void closePop() {
                    if (listPop != null && listPop.isVisible()) {

                        listPop.cancel();
                    }
                }
            });
        }

        public void textFocus() {
            textArea.requestFocus();
        }
    }

}
