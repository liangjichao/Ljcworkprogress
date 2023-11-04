package com.jdl.ljc.joyworkprogress.util;

import com.jdl.ljc.joyworkprogress.config.WpsPluginSetting;

public class AppUtils {
    public static String getDomain() {
        if (WpsPluginSetting.getInstance().getState() != null) {
            return WpsPluginSetting.getInstance().getState().domain;
        }
        return "localhost";
    }
}
