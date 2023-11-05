package com.jdl.ljc.joyworkprogress.ui.editor;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.text.HtmlBuilder;
import com.intellij.openapi.util.text.HtmlChunk;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vcs.ui.FontUtil;
import com.intellij.ui.BrowserHyperlinkListener;
import com.intellij.util.ui.HTMLEditorKitBuilder;
import com.intellij.util.ui.UIUtil;
import com.jdl.ljc.joyworkprogress.ui.editor.preview.CommonResourceProvider;
import com.jdl.ljc.joyworkprogress.ui.editor.preview.Resource;
import com.jdl.ljc.joyworkprogress.ui.editor.preview.ResourceProvider;
import com.jdl.ljc.joyworkprogress.ui.editor.preview.WpsStaticServer;
import com.jdl.ljc.joyworkprogress.util.FileUtils;
import com.jdl.ljc.joyworkprogress.util.MarkdownTextUtils;
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
import java.util.ArrayList;
import java.util.List;

@Service(Service.Level.APP)
public final class WpsMarkdownViewPanel extends JEditorPane implements HyperlinkListener, WpsViewPanel,Disposable {
    private final String pageBaseName = String.format("markdown-preview-index-%s.html", hashCode());
    private ResourceProvider resourceProvider = new MyAggregatingResourceProvider();

    private WpsEditorPanel editorPanel;

    public WpsMarkdownViewPanel() {
        super(UIUtil.HTML_MIME, "");
        this.editorPanel = ApplicationManager.getApplication().getService(WpsEditorPanel.class);
        Disposer.register(this, WpsStaticServer.getInstance().registerResourceProvider(resourceProvider));
        setEditable(false);
        setOpaque(false);
        putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        addHyperlinkListener(this);
        setEditorKit(new HTMLEditorKitBuilder().withWordWrapViewFactory().build());
        setFont(FontUtil.getCommitMessageFont());
    }
    @Override
    public Color getBackground() {
        return EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground();
    }

    @Override
    public void setText(String t) {
        String all = getConvertHTML(t);
        super.setText(all);
    }

    @Override
    public String getConvertHTML(String t) {
        String html = String.format("""
                <div class="markdown-body">%s</div>
                """, MarkdownTextUtils.convertHTML(t));
        String css = String.format("<meta charset=\"UTF-8\"><style>%s</style>",FileUtils.getResource("/html/markdown-view.css"));

        return new HtmlBuilder()
                .append(HtmlChunk.raw(css).wrapWith("head"))
                .append(HtmlChunk.raw(html).wrapWith(HtmlChunk.body()))
                .wrapWith(HtmlChunk.html().attr("lang","zh")).toString();
    }

    @Override
    public String getViewUrl() {
        return  WpsStaticServer.getStaticUrl(resourceProvider, pageBaseName);
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

    @Override
    public void dispose() {

    }

    private class MyAggregatingResourceProvider implements ResourceProvider {
        private static List<String> internalResources = new ArrayList<>();

        public Boolean canProvide(String resourceName) {
            return internalResources.contains(resourceName) ||
                    resourceName.equals(pageBaseName);
        }

        public Resource loadResource(String resourceName) {
            if (resourceName.equals(pageBaseName)) {
                return new Resource(buildIndexContent().getBytes(), "text/html");
            } else if (internalResources.contains(resourceName)) {
                return CommonResourceProvider.loadInternalResource(this.getClass(), resourceName, null);
            } else {
                return null;
            }

        }

        private String buildIndexContent() {
            String text = editorPanel.getEditorArea().getText();
            return getConvertHTML(text);
        }
    }
}
