package com.jdl.ljc.joyworkprogress.ui.panel;

import com.intellij.ui.jcef.JCEFHtmlPanel;
import com.jdl.ljc.joyworkprogress.domain.vo.EditorContent;
import com.jdl.ljc.joyworkprogress.util.FileUtils;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;

/**
 * @author liangjichao
 * @date 2023/9/7 3:57 PM
 */
public class ProgressHtmlPanel extends JCEFHtmlPanel {
    private final EditorContent editorContent = new EditorContent();
    public ProgressHtmlPanel(String content) {
        super(null);
        CefMessageRouter.CefMessageRouterConfig config = new CefMessageRouter.CefMessageRouterConfig("sendDataToJava","sendDataFailure");
        CefMessageRouter cmr = CefMessageRouter.create(config);
        cmr.addHandler(new CefMessageRouterHandlerAdapter() {
            @Override
            public boolean onQuery(CefBrowser browser, CefFrame frame, long queryId, String request, boolean persistent, CefQueryCallback callback) {
                editorContent.setContent(request);
                return true;
            }
        },true);

        CefBrowser cefBrowser = getCefBrowser();

        CefClient cefClient = cefBrowser.getClient();

        cefClient.addMessageRouter(cmr);

        setHtml(getIndexContent(content));

    }
    private String getIndexContent(String content) {
        String html = FileUtils.getResource("/html/index.html").replace("[!editor-content]", content);
        html=html.replace("<!--#style-->", FileUtils.getResource("/html/css/style.css"));
        html=html.replace("<!--#index-->", FileUtils.getResource("/html/js/index.js"));
        return html;
    }

    public String getEditorContent() {
        return editorContent.getContent();
    }

    @Override
    public void dispose() {
        super.dispose();
        getCefBrowser().getClient().removeRequestHandler();

    }



}
