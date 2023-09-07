package com.jdl.ljc.joyworkprogress;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.intellij.util.ui.UIUtil;
import com.jdl.ljc.joyworkprogress.panel.ProgressPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * @author liangjichao
 * @date 2023/9/7 1:32 PM
 */
public class DevWorkProgressWindowFactory implements ToolWindowFactory, DumbAware {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        if (project == null) {
            return;
        }
        if (toolWindow == null) {
            return;
        }
        ContentManager contentManager = toolWindow.getContentManager();
        SimpleToolWindowPanel panel = new SimpleToolWindowPanel(true);
        panel.setBackground(UIUtil.getFieldForegroundColor());

        ProgressPanel jsp=new ProgressPanel(project,"<h1>Hello World!!!</h1>");

        panel.setContent(jsp.getComponent());

        panel.revalidate();
        Content content = contentManager.getFactory().createContent(panel, "DevProgress", false);
        contentManager.addContent(content);
    }
}
