package com.ljc.workprogress.ui.editor;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.ui.JBColor;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.OnePixelSplitter;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.jcef.JBCefApp;
import com.ljc.workprogress.action.editor.EditorButtonAction;
import com.ljc.workprogress.enums.EditorButtonEnum;
import com.ljc.workprogress.ui.dialog.JDWorkProgressFormDialog;
import com.ljc.workprogress.ui.editor.listener.EditorDocumentListener;
import com.ljc.workprogress.util.ObjectUtils;
import icons.LjcworkprogressIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;

/**
 * @author liangjichao
 * @date 2023/10/23 5:00 PM
 */
public class WpsMarkdownEditor implements Disposable {

    private JComponent myComponent;

    private JComponent viewEditor;

    private WpsEditorPanel editorPanel;

    private JDWorkProgressFormDialog formDialog;
    private JBSplitter editorSplitter;
    private WpsViewPanel wpsViewPanel;

    private Project project;

    private boolean cefApp;

    public WpsMarkdownEditor(Project project, String content, JDWorkProgressFormDialog formDialog) {

        this.project = project;
        this.formDialog = formDialog;
        editorPanel = ApplicationManager.getApplication().getService(WpsEditorPanel.class);
        editorPanel.getEditorArea().setText(content);
        cefApp=JBCefApp.isSupported()&& ObjectUtils.hasClazz("com.intellij.ui.jcef.JCEFHtmlPanel");
        if (cefApp) {
            WpsMarkdownJCEFViewPanel viewPanel = ApplicationManager.getApplication().getService(WpsMarkdownJCEFViewPanel.class);
            viewPanel.updateContent(content, 0);
            wpsViewPanel = viewPanel;
            editorPanel.getScrollPane().addMouseWheelListener(new PreciseVerticalScrollHelper(viewPanel));
            editorPanel.getEditorArea().getDocument().addDocumentListener(new EditorDocumentListener(editorPanel, viewPanel));

            viewEditor = viewPanel;
        } else {
//            WpsMarkdownViewPanel viewPanel = ApplicationManager.getApplication().getService(WpsMarkdownViewPanel.class);
//            viewPanel.setText(content);
//            wpsViewPanel = viewPanel;
//            JBScrollPane scrollPane = new JBScrollPane(viewPanel);
//            editorPanel.getScrollPane().addMouseWheelListener(new ViewScrollHelper(scrollPane));
//            editorPanel.getEditorArea().getDocument().addDocumentListener(new EditorDocumentListener(editorPanel, viewPanel));
//            viewEditor = scrollPane;
            WpsMarkdownBrowserView viewPanel = ApplicationManager.getApplication().getService(WpsMarkdownBrowserView.class);
            wpsViewPanel = viewPanel;
        }


        editorSplitter = new OnePixelSplitter(false);
        editorSplitter.setDividerWidth(1);
        JPanel divider = editorSplitter.getDivider();
        divider.setBackground(JBColor.border().brighter());
        editorSplitter.setFirstComponent(editorPanel.getScrollPane());
        editorSplitter.setSecondComponent(viewEditor);


        JPanel editorPanel = new JPanel(new BorderLayout());
        ActionToolbar toolbar = createToolbar();
        toolbar.setTargetComponent(editorPanel);
        editorPanel.add(toolbar.getComponent(), BorderLayout.NORTH);
        editorPanel.add(editorSplitter, BorderLayout.CENTER);

        myComponent = editorPanel;

    }

    public void changeSplitter(String uid) {
        if (EditorButtonEnum.EDITOR.name().equals(uid)) {
            editorSplitter.getFirstComponent().setVisible(true);
            editorSplitter.getSecondComponent().setVisible(false);
        } else if (EditorButtonEnum.EDITOR_AND_PREVIEW.name().equals(uid)) {
            editorSplitter.getFirstComponent().setVisible(true);
            editorSplitter.getSecondComponent().setVisible(true);
        } else if (EditorButtonEnum.PREVIEW.name().equals(uid)) {
            editorSplitter.getFirstComponent().setVisible(false);
            editorSplitter.getSecondComponent().setVisible(true);
        }
        editorSplitter.getFirstComponent().revalidate();
        editorSplitter.getFirstComponent().repaint();
        editorSplitter.getSecondComponent().revalidate();
        editorSplitter.getSecondComponent().repaint();
        editorSplitter.revalidate();
        editorSplitter.repaint();
    }

    public String getSelectionText() {
        return editorPanel.getEditorArea().getSelectedText();
    }

    public void insertText(String text) {
        ApplicationManager.getApplication().runWriteAction(() -> {
            CommandProcessor.getInstance().executeCommand(project, () -> {
                int offset = editorPanel.getEditorArea().getCaretPosition();
                editorPanel.getEditorArea().insert(text, offset);
                editorPanel.getEditorArea().moveCaretPosition(offset + text.length());
            }, "Insert Text", null);
        });

    }

    public void replateText(String text) {
        ApplicationManager.getApplication().runWriteAction(() -> {
            CommandProcessor.getInstance().executeCommand(project, () -> {
                editorPanel.getEditorArea().replaceSelection(text);
            }, "Insert Text", null);
        });

    }

    public void openView() {
        BrowserUtil.browse(wpsViewPanel.getViewUrl());
    }

    private ActionToolbar createToolbar() {
        DefaultActionGroup actionGroup = new DefaultActionGroup("WPS_EDITOR_GROUP", false);

        actionGroup.add(new EditorButtonAction(EditorButtonEnum.FULL_SCREENT.name(), LjcworkprogressIcons.FULL_SCREEN, this));
        actionGroup.add(new EditorButtonAction(EditorButtonEnum.LINK.name(), LjcworkprogressIcons.LINK, this));
        actionGroup.add(new EditorButtonAction(EditorButtonEnum.IMG.name(), LjcworkprogressIcons.IMG, this));
        if (!cefApp) {
            actionGroup.addSeparator();
            actionGroup.add(new EditorButtonAction(EditorButtonEnum.BROWSER.name(), LjcworkprogressIcons.BROWSER, this));
        } else {
            actionGroup.addSeparator();
            actionGroup.add(new EditorButtonAction(EditorButtonEnum.EDITOR.name(), LjcworkprogressIcons.EDITOR, this));
            actionGroup.add(new EditorButtonAction(EditorButtonEnum.EDITOR_AND_PREVIEW.name(), LjcworkprogressIcons.EDITOR_AND_PREVIEW, this));
            actionGroup.add(new EditorButtonAction(EditorButtonEnum.PREVIEW.name(), LjcworkprogressIcons.PREVIEW, this));
        }

        ActionManager actionManager = ActionManager.getInstance();
        return actionManager.createActionToolbar("WPS_EDITOR_TOOLBAR", actionGroup, true);
    }

    public JDWorkProgressFormDialog getFormDialog() {
        return formDialog;
    }

    public String getText() {
        return editorPanel.getEditorArea().getText();
    }

    public JComponent getComponent() {
        return myComponent;
    }

    @Override
    public void dispose() {

    }

    private static class PreciseVerticalScrollHelper extends MouseAdapter {
        private final WpsMarkdownJCEFViewPanel viewPanel;

        private PreciseVerticalScrollHelper(WpsMarkdownJCEFViewPanel viewPanel) {
            this.viewPanel = viewPanel;
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent event) {
            final var actualPanel = viewPanel.getComponent();
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

                verticalScrollBar.setValue(verticalScrollBar.getValue() + amount);
            }
        }


    }


}
