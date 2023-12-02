package com.ljc.workprogress.util;

import com.ljc.workprogress.config.WpsPluginSetting;
import com.ljc.workprogress.domain.WpsState;

public class AppUtils {
    public static String getDomain() {
        WpsState state = WpsPluginSetting.getInstance().getState();
        if (state != null&&state.domain!=null) {
            return state.domain;
        }
        return "localhost";
    }
}
