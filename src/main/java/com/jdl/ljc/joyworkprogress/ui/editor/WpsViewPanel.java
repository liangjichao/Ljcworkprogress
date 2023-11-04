package com.jdl.ljc.joyworkprogress.ui.editor;

import javax.swing.*;

/**
 * @author liangjichao
 * @date 2023/11/3 10:01 AM
 */
public interface WpsViewPanel {
    void updateContent(String content, int offset);

    String getConvertHTML(String content);

    String getViewUrl();

}
