package com.jdl.ljc.joyworkprogress.ui.panel;

import com.intellij.openapi.project.Project;
import com.intellij.ui.jcef.JCEFHtmlPanel;
import org.jetbrains.annotations.NotNull;

/**
 * @author liangjichao
 * @date 2023/9/7 3:57 PM
 */
public class ProgressHtmlPanel extends JCEFHtmlPanel {
    public ProgressHtmlPanel(@NotNull Project project, String html) {
        super(null);
        setHtml(html);
    }
}
