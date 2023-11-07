package com.jdl.ljc.joyworkprogress;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.jdl.ljc.joyworkprogress.domain.WpsConfig;
import com.jdl.ljc.joyworkprogress.toolwindow.HomeToolWindowPanel;
import org.jetbrains.annotations.NotNull;

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

        Content content = contentManager.getFactory().createContent(panel, "工作进度列表", false);
        contentManager.addContent(content);

    }


}
