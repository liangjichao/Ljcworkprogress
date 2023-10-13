package com.jdl.ljc.joyworkprogress.ui;

import com.intellij.openapi.util.SystemInfoRt;
import com.intellij.ui.components.JBMenu;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.plaf.basic.BasicMenuUI;
import java.awt.*;

public class UserMenu extends JBMenu {
    private Icon menuIcon;

    private Runnable selectedRun;

    private boolean showMenuIcon;

    public UserMenu(String text, Icon icon) {
        super();
        setText(text);
        this.menuIcon = icon;
        showMenuIcon = true;

        setUI(new BasicMenuUI(){
            @Override
            protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
                super.paintBackground(g, menuItem, bgColor);
                if (SystemInfoRt.isMac) {
                    g.setColor(JBUI.CurrentTheme.CustomFrameDecorations.paneBackground());
                    g.fillRect(0, 0, menuItem.getWidth(), menuItem.getHeight());
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (menuIcon != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            int w = menuIcon.getIconWidth();
            int h = menuIcon.getIconHeight();
            int x = (getWidth() - w);
            int y = (getHeight() - h) / 2;
            if (showMenuIcon) {
                menuIcon.paintIcon(this, g2, x, y);
            }else{
                menuIcon.paintIcon(this, g2, -w, -h);
            }
            g2.dispose();

        }
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension originalSize = super.getPreferredSize();
        int newwidth = originalSize.width;
        if (menuIcon != null) {
            if (showMenuIcon) {
                newwidth = originalSize.width + menuIcon.getIconWidth();
            }
        }
        return new Dimension(newwidth, originalSize.height);
    }

    public void editSelectedMenuItem(String text) {
        System.out.println("我收到选择的文本：" + text);
        setText("User:" + text);
        showMenuIcon=false;
        if (selectedRun != null) {
            selectedRun.run();
        }
    }

    public void setSelectedRun(Runnable selectedRun) {
        this.selectedRun = selectedRun;
    }

    public void setShowMenuIcon(boolean visiable) {
        this.showMenuIcon = visiable;
    }

}
