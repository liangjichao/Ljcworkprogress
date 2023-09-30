package com.jdl.ljc.joyworkprogress.ui.panel;

import com.intellij.ui.jcef.JCEFHtmlPanel;
import com.jdl.ljc.joyworkprogress.util.FileUtils;
import com.jdl.ljc.joyworkprogress.vo.EditorContent;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefLoadHandlerAdapter;
import org.cef.handler.CefMessageRouterHandlerAdapter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
        return FileUtils.getResource("/html/index.html").replace("[!editor-content]",content);
    }

    public String getEditorContent() {
//        CefBrowser browser = getCefBrowser();
//        browser.executeJavaScript("getEditorContent();", null, 0);
        return editorContent.getContent();
    }

    @Override
    public void dispose() {
        super.dispose();
        getCefBrowser().getClient().removeRequestHandler();

    }



}
