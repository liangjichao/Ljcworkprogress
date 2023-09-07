package com.jdl.ljc.joyworkprogress.panel;

import com.intellij.openapi.project.Project;
import com.intellij.ui.jcef.JCEFHtmlPanel;
import net.minidev.json.JSONObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author liangjichao
 * @date 2023/9/7 3:57 PM
 */
public class ProgressPanel extends JCEFHtmlPanel {
    public ProgressPanel(@NotNull Project project,String html) {
        super(null);
        setHtml(html);
    }
}
