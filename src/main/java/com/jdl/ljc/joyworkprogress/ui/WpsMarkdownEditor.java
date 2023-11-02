package com.jdl.ljc.joyworkprogress.ui;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
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
import com.jdl.ljc.joyworkprogress.action.editor.EditorButtonAction;
import com.jdl.ljc.joyworkprogress.enums.EditorButtonEnum;
import com.jdl.ljc.joyworkprogress.ui.dialog.JDWorkProgressFormDialog;
import com.jdl.ljc.joyworkprogress.ui.panel.WpsMarkdownJCEFViewPanel;
import com.jdl.ljc.joyworkprogress.ui.panel.WpsMarkdownViewPanel;
import icons.JoyworkprogressIcons;
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

    private JDWorkProgressFormDialog formDialog;
    public WpsMarkdownEditor(Project project,String content,JDWorkProgressFormDialog formDialog) {
        this.formDialog = formDialog;
        Document document = EditorFactory.getInstance().createDocument(content);
        editor = EditorFactory.getInstance().createEditor(document, project, FileTypeManager.getInstance().getFileTypeByExtension("md"), false);
        editor.getSettings().setLineNumbersShown(true);

        if (JBCefApp.isSupported()) {
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

        JPanel editorPanel = new JPanel(new BorderLayout());
        ActionToolbar toolbar = createToolbar();
        toolbar.setTargetComponent(editorPanel);
        editorPanel.add(toolbar.getComponent(), BorderLayout.NORTH);
        editorPanel.add(splitter, BorderLayout.CENTER);

        myComponent = splitter;
    }
    public String getSelectionText() {
        return this.editor.getSelectionModel().getSelectedText();
    }

    public void insertText(String text) {
        ApplicationManager.getApplication().runWriteAction(() -> {
            CommandProcessor.getInstance().executeCommand(editor.getProject(), () -> {
                int offset = editor.getCaretModel().getOffset();
                editor.getDocument().insertString(offset, text);
                editor.getCaretModel().moveToOffset(offset + text.length());
            }, "Insert Text", null);
        });

    }

    public void replateText(String text) {
        ApplicationManager.getApplication().runWriteAction(() -> {
            CommandProcessor.getInstance().executeCommand(editor.getProject(), () -> {
                int start = editor.getSelectionModel().getSelectionStart();
                int end = editor.getSelectionModel().getSelectionEnd();
                editor.getDocument().replaceString(start, end, text);
                editor.getCaretModel().moveToOffset(start
                        + text.length());
                editor.getSelectionModel().removeSelection();
            }, "Insert Text", null);
        });

    }
    private ActionToolbar createToolbar() {
        DefaultActionGroup actionGroup = new DefaultActionGroup("WPS_EDITOR_GROUP", false);

        actionGroup.add(new EditorButtonAction(EditorButtonEnum.FULL_SCREENT.name(), JoyworkprogressIcons.FULL_SCREEN, this));

        ActionManager actionManager = ActionManager.getInstance();
        return actionManager.createActionToolbar("WPS_EDITOR_TOOLBAR", actionGroup, true);
    }

    public JDWorkProgressFormDialog getFormDialog() {
        return formDialog;
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
