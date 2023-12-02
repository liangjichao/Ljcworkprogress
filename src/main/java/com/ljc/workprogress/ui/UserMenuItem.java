package com.ljc.workprogress.ui;

import com.google.common.base.Joiner;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.SystemInfoRt;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import com.ljc.workprogress.util.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class UserMenuItem extends JMenuItem {

    private UserMenu userMenu;
    private String originalText;

    private JBPopup searchUserPop;

    public UserMenuItem(String text, UserMenu userMenu) {
        super(String.format("<html>&nbsp;&nbsp;%s</html>", text));
        this.originalText = text;
        this.userMenu = userMenu;
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (originalText.equals("Select...")) {
                    SelectUserPanel selectUserPanel = new SelectUserPanel();
                    searchUserPop = JBPopupFactory.getInstance().createComponentPopupBuilder(selectUserPanel, selectUserPanel)
                            .setRequestFocus(true).setFocusable(true).setResizable(false).setMovable(false)
                            .setModalContext(false).setShowShadow(true).setShowBorder(true)
                            .setCancelKeyEnabled(true).setCancelOnClickOutside(true)
                            .setCancelOnOtherWindowOpen(true).createPopup();
                    RelativePoint loc = new RelativePoint(userMenu, new Point(0, 0 + userMenu.getSize().height));
                    searchUserPop.show(loc);
                    selectUserPanel.textFocus();
                } else {
                    userMenu.editSelectedMenuItem(originalText);

                }
            }
        });
    }

    private class UserListPanel extends JBPanel {
        private JList<String> list;
        private DefaultListModel<String> listModel;

        public UserListPanel() {
            super(new BorderLayout());
            listModel = new DefaultListModel<>();
            list = new JBList<>(listModel);
            JBScrollPane jp = new JBScrollPane(list);
            add(jp, BorderLayout.CENTER);
        }

        public void add(List<String> eleList) {
            listModel.clear();
            for (String s : eleList) {
                listModel.addElement(s);
            }
            list.setSelectedIndex(0);

        }

        public String getSelectedItem() {
            return list.getSelectedValue();
        }

        public void moveDown() {
            int ix = list.getSelectedIndex();
            int max = listModel.getSize() - 1;
            int nextIndex = ix + 1;
            if (nextIndex > max) {
                nextIndex = 0;
            }
            list.setSelectedIndex(nextIndex);

        }

        public void moveUp() {
            int ix = list.getSelectedIndex();
            int max = listModel.getSize() - 1;
            int nextIndex = ix - 1;
            if (nextIndex < 0) {
                nextIndex = max;
            }
            list.setSelectedIndex(nextIndex);

        }

    }

    private class SelectUserPanel extends JBPanel {

        private JTextArea textArea;
        private JLabel label;

        private Timer timer;
        private List<String> suggestions = new ArrayList<>();
        private UserListPanel listPanel;
        private JBPopup listPop;

        public SelectUserPanel() {
            super(new BorderLayout(3, 3));

            setBorder(JBUI.Borders.empty(3, 3, 3, 3));
            textArea = new JTextArea(3, 20);
            String opTip = "请选择一个或多个用户,间隔符为|或换行，Ctrl+Enter完成选择,ESC退出";
            if (SystemInfoRt.isMac) {
                opTip = "请选择一个或多个用户,间隔符为|或换行，Command+Enter完成选择,ESC退出";
            }
            label = new JLabel(opTip);
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
                    String textValue = textArea.getText();
                    String[] parts = textValue.split("\\n|\\|");

                    userMenu.editSelectedMenuItem(Joiner.on(",").join(parts));
                    if (searchUserPop != null && searchUserPop.isVisible()) {
                        searchUserPop.cancel();
                    }
                }
            };
            KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
            String actionKey = "ctrlEnter";
            if (SystemInfoRt.isMac) {
                actionKey = "commandEnter";
            }
            textArea.getInputMap(JComponent.WHEN_FOCUSED).put(keyStroke, actionKey);
            textArea.getActionMap().put(actionKey, enterAction);


            textArea.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    boolean enable = listPop != null && listPop.isVisible();
                    if (!enable) {
                        return;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        String val = listPanel.getSelectedItem();
                        String newValue = convertTextValue(val);
                        textArea.setText(newValue);
                        closePop();
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        listPanel.moveUp();
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        listPanel.moveDown();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });
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
                    if (timer != null) {
                        timer.stop();
                    }

                    String text = StringUtils.lastSplitText(textArea.getText());
                    if (StringUtil.isNotEmpty(text)) {

                        timer = new Timer(200, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                expandUserList(text);
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                }


            });

        }

        private void expandUserList(String text) {
            List<String> its = new ArrayList<>();
            for (String s : suggestions) {
                boolean b = s.startsWith(text);
                if (b) {
                    its.add(s);
                }
            }
            if (its.size() > 0) {
                listPanel.add(its);
                int x = textArea.getCaret().getMagicCaretPosition().x;
                int y = textArea.getCaret().getMagicCaretPosition().y;
                FontMetrics fontMetrics = textArea.getFontMetrics(textArea.getFont());
                int tw = fontMetrics.stringWidth(text);
                x = x - tw;
                y = y + textArea.getFont().getSize();

                listPop = JBPopupFactory.getInstance().createComponentPopupBuilder(listPanel, listPanel)
                        .setRequestFocus(false).setFocusable(true).setResizable(false).setMovable(false)
                        .setModalContext(false).setShowShadow(true).setShowBorder(true)
                        .setCancelKeyEnabled(true).setCancelOnClickOutside(true)
                        .setCancelOnOtherWindowOpen(true).createPopup();

                RelativePoint loc = new RelativePoint(textArea, new Point(x, y));
                listPop.show(loc);

//                textArea.requestFocus();
            } else {
                closePop();
            }
        }

        private String convertTextValue(String val) {
            String textValue = textArea.getText();
            int aIx = textValue.lastIndexOf('\n');
            int bIx = textValue.lastIndexOf('|');
            int lastIndex = Math.max(aIx, bIx);
            String newValue = val;
            if (lastIndex != -1) {
                char splitChar = '\n';
                if (bIx == lastIndex) {
                    splitChar = '|';
                }
                newValue = textValue.substring(0, lastIndex) + splitChar + val;
            }
            return newValue;
        }

        private void closePop() {
            if (listPop != null && listPop.isVisible()) {
                listPop.cancel();

            }
        }

        public void textFocus() {
            textArea.requestFocus();
        }
    }

}
