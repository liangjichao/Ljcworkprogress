package com.jdl.ljc.joyworkprogress.util;

import com.jdl.ljc.joyworkprogress.config.WpsPluginSetting;
import com.jdl.ljc.joyworkprogress.domain.WpsState;

public class AppUtils {
    public static String getDomain() {
        WpsState state = WpsPluginSetting.getInstance().getState();
        if (state != null&&state.domain!=null) {
            return state.domain;
        }
        return "localhost";
    }
}
