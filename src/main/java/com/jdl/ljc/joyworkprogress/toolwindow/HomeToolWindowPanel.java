package com.jdl.ljc.joyworkprogress.toolwindow;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.jdl.ljc.joyworkprogress.action.AddWorkProgressDialogAction;
import com.jdl.ljc.joyworkprogress.action.DeleteWorkProgressDialogAction;
import com.jdl.ljc.joyworkprogress.action.EditWorkProgressDialogAction;
import com.jdl.ljc.joyworkprogress.ui.panel.WorkProgressPanel;
import org.jetbrains.annotations.NotNull;

public class HomeToolWindowPanel extends SimpleToolWindowPanel {
    public HomeToolWindowPanel(@NotNull Project project) {
        super(true);
        initialize(project);
    }

    private void initialize(Project project) {
//        ProgressHtmlPanel jsp=new ProgressHtmlPanel(project,"<h1>Hello World!!</h1>");
        WorkProgressPanel panel = new WorkProgressPanel();

        ActionToolbar toolbar = createToolbar(panel);
        toolbar.setTargetComponent(panel);

        setToolbar(toolbar.getComponent());
        setContent(panel);
    }

    private ActionToolbar createToolbar(WorkProgressPanel panel) {
        DefaultActionGroup actionGroup = new DefaultActionGroup("WORK_BAR_GROUP", false);
        AddWorkProgressDialogAction addAction = new AddWorkProgressDialogAction(AllIcons.Actions.AddFile, panel);
        actionGroup.add(addAction);
        EditWorkProgressDialogAction editAction = new EditWorkProgressDialogAction(AllIcons.Actions.EditSource, panel);
        actionGroup.add(editAction);
        DeleteWorkProgressDialogAction delAction = new DeleteWorkProgressDialogAction(AllIcons.Actions.DeleteTag, panel);
        actionGroup.add(delAction);

        ActionManager actionManager = ActionManager.getInstance();
        return actionManager.createActionToolbar("NAV_DevWorkToolbar", actionGroup, false);
    }
}
