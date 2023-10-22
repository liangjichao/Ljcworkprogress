package com.jdl.ljc.joyworkprogress.ui.panel;

import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.ui.jcef.JCEFHtmlPanel;
import com.jdl.ljc.joyworkprogress.util.FileUtils;
import com.jdl.ljc.joyworkprogress.util.StringUtils;
import groovy.json.StringEscapeUtils;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;

import java.awt.*;

/**
 * @author liangjichao
 * @date 2023/9/7 3:57 PM
 */
public class ProgressHtmlPanel extends JCEFHtmlPanel {
    public ProgressHtmlPanel(String content) {
        super(true,null,null);
        String html = initHTML(content);
        setHtml(html);
        final EditorColorsScheme globalScheme = EditorColorsManager.getInstance().getGlobalScheme();
        setPageBackgroundColor(colorToHex(globalScheme.getDefaultBackground()));


    }

    public void changeHtml(String content) {
        String html = StringUtils.convertHTML(content);
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
        String innerHTML = StringUtils.convertHTML(content);
        return FileUtils.getResource("/html/index.html").replace("[!editor-content]", innerHTML);
    }



    public String colorToHex(Color color) {
        int r = color.getRed();;
        int g = color.getGreen();
        int b = color.getBlue();
        return String.format("#%02x%02x%02x", r, g, b);
    }
}
