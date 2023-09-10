package com.jdl.ljc.joyworkprogress.toolwindow;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.jdl.ljc.joyworkprogress.action.AddWorkProgressDialogAction;
import com.jdl.ljc.joyworkprogress.ui.panel.ProgressPanel;
import org.jetbrains.annotations.NotNull;

public class HomeToolWindowPanel extends SimpleToolWindowPanel {
    public HomeToolWindowPanel(@NotNull Project project) {
        super(true);
        initialize(project);
    }

    private void initialize(Project project) {
        ProgressPanel jsp=new ProgressPanel(project,"<h1>Hello World!!</h1>");

        ActionToolbar toolbar = createToolbar(project);
        toolbar.setTargetComponent(jsp.getComponent());


        setToolbar(toolbar.getComponent());
        setContent(jsp.getComponent());
    }

    private ActionToolbar createToolbar(Project project) {
        DefaultActionGroup actionGroup = new DefaultActionGroup("WORK_BAR_GROUP",false);
        AddWorkProgressDialogAction addAction=new AddWorkProgressDialogAction(AllIcons.Actions.AddFile);
        actionGroup.add(addAction);

        ActionManager actionManager = ActionManager.getInstance();
        return actionManager.createActionToolbar("NAV_DevWorkToolbar", actionGroup,false);
    }
}
