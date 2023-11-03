package com.jdl.ljc.joyworkprogress.ui.editor;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 * @author liangjichao
 * @date 2023/11/3 9:21 AM
 */
public class WpsEditorPanel {
    private RSyntaxTextArea editorArea;
    private RTextScrollPane editorScrollPane;
    public WpsEditorPanel(String content) {
        editorArea = new RSyntaxTextArea(content);
        editorArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
        editorScrollPane = new RTextScrollPane(editorArea);

    }
}
