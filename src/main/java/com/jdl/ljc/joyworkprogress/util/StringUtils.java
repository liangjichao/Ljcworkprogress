package com.jdl.ljc.joyworkprogress.util;

import com.intellij.openapi.util.text.StringUtil;

public class StringUtils {
    public static String lastSplitText(String input) {
        if (StringUtil.isEmpty(input)) {
            return "";
        }
        String[] parts = input.split("\\n|\\|");
        return parts[parts.length-1];
    }
}
