package com.jdl.ljc.joyworkprogress.ui;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.popup.AbstractPopup;
import com.intellij.util.ui.JBUI;
import com.jdl.ljc.joyworkprogress.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
                    JPanel rootPanel = new JPanel(new BorderLayout(3,3));
                    JTextArea textArea = new JTextArea(3,20);
                    rootPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
                    JLabel label = new JLabel("请选择一个或多个用户,间隔符为|或换行，Ctrl+Enter完成选择,ESC退出");
                    rootPanel.add(label, BorderLayout.SOUTH);
                    JBPopup ourPopup = JBPopupFactory.getInstance().createComponentPopupBuilder(rootPanel, rootPanel).setRequestFocus(true).setFocusable(true).setResizable(false).setMovable(false).setModalContext(false).setShowShadow(true).setShowBorder(false).setCancelKeyEnabled(true).setCancelOnClickOutside(true).setCancelOnOtherWindowOpen(true).createPopup();
                    RelativePoint loc = new RelativePoint(userMenu, new Point(0, 0+userMenu.getSize().height));
                    ourPopup.show(loc);
//                    showSearchUserDialog();

                }else{
                    userMenu.editSelectedMenuItem(originalText);
                }
            }
        });
    }

    private void showSearchUserDialog() {
        SearchUserDialog dialog = new SearchUserDialog();
        Point newLocation = getPopLocation(dialog.getSize().height);
        dialog.setLocation(newLocation);
        dialog.show();
    }

    @NotNull
    private Point getPopLocation(int dialogHeight) {
        Point location = userMenu.getLocationOnScreen();
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        int visibleHeight = screenHeight - location.y - userMenu.getHeight();
        Point newLocation=null;
        if (dialogHeight > visibleHeight) {
            newLocation = new Point(location.x, location.y + userMenu.getHeight() - dialogHeight);
//            dialog.setLocation(location.x,location.y+userMenu.getHeight()-dialogHeight);
        }else{
            newLocation = new Point(location.x, location.y + userMenu.getHeight());
//            dialog.setLocation(location.x,location.y+userMenu.getHeight());
        }
        return newLocation;
    }

    private class SearchUserDialog extends DialogWrapper{

        private JPopupMenu popupMenu;
        private List<String> suggestions = new ArrayList<>();

        private Timer timer;

        protected SearchUserDialog() {
            super(true);
            setUndecorated(true);//不显示标题栏和关闭按钮
            setModal(false);

            popupMenu = new JPopupMenu();
            suggestions.add("apple");
            suggestions.add("banana");
            suggestions.add("cherry");

            init();

            Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
                @Override
                public void eventDispatched(AWTEvent event) {
                    if (event instanceof MouseEvent) {
                        if (event.getID() == MouseEvent.MOUSE_CLICKED) {
                            boolean isChild =SwingUtilities.isDescendingFrom(((MouseEvent) event).getComponent(), getRootPane());
                            if (!isChild) {
                                doCancelAction();
                                Toolkit.getDefaultToolkit().removeAWTEventListener(this);
                            }
                        }
                    }
                }
            },AWTEvent.MOUSE_EVENT_MASK);
        }

        @Override
        protected @Nullable JComponent createCenterPanel() {
            JPanel rootPanel = new JPanel(new BorderLayout(3,3));
            JTextArea textArea = new JTextArea(3,20);
            rootPanel.add(textArea, BorderLayout.CENTER);
            JLabel label = new JLabel("请选择一个或多个用户,间隔符为|或换行，Ctrl+Enter完成选择,ESC退出");
            rootPanel.add(label, BorderLayout.SOUTH);

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
                }

                @Override
                public void changedUpdate(DocumentEvent e) {

                }

                private void updateSuggestions() {

                    String text = StringUtils.lastSplitText( textArea.getText());
                    if (StringUtil.isNotEmpty(text)) {
//                        if (timer != null) {
//                            timer.stop();
//                        }
                        timer = new Timer(200, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                popupMenu.removeAll();
                                for (String suggestion : suggestions) {
                                    if (suggestion.startsWith(text)) {
                                        JMenuItem menuItem = new JMenuItem(suggestion);
                                        menuItem.addActionListener(popMenuListener);
                                        popupMenu.add(menuItem);
                                    }
                                }
                                if (popupMenu.getComponentCount() > 0) {
                                    int x = textArea.getCaret().getMagicCaretPosition().x;
                                    FontMetrics fontMetrics = textArea.getFontMetrics(textArea.getFont());
                                    int tw = fontMetrics.stringWidth(text);
                                    x=x-tw;
                                    popupMenu.show(textArea, x, textArea.getCaret().getMagicCaretPosition().y + textArea.getFont().getSize());
                                    textArea.requestFocus();

                                }else{
                                    if (popupMenu.isVisible()) {
                                        popupMenu.setVisible(false);
                                    }
                                }
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                }
            });


            return rootPanel;
        }


        @Override
        protected Action @NotNull [] createActions() {
            // 创建对话框的动作按钮
            return new Action[]{

            };
        }

        @Override
        protected @Nullable Border createContentPaneBorder() {
            Border border = JBUI.Borders.compound(JBUI.Borders.customLine(JBUI.CurrentTheme.Component.FOCUSED_BORDER_COLOR, 1, 1, 1, 1), JBUI.Borders.empty(3, 3, 3, 3));
            return border;
        }
    }

    private class SearchUserPopopMenu extends AbstractPopup {

    }


}
