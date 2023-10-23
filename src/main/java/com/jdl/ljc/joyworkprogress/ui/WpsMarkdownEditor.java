package com.jdl.ljc.joyworkprogress.ui;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.OnePixelSplitter;
import com.jdl.ljc.joyworkprogress.ui.panel.WpsMarkdownJCEFViewPanel;
import org.intellij.plugins.markdown.ui.preview.MarkdownHtmlPanelEx;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.util.function.Supplier;

/**
 * @author liangjichao
 * @date 2023/10/23 5:00 PM
 */
public class WpsMarkdownEditor {
    private JComponent myComponent;

    private Editor editor;
    private WpsMarkdownJCEFViewPanel myPanel;
    public WpsMarkdownEditor(Project project,String content) {
        myPanel = new WpsMarkdownJCEFViewPanel(project, content);
        Document document = EditorFactory.getInstance().createDocument(content);
        editor = EditorFactory.getInstance().createEditor(document, project, FileTypeManager.getInstance().getFileTypeByExtension("md"), false);
        editor.getSettings().setLineNumbersShown(true);


        EditorImpl myEditor = (EditorImpl)editor;
        myEditor.getScrollPane().addMouseWheelListener(new PreciseVerticalScrollHelper(myEditor,
                () -> (myPanel.getComponent() instanceof MarkdownHtmlPanelEx)? (MarkdownHtmlPanelEx)myPanel.getComponent() : null));

        editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                int s = myEditor.getScrollingModel().getVerticalScrollOffset();
                myPanel.updateContent(event.getDocument().getText(),s);
            }
        });

        JBSplitter splitter = new OnePixelSplitter(false);
        splitter.setFirstComponent(editor.getComponent());
        splitter.setSecondComponent(myPanel);
        myComponent = splitter;
    }

    public String getText() {
        return editor.getDocument().getText();
    }

    public JComponent getComponent() {
        return myComponent;
    }

    private static class PreciseVerticalScrollHelper extends MouseAdapter {
        private final @NotNull EditorImpl editor;
        private final @NotNull Supplier<MarkdownHtmlPanelEx> htmlPanelSupplier;

        private PreciseVerticalScrollHelper(@NotNull EditorImpl editor, @NotNull Supplier<MarkdownHtmlPanelEx> htmlPanelSupplier) {
            this.editor = editor;
            this.htmlPanelSupplier = htmlPanelSupplier;
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent event) {
            final var actualPanel = htmlPanelSupplier.get();
            if (actualPanel == null) {
                return;
            }
            if (event.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                final var multiplier = Registry.intValue("ide.browser.jcef.osr.wheelRotation.factor", 1);
                final var amount = event.getScrollAmount() * event.getWheelRotation() * multiplier;
                actualPanel.scrollBy(0, amount);
            }
        }


    }
}
