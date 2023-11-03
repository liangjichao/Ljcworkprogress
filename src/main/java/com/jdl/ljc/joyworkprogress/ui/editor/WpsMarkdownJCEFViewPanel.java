package com.jdl.ljc.joyworkprogress.ui.editor;

import com.intellij.openapi.project.Project;
import com.jdl.ljc.joyworkprogress.ui.editor.WpsViewPanel;
import com.jdl.ljc.joyworkprogress.util.StringUtils;
import org.intellij.plugins.markdown.ui.preview.jcef.MarkdownJCEFHtmlPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

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
    public void updateContent(String content, int offset) {
        htmlPanel.setHtml(getConvertHTML(content), offset);
    }



    public MarkdownJCEFHtmlPanel getComponent() {
        return htmlPanel;
    }
}
