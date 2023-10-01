package com.jdl.ljc.joyworkprogress.toolwindow;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.jdl.ljc.joyworkprogress.action.*;
import com.jdl.ljc.joyworkprogress.enums.WorkProgressStatusEnum;
import com.jdl.ljc.joyworkprogress.ui.panel.WorkProgressPanel;
import com.jdl.ljc.joyworkprogress.vo.WorkProgressGridData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeToolWindowPanel extends SimpleToolWindowPanel {
    public HomeToolWindowPanel(@NotNull Project project) {
        super(true);
        initialize(project);
    }

    private void initialize(Project project) {


        List<WorkProgressGridData> gridDataList = new ArrayList<>();
        WorkProgressGridData data = new WorkProgressGridData();
        gridDataList.add(data);
        data.setTitle("上架页面的保质期展示日月管理维度");
        data.setProgressStatus(WorkProgressStatusEnum.DEV_ING.toString());
        data.setPlanWorkHours("2023.10.1-2023.10.7");
        for (int i = 0; i < 20; i++) {
            data = new WorkProgressGridData();
            gridDataList.add(data);
            data.setTitle("上架页面的保质期展示日月管理维度");
            data.setProgressStatus(WorkProgressStatusEnum.DEV_ING.toString());
            data.setPlanWorkHours("2023.10.1-2023.10.7");
        }

        WorkProgressPanel panel = new WorkProgressPanel(gridDataList);

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
        FlushProgressDialogAction flushAction = new FlushProgressDialogAction(AllIcons.Actions.Refresh, panel);
        actionGroup.add(flushAction);
        LocateWorkProgressDialogAction locateAction = new LocateWorkProgressDialogAction(AllIcons.Providers.Openedge, panel);
        actionGroup.add(locateAction);
        SearchWorkProgressDialogAction searchAction = new SearchWorkProgressDialogAction(AllIcons.Actions.Search, panel);
        actionGroup.add(searchAction);
        DeleteWorkProgressDialogAction delAction = new DeleteWorkProgressDialogAction(AllIcons.Actions.DeleteTag, panel);
        actionGroup.add(delAction);

        ActionManager actionManager = ActionManager.getInstance();
        return actionManager.createActionToolbar("NAV_DevWorkToolbar", actionGroup, false);
    }
}
