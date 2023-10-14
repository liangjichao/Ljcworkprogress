package com.jdl.ljc.joyworkprogress.toolwindow;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.ComboBoxFieldPanel;
import com.intellij.ui.components.TwoSideComponent;
import com.jdl.ljc.joyworkprogress.action.*;
import com.jdl.ljc.joyworkprogress.enums.WorkProgressStatusEnum;
import com.jdl.ljc.joyworkprogress.ui.panel.SearchComboBoxPanel;
import com.jdl.ljc.joyworkprogress.ui.panel.WorkProgressPanel;
import com.jdl.ljc.joyworkprogress.domain.vo.WorkProgressGridData;
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
        data.setUserCode("liangjichao");
        for (int i = 0; i < 20; i++) {
            data = new WorkProgressGridData();
            gridDataList.add(data);
            data.setTitle("上架页面的保质期展示日月管理维度");
            data.setProgressStatus(WorkProgressStatusEnum.DEV_ING.toString());
            data.setPlanWorkHours("2023.10.1-2023.10.7");
            data.setUserCode("liangjichao");
        }

        WorkProgressPanel panel = new WorkProgressPanel(gridDataList);

        ActionToolbar leftToolbar = createToolbar(panel);
        leftToolbar.setTargetComponent(panel);


        List<String> userCodeVecs = new ArrayList<>();
        userCodeVecs.add("all");
        userCodeVecs.add("liangjichao");
        userCodeVecs.add("wangcaixia");
        userCodeVecs.add("wangbin163");
        ComboBoxFieldPanel userCodeComoBox = new ComboBoxFieldPanel(userCodeVecs.toArray(new String[4]), null, "搜索用户", null, new Runnable() {
            @Override
            public void run() {
                System.out.println("::::");
            }
        });



//        SearchTextField searchTextField = new SearchTextField();
        SearchComboBoxPanel searchComboBoxPanel = new SearchComboBoxPanel();

        TwoSideComponent twoSideComponent = new TwoSideComponent(searchComboBoxPanel,leftToolbar.getComponent());

        setToolbar(twoSideComponent);
        setContent(panel);

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
