package com.jdl.ljc.joyworkprogress.ui.editor;

import com.intellij.openapi.components.Service;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 * @author liangjichao
 * @date 2023/11/3 9:21 AM
 */
@Service(Service.Level.APP)
public final class WpsEditorPanel {
    private RSyntaxTextArea editorArea;
    private RTextScrollPane editorScrollPane;

    public WpsEditorPanel() {
        editorArea = new RSyntaxTextArea();
        editorArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
        editorScrollPane = new RTextScrollPane(editorArea);
    }


    public RTextScrollPane getScrollPane() {
        return editorScrollPane;
    }

    public RSyntaxTextArea getEditorArea() {
        return editorArea;
    }

}
