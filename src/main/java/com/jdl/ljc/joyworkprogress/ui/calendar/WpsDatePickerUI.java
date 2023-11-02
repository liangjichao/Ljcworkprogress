package com.jdl.ljc.joyworkprogress.ui.calendar;

import com.intellij.util.ui.JBUI;
import com.michaelbaranov.microba.calendar.ui.basic.BasicDatePickerUI;

/**
 * @author liangjichao
 * @date 2023/10/19 9:19 PM
 */
public class WpsDatePickerUI extends BasicDatePickerUI {
    @Override
    protected void installComponents() {
        super.installComponents();
        this.button.setPreferredSize(JBUI.size(30,30));
    }
}
