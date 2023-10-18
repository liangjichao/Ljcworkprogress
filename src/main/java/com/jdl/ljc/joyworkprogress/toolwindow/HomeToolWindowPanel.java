package com.jdl.ljc.joyworkprogress.toolwindow;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.components.TwoSideComponent;
import com.intellij.util.ui.JBUI;
import com.jdl.ljc.joyworkprogress.action.AddWorkProgressDialogAction;
import com.jdl.ljc.joyworkprogress.action.DeleteWorkProgressDialogAction;
import com.jdl.ljc.joyworkprogress.action.EditWorkProgressDialogAction;
import com.jdl.ljc.joyworkprogress.action.LocateWorkProgressDialogAction;
import com.jdl.ljc.joyworkprogress.action.SettingAction;
import com.jdl.ljc.joyworkprogress.domain.WpsConfig;
import com.jdl.ljc.joyworkprogress.ui.panel.SearchComboBoxPanel;
import com.jdl.ljc.joyworkprogress.ui.panel.WorkProgressPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class HomeToolWindowPanel extends SimpleToolWindowPanel {
    private WorkProgressPanel gridPanel;
    private SearchComboBoxPanel searchComboBoxPanel;

    private Project project;
    public HomeToolWindowPanel(@NotNull Project project) {
        super(true);
        this.project=project;
        initialize(project);
    }
    private void initialize(Project project) {
        InitWorkTasker tasker = new InitWorkTasker(project);
        tasker.execute();



        gridPanel = new WorkProgressPanel(this);

        ActionToolbar leftToolbar = createToolbar(gridPanel);
        leftToolbar.setTargetComponent(gridPanel);


        searchComboBoxPanel = new SearchComboBoxPanel(this);

        TwoSideComponent twoSideComponent = new TwoSideComponent(searchComboBoxPanel, leftToolbar.getComponent());
        twoSideComponent.setBorder(JBUI.Borders.customLineBottom(JBUI.CurrentTheme.ToolWindow.headerBorderBackground()));
        setToolbar(twoSideComponent);
        setContent(gridPanel);

        gridPanel.refreshTableData();
    }

    private class InitWorkTasker extends SwingWorker{
        private Project project;
        public InitWorkTasker(Project project){
            this.project=project;
        }
        @Override
        protected Object doInBackground() throws Exception {
            WpsConfig.getInstance().init(project);
            return null;
        }
    }


    private ActionToolbar createToolbar(WorkProgressPanel panel) {
        DefaultActionGroup actionGroup = new DefaultActionGroup("WORK_BAR_GROUP", false);
        actionGroup.add(new SettingAction(project));
        actionGroup.addSeparator();
        LocateWorkProgressDialogAction locateAction = new LocateWorkProgressDialogAction(AllIcons.Providers.Openedge, panel);
        actionGroup.add(locateAction);
        actionGroup.addSeparator();
        AddWorkProgressDialogAction addWorkAction = new AddWorkProgressDialogAction(AllIcons.Actions.AddFile, panel);
        actionGroup.add(addWorkAction);
        EditWorkProgressDialogAction editAction = new EditWorkProgressDialogAction(AllIcons.Actions.EditSource, panel);
        actionGroup.add(editAction);

        actionGroup.addSeparator();
        DeleteWorkProgressDialogAction delAction = new DeleteWorkProgressDialogAction(AllIcons.Actions.DeleteTag, panel);
        actionGroup.add(delAction);

        ActionManager actionManager = ActionManager.getInstance();
        return actionManager.createActionToolbar("NAV_DevWorkToolbar", actionGroup, true);
    }

    public WorkProgressPanel getGridPanel() {
        return gridPanel;
    }

    public SearchComboBoxPanel getSearchComboBoxPanel() {
        return searchComboBoxPanel;
    }
}
