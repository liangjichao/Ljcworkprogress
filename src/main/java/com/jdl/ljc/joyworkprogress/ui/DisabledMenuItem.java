package com.jdl.ljc.joyworkprogress.ui;

import javax.swing.*;
import java.awt.*;

public class DisabledMenuItem extends JMenuItem {
    public DisabledMenuItem(String text) {
        super(text);
        setEnabled(false);
        setFont(new Font(getFont().getFontName(), Font.BOLD, getFont().getSize()+2));
    }
}
