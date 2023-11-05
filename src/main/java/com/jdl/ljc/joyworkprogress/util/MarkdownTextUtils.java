package com.jdl.ljc.joyworkprogress.util;

import com.intellij.util.ui.ImageUtil;
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
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class MarkdownTextUtils {
    public static String createLink(String title,String url){
        return String.format("[%s](%s)",title,url);
    }
    public static String createImg(String url,String width,String height){
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(width)) {
            sb.append(" " + width);
        }
        if (StringUtils.isNotBlank(height)) {
            sb.append("x" + height);
        }
        return String.format("![%s](%s%s)", "", url,sb.toString());
    }

    public static String imageToBase64(Image image,String formatName) {
        BufferedImage bufferedImage = ImageUtil.toBufferedImage(image);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, formatName, outputStream);
        } catch (IOException e) {
            return null;
        }
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeBase64String(imageBytes);
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
     *
     * ![Resizable](path/to/image.jpg 20x20)
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
        imgBaseUrl = "https://github.githubassets.com/images/icons/emoji/";
        options.set(EmojiExtension.ROOT_IMAGE_PATH, imgBaseUrl);

        Parser parser=Parser.builder(options).build();
        HtmlRenderer renderer=HtmlRenderer.builder(options).build();
        Node document = parser.parse(content);
        return renderer.render(document);
    }
}
