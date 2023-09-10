package com.jdl.ljc.joyworkprogress;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.intellij.util.ui.UIUtil;
import com.jdl.ljc.joyworkprogress.action.AddWorkProgressDialogAction;
import com.jdl.ljc.joyworkprogress.toolwindow.HomeToolWindowPanel;
import com.jdl.ljc.joyworkprogress.ui.panel.ProgressPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

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
        HomeToolWindowPanel panel = new HomeToolWindowPanel(project);

        Content content = contentManager.getFactory().createContent(panel, "", false);
        contentManager.addContent(content);

    }


}
