package com.ljc.workprogress.ui.calendar;

import com.michaelbaranov.microba.calendar.CalendarResources;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

/**
 * @author liangjichao
 * @date 2023/10/19 8:23 PM
 */
public class WpsCalendarResources implements CalendarResources {
    private static final String RESOURCE_FILE = "DefaultCalendarResources.properties";
    private static final String DEFAULT_LANGUAGE = "en";
    private Properties strings = new Properties();

    public WpsCalendarResources() {
        try {
            this.strings.load(WpsCalendarResources.class.getResourceAsStream("/DefaultCalendarResources.properties"));
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public String getResource(String key, Locale locale) {
        String language = locale.getLanguage();
        String word = (String)this.strings.get(language + "." + key);
        if (word == null) {
            word = (String)this.strings.get("zh." + key);
        }

        return word;
    }
}
