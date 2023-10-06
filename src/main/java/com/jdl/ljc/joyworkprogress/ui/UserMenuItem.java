package com.jdl.ljc.joyworkprogress.ui;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.popup.AbstractPopup;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

                    SearchUserDialog dialog = new SearchUserDialog();
                    Point location = userMenu.getLocationOnScreen();
                    int dialogHeight = dialog.getSize().height;
                    int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
                    int visibleHeight = screenHeight - location.y - userMenu.getHeight();
                    if (dialogHeight > visibleHeight) {
                        dialog.setLocation(location.x,location.y+userMenu.getHeight()-dialogHeight);
                    }else{
                        dialog.setLocation(location.x,location.y+userMenu.getHeight());
                    }
                    dialog.show();

                }else{
                    userMenu.editSelectedMenuItem(originalText);
                }
            }
        });
    }

    private class SearchUserDialog extends DialogWrapper{

        protected SearchUserDialog() {
            super(false);
            setUndecorated(true);//不显示标题栏和关闭按钮
            setModal(false);

            init();

        }

        @Override
        protected @Nullable JComponent createCenterPanel() {
            JPanel rootPanel = new JPanel(new BorderLayout(3,3));
            JTextArea textArea = new JTextArea(3,20);
            rootPanel.add(textArea, BorderLayout.CENTER);
            JLabel label = new JLabel("请选择一个或多个用户,间隔符为|或换行，Ctrl+Enter完成选择,ESC退出");
            rootPanel.add(label, BorderLayout.SOUTH);
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
