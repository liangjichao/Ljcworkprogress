package com.ljc.workprogress.ui.editor;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.text.HtmlBuilder;
import com.intellij.openapi.util.text.HtmlChunk;
import com.ljc.workprogress.ui.editor.preview.CommonResourceProvider;
import com.ljc.workprogress.ui.editor.preview.Resource;
import com.ljc.workprogress.ui.editor.preview.ResourceProvider;
import com.ljc.workprogress.ui.editor.preview.WpsStaticServer;
import com.ljc.workprogress.util.FileUtils;
import com.ljc.workprogress.util.MarkdownTextUtils;

import java.util.ArrayList;
import java.util.List;

@Service(Service.Level.APP)
public final class WpsMarkdownBrowserView implements WpsViewPanel,Disposable {
    private final String pageBaseName = String.format("markdown-preview-index-%s.html", hashCode());
    private ResourceProvider resourceProvider = new MyAggregatingResourceProvider();

    private WpsEditorPanel editorPanel;

    public WpsMarkdownBrowserView() {

        this.editorPanel = ApplicationManager.getApplication().getService(WpsEditorPanel.class);
        Disposer.register(this, WpsStaticServer.getInstance().registerResourceProvider(resourceProvider));

    }


    @Override
    public void updateContent(String content, int offset) {

    }

    public String getConvertHTML(String t) {
        String html = String.format("<div class=\"markdown-body\">%s</div>", MarkdownTextUtils.convertHTML(t));
        String css = String.format("<meta charset=\"UTF-8\"><style>%s</style>", FileUtils.getResource("/html/markdown-view.css"));

        return new HtmlBuilder()
                .append(HtmlChunk.raw(css).wrapWith("head"))
                .append(HtmlChunk.raw(html).wrapWith(HtmlChunk.body()))
                .wrapWith(HtmlChunk.html().attr("lang", "zh")).toString();
    }

    @Override
    public String getViewUrl() {
        return WpsStaticServer.getStaticUrl(resourceProvider, pageBaseName);
    }


    @Override
    public void dispose() {

    }

    private class MyAggregatingResourceProvider implements ResourceProvider {
        private List<String> internalResources = new ArrayList<>();

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
