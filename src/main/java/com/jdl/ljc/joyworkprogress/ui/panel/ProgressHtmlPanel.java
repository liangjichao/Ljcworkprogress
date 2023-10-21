package com.jdl.ljc.joyworkprogress.ui.panel;

import com.intellij.ui.jcef.JCEFHtmlPanel;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

/**
 * @author liangjichao
 * @date 2023/9/7 3:57 PM
 */
public class ProgressHtmlPanel extends JCEFHtmlPanel {
    public ProgressHtmlPanel(String content) {
        super(null);
        String html = convertHTML(content);
        setHtml(html);
    }

    /**
     * <pre>
     *     https://github.com/vsch/flexmark-java
     * </pre>
     *
     * @param content
     * @return
     */
    private static String convertHTML(String content) {
        MutableDataSet options = new MutableDataSet();
        Parser parser=Parser.builder(options).build();
        HtmlRenderer renderer=HtmlRenderer.builder(options).build();
        Node document = parser.parse(content);
        return renderer.render(document);
    }
}
