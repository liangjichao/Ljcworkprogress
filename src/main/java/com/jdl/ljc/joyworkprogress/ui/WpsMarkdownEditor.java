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
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.jcef.JBCefApp;
import com.jdl.ljc.joyworkprogress.ui.panel.WpsMarkdownJCEFViewPanel;
import com.jdl.ljc.joyworkprogress.ui.panel.WpsMarkdownViewPanel;
import org.intellij.plugins.markdown.ui.preview.MarkdownHtmlPanelEx;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
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
    private JComponent viewEditor;
    public WpsMarkdownEditor(Project project,String content) {

        Document document = EditorFactory.getInstance().createDocument(content);
        editor = EditorFactory.getInstance().createEditor(document, project, FileTypeManager.getInstance().getFileTypeByExtension("md"), false);
        editor.getSettings().setLineNumbersShown(true);

        if (!JBCefApp.isSupported()) {
            WpsMarkdownJCEFViewPanel myPanel = new WpsMarkdownJCEFViewPanel(project, content);
            EditorImpl myEditor = (EditorImpl)editor;
            myEditor.getScrollPane().addMouseWheelListener(new PreciseVerticalScrollHelper(
                    () -> (myPanel.getComponent() instanceof MarkdownHtmlPanelEx)? myPanel.getComponent() : null));
            editor.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void documentChanged(@NotNull DocumentEvent event) {
                    int s = myEditor.getScrollingModel().getVerticalScrollOffset();
                    myPanel.updateContent(event.getDocument().getText(),s);
                }
            });
            viewEditor=myPanel;
        }else{
            WpsMarkdownViewPanel viewPanel = new WpsMarkdownViewPanel(content);
            JBScrollPane scrollPane = new JBScrollPane(viewPanel);

            EditorImpl myEditor = (EditorImpl)editor;
            myEditor.getScrollPane().addMouseWheelListener(new ViewScrollHelper(scrollPane));

            editor.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void documentChanged(@NotNull DocumentEvent event) {
                    viewPanel.updateContent(event.getDocument().getText(),0);
                }
            });
            viewEditor= scrollPane;
        }


        JBSplitter splitter = new OnePixelSplitter(false);
        splitter.setFirstComponent(editor.getComponent());
        splitter.setSecondComponent(viewEditor);

        myComponent = splitter;
    }

    public String getText() {
        return editor.getDocument().getText();
    }

    public JComponent getComponent() {
        return myComponent;
    }

    private static class PreciseVerticalScrollHelper extends MouseAdapter {
        private final @NotNull Supplier<MarkdownHtmlPanelEx> htmlPanelSupplier;

        private PreciseVerticalScrollHelper(@NotNull Supplier<MarkdownHtmlPanelEx> htmlPanelSupplier) {
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
    private static class ViewScrollHelper extends MouseAdapter {
        private final @NotNull JBScrollPane scrollPane;

        private ViewScrollHelper(@NotNull JBScrollPane htmlPanelSupplier) {
            this.scrollPane = htmlPanelSupplier;
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent event) {
            if (scrollPane == null) {
                return;
            }
            if (event.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                final var multiplier = Registry.intValue("ide.browser.jcef.osr.wheelRotation.factor", 1);
                final var amount = event.getScrollAmount() * event.getWheelRotation() * multiplier;
                JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();

                verticalScrollBar.setValue(verticalScrollBar.getValue()+amount);
            }
        }


    }
}
