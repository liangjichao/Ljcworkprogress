package com.ljc.workprogress.ui.editor;

/**
 * @author liangjichao
 * @date 2023/11/3 10:01 AM
 */
public interface WpsViewPanel {
    void updateContent(String content, int offset);

    String getConvertHTML(String content);

    String getViewUrl();

}
