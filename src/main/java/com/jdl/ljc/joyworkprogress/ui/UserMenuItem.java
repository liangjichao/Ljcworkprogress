package com.jdl.ljc.joyworkprogress.ui;

import javax.swing.*;
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
                    System.out.println("弹出搜索框");
                }else{
                    userMenu.editSelectedMenuItem(originalText);
                }
            }
        });
    }

}
