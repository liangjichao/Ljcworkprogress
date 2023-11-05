package com.jdl.ljc.joyworkprogress.util;

import com.intellij.openapi.util.text.StringUtil;
import com.vladsch.flexmark.ext.anchorlink.AnchorLinkExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.ins.InsExtension;
import com.vladsch.flexmark.ext.resizable.image.ResizableImageExtension;
import com.vladsch.flexmark.ext.superscript.SuperscriptExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.typographic.TypographicExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.util.Arrays;

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
