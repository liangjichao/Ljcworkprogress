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

    /**
     * 样例：
     * <pre>
     * | day         | time  |   spent |
     * |:------------|:-----:|--------:|
     * | nov. 2. tue | 10:00 |  4h 40m |
     * | nov. 3. thu | 11:00 |      4h |
     * | nov. 7. mon | 10:20 |  4h 20m |
     * | total:             ||     13h |
     *
     * ![Resizable Image](path/to/image.jpg){width=500 height=300}
     *
     * This is a ^superscript^ text
     *
     * This is a \"quoted\" text with 'apostrophes' and three dots...
     *
     * This is some ~~inserted~~ text.
     *
     * This is an example with emoji :smile: :heart:
     * </pre>
     * @param content
     * @return
     */
    public static String convertHTML(String content) {
        MutableDataSet options = new MutableDataSet();

        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()
                , ResizableImageExtension.create(), SuperscriptExtension.create()
        , TypographicExtension.create(), AnchorLinkExtension.create()
        , InsExtension.create(), EmojiExtension.create()));
        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");
        options.set(HtmlRenderer.GENERATE_HEADER_ID, true);
        String imgBaseUrl = String.format("http://%s%s", AppUtils.getDomain(),"/img/");
        options.set(EmojiExtension.ROOT_IMAGE_PATH, imgBaseUrl);

        Parser parser=Parser.builder(options).build();
        HtmlRenderer renderer=HtmlRenderer.builder(options).build();
        Node document = parser.parse(content);
        return renderer.render(document);
    }
}
