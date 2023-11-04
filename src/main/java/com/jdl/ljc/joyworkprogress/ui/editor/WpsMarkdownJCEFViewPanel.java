package com.jdl.ljc.joyworkprogress.ui.editor;

import com.intellij.openapi.project.Project;
import com.jdl.ljc.joyworkprogress.util.StringUtils;
import org.cef.browser.CefBrowser;
import org.cef.callback.CefStringVisitor;
import org.cef.handler.CefLoadHandlerAdapter;
import org.intellij.plugins.markdown.ui.preview.jcef.MarkdownJCEFHtmlPanel;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author liangjichao
 * @date 2023/10/23 11:31 AM
 */
public class WpsMarkdownJCEFViewPanel extends JPanel implements WpsViewPanel {
    private MarkdownJCEFHtmlPanel htmlPanel;

    public WpsMarkdownJCEFViewPanel(Project project, String content) {
        super(new BorderLayout());
        htmlPanel = new MarkdownJCEFHtmlPanel(project, null);
        add(htmlPanel.getComponent(), BorderLayout.CENTER);
        htmlPanel.setHtml(getConvertHTML(content), 0);

    }

    @Override
    public String getConvertHTML(String content) {
        return "<html lang=\"zh\"><head><meta charset=\"UTF-8\"><title>Wps Markdown Preview</title></head>" + StringUtils.convertHTML(content) + "</html>";
    }

    @Override
    public String getViewUrl() {
        return htmlPanel.getCefBrowser().getURL();
    }

    @Override
    public void updateContent(String content, int offset) {
        htmlPanel.setHtml(getConvertHTML(content), offset);
    }



    public MarkdownJCEFHtmlPanel getComponent() {
        return htmlPanel;
    }
}
