package com.ljc.workprogress.action.editor;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.ljc.workprogress.enums.EditorButtonEnum;
import com.ljc.workprogress.ui.dialog.EditorImgDialog;
import com.ljc.workprogress.ui.dialog.EditorLinkDialog;
import com.ljc.workprogress.ui.editor.WpsMarkdownEditor;
import com.ljc.workprogress.util.MarkdownTextUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class EditorButtonAction extends AnAction {
    private final String uid;

    private final WpsMarkdownEditor editor;
    public EditorButtonAction(String uid,Icon icon,WpsMarkdownEditor editor) {
        super(icon);
        this.uid = uid;
        this.editor=editor;
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        if (EditorButtonEnum.FULL_SCREENT.name().equals(uid)) {
            editor.getFormDialog().full();
        } else if (EditorButtonEnum.LINK.name().equals(uid)) {
            EditorLinkDialog dialog = new EditorLinkDialog();
            dialog.show();
            String url = dialog.getURLText();
            String title = dialog.getTitleText();
            if (StringUtils.isBlank(title)) {
                title=url;
            }
            if (StringUtils.isNoneBlank(url)) {
                String selectText = editor.getSelectionText();
                if (StringUtils.isBlank(selectText)) {
                    editor.insertText(MarkdownTextUtils.createLink(title, url));
                }else{
                    editor.replateText(MarkdownTextUtils.createLink(title, url));
                }
            }
        }else if(EditorButtonEnum.spliterBtn(uid)){
            editor.changeSplitter(uid);
        } else if (EditorButtonEnum.BROWSER.name().equals(uid)) {
            editor.openView();
        } else if (EditorButtonEnum.IMG.name().equals(uid)) {
            EditorImgDialog dialog = new EditorImgDialog();
            dialog.show();
            String url = dialog.getURLText();
            String selectText = editor.getSelectionText();
            if (StringUtils.isNoneBlank(url)) {
                if (StringUtils.isBlank(selectText)) {
                    editor.insertText(MarkdownTextUtils.createImg(url,dialog.getWidthText(),dialog.getHeightText()));
                }else{
                    editor.replateText(MarkdownTextUtils.createImg(url,dialog.getWidthText(),dialog.getHeightText()));
                }
            }
        }

    }


}
