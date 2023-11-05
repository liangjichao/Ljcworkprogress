package com.jdl.ljc.joyworkprogress.ui.calendar;

import com.intellij.util.ui.JBUI;
import com.michaelbaranov.microba.calendar.DatePicker;

import java.text.SimpleDateFormat;

/**
 * @author liangjichao
 * @date 2023/10/19 9:03 PM
 */
public class WpsDatePicker extends DatePicker {
    public WpsDatePicker() {
        super(null);
        setUI(new WpsDatePickerUI());
        setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        setPreferredSize(JBUI.size(200,30));
        setResources(new WpsCalendarResources());
    }

    @Override
    public void updateUI() {
        super.updateUI();
    }


}
