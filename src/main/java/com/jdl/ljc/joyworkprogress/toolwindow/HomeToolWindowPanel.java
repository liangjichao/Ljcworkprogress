package com.jdl.ljc.joyworkprogress.toolwindow;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.components.TwoSideComponent;
import com.jdl.ljc.joyworkprogress.action.*;
import com.jdl.ljc.joyworkprogress.domain.WpsConfig;
import com.jdl.ljc.joyworkprogress.ui.panel.SearchComboBoxPanel;
import com.jdl.ljc.joyworkprogress.ui.panel.WorkProgressPanel;
import org.jetbrains.annotations.NotNull;

public class HomeToolWindowPanel extends SimpleToolWindowPanel {
    public HomeToolWindowPanel(@NotNull Project project) {
        super(true);
        initialize(project);
    }

    private void initialize(Project project) {
        WpsConfig.getInstance().init(project);


        WorkProgressPanel panel = new WorkProgressPanel();

        ActionToolbar leftToolbar = createToolbar(panel);
        leftToolbar.setTargetComponent(panel);


        SearchComboBoxPanel searchComboBoxPanel = new SearchComboBoxPanel(panel);

        TwoSideComponent twoSideComponent = new TwoSideComponent(searchComboBoxPanel, leftToolbar.getComponent());

        setToolbar(twoSideComponent);
        setContent(panel);

        panel.refreshTableData(null);
    }


    private ActionToolbar createToolbar(WorkProgressPanel panel) {
        DefaultActionGroup actionGroup = new DefaultActionGroup("WORK_BAR_GROUP", false);
        SearchWorkProgressDialogAction searchAction = new SearchWorkProgressDialogAction(AllIcons.Actions.Search, panel);
        actionGroup.add(searchAction);
        actionGroup.addSeparator();
        LocateWorkProgressDialogAction locateAction = new LocateWorkProgressDialogAction(AllIcons.Providers.Openedge, panel);
        actionGroup.add(locateAction);
        actionGroup.addSeparator();
        AddWorkProgressDialogAction addWorkAction = new AddWorkProgressDialogAction(AllIcons.Actions.AddFile, panel);
        actionGroup.add(addWorkAction);
        EditWorkProgressDialogAction editAction = new EditWorkProgressDialogAction(AllIcons.Actions.EditSource, panel);
        actionGroup.add(editAction);
        FlushProgressDialogAction flushAction = new FlushProgressDialogAction(AllIcons.Actions.Refresh, panel);
        actionGroup.add(flushAction);

        actionGroup.addSeparator();
        DeleteWorkProgressDialogAction delAction = new DeleteWorkProgressDialogAction(AllIcons.Actions.DeleteTag, panel);
        actionGroup.add(delAction);

        ActionManager actionManager = ActionManager.getInstance();
        return actionManager.createActionToolbar("NAV_DevWorkToolbar", actionGroup, true);
    }
}
