package com.jdl.ljc.joyworkprogress.ui.panel;

import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.ui.jcef.JCEFHtmlPanel;
import com.jdl.ljc.joyworkprogress.util.FileUtils;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import groovy.json.StringEscapeUtils;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * @author liangjichao
 * @date 2023/9/7 3:57 PM
 */
public class ProgressHtmlPanel extends JCEFHtmlPanel {
    public ProgressHtmlPanel(String content) {
        super(null);
        String html = initHTML(content);
        setHtml(html);
        final EditorColorsScheme globalScheme = EditorColorsManager.getInstance().getGlobalScheme();
        setPageBackgroundColor(colorToHex(globalScheme.getDefaultBackground()));


    }

    public void changeHtml(String content) {
        String html = convertHTML(content);
        CefBrowser cefBrowser = getCefBrowser();
        CefFrame frame = cefBrowser.getMainFrame();
        html = StringEscapeUtils.escapeJavaScript(html);
        frame.executeJavaScript("changeContent('"+html+"');", frame.getURL(), 0);


    }

    /**
     * <pre>
     *     https://github.com/vsch/flexmark-java
     * </pre>
     *
     * @param content
     * @return
     */
    private static String initHTML(String content) {
        String innerHTML = convertHTML(content);
        return FileUtils.getResource("/html/index.html").replace("[!editor-content]", innerHTML);
    }

    @NotNull
    private static String convertHTML(String content) {
        MutableDataSet options = new MutableDataSet();
        Parser parser=Parser.builder(options).build();
        HtmlRenderer renderer=HtmlRenderer.builder(options).build();
        Node document = parser.parse(content);
        return renderer.render(document);
    }

    public String colorToHex(Color color) {
        int r = color.getRed();;
        int g = color.getGreen();
        int b = color.getBlue();
        return String.format("#%02x%02x%02x", r, g, b);
    }
}
