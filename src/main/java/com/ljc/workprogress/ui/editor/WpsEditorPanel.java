package com.ljc.workprogress.ui.editor;

import com.intellij.openapi.components.Service;
import com.intellij.util.ui.JBUI;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.io.IOException;

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
        editorArea.setBorder(JBUI.Borders.empty(20));
        editorArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
        try {
            Theme theme = Theme.load(getClass().getResourceAsStream(
                    "/org/fife/ui/rsyntaxtextarea/themes/dark.xml"));
            theme.apply(editorArea);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        editorScrollPane = new RTextScrollPane(editorArea);
        editorScrollPane.setBorder(JBUI.Borders.empty());
    }


    public RTextScrollPane getScrollPane() {
        return editorScrollPane;
    }

    public RSyntaxTextArea getEditorArea() {
        return editorArea;
    }

}
