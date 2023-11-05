package com.jdl.ljc.joyworkprogress.util;

import com.intellij.openapi.util.text.StringUtil;

public class StringUtils {
    public static String lastSplitText(String input) {
        if (StringUtil.isEmpty(input)) {
            return "";
        }
        int lastIndex = Math.max(input.lastIndexOf('\n'), input.lastIndexOf('|'));
        if (lastIndex != -1) {
            return input.substring(lastIndex+1, input.length());
        }
        return input.substring(0, input.length());
    }
}
