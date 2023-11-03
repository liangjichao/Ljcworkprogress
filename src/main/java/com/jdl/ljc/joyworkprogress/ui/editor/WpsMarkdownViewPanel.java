package com.jdl.ljc.joyworkprogress.ui.editor;

import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.util.text.HtmlBuilder;
import com.intellij.openapi.util.text.HtmlChunk;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vcs.ui.FontUtil;
import com.intellij.ui.BrowserHyperlinkListener;
import com.intellij.util.ui.HTMLEditorKitBuilder;
import com.intellij.util.ui.UIUtil;
import com.jdl.ljc.joyworkprogress.ui.editor.WpsViewPanel;
import com.jdl.ljc.joyworkprogress.util.FileUtils;
import com.jdl.ljc.joyworkprogress.util.StringUtils;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Position;
import java.awt.*;
import java.io.IOException;
import java.io.StringWriter;

public class WpsMarkdownViewPanel extends JEditorPane implements HyperlinkListener, WpsViewPanel {

    public WpsMarkdownViewPanel(String content) {
        super(UIUtil.HTML_MIME, "");
        setEditable(false);
        setOpaque(false);
        putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        addHyperlinkListener(this);
        setEditorKit(new HTMLEditorKitBuilder().withWordWrapViewFactory().build());
        if (org.apache.commons.lang3.StringUtils.isNotBlank(content)) {
            setText(content);
        }
        setFont(FontUtil.getCommitMessageFont());
    }
    @Override
    public Color getBackground() {
        return EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground();
    }

    @Override
    public void setText(String t) {
        String html = String.format("""
                <div class="markdown-body">%s</div>
                """,StringUtils.convertHTML(t));
        String css = String.format("<style>%s</style>",FileUtils.getResource("/html/markdown-view.css"));

        String all = new HtmlBuilder()
                .append(HtmlChunk.raw(css).wrapWith("head"))
                .append(HtmlChunk.raw(html).wrapWith(HtmlChunk.body()))
                .wrapWith(HtmlChunk.html()).toString();
        super.setText(all);
    }


    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        BrowserHyperlinkListener.INSTANCE.hyperlinkUpdate(e);
    }

    @Override
    public String getSelectedText() {
        Document doc = getDocument();
        int start = getSelectionStart();
        int end = getSelectionEnd();

        try {
            Position p0 = doc.createPosition(start);
            Position p1 = doc.createPosition(end);
            StringWriter sw = new StringWriter(p1.getOffset() - p0.getOffset());
            getEditorKit().write(sw, doc, p0.getOffset(), p1.getOffset() - p0.getOffset());

            return StringUtil.removeHtmlTags(sw.toString());
        }
        catch (BadLocationException | IOException ignored) {
        }
        return super.getSelectedText();
    }

    @Override
    public void updateContent(String content, int offset) {
        setText(content);
    }
}
