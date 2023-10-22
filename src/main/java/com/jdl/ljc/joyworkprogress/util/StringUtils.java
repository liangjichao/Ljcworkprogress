package com.jdl.ljc.joyworkprogress.util;

import com.intellij.openapi.util.text.StringUtil;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

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

    public static String convertHTML(String content) {
        MutableDataSet options = new MutableDataSet();
        Parser parser=Parser.builder(options).build();
        HtmlRenderer renderer=HtmlRenderer.builder(options).build();
        Node document = parser.parse(content);
        return renderer.render(document);
    }
}
