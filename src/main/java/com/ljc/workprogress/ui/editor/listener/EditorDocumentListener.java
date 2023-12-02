package com.ljc.workprogress.ui.editor.listener;

import com.ljc.workprogress.ui.editor.WpsEditorPanel;
import com.ljc.workprogress.ui.editor.WpsViewPanel;

import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 * @author liangjichao
 * @date 2023/11/3 9:56 AM
 */
public class EditorDocumentListener implements DocumentListener {
    private WpsEditorPanel editorPanel;
    private WpsViewPanel viewPanel;

    public EditorDocumentListener(WpsEditorPanel editorPanel,WpsViewPanel viewPanel) {
        this.editorPanel=editorPanel;
        this.viewPanel=viewPanel;
    }
    @Override
    public void insertUpdate(javax.swing.event.DocumentEvent e) {
        documentChanged(e);
    }

    @Override
    public void removeUpdate(javax.swing.event.DocumentEvent e) {
        documentChanged(e);
    }

    @Override
    public void changedUpdate(javax.swing.event.DocumentEvent e) {
        documentChanged(e);
    }
    public void documentChanged(javax.swing.event.DocumentEvent e) {
        int s = editorPanel.getScrollPane().getVerticalScrollBar().getValue();
        try {
            viewPanel.updateContent(e.getDocument().getText(0,e.getDocument().getLength()),s);
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }
}
