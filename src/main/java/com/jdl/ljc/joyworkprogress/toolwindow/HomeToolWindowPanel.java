package com.jdl.ljc.joyworkprogress.toolwindow;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.components.TwoSideComponent;
import com.jdl.ljc.joyworkprogress.action.*;
import com.jdl.ljc.joyworkprogress.domain.dto.ResultDto;
import com.jdl.ljc.joyworkprogress.domain.dto.WpsDto;
import com.jdl.ljc.joyworkprogress.domain.vo.WorkProgressGridData;
import com.jdl.ljc.joyworkprogress.domain.vo.WpsQueryDto;
import com.jdl.ljc.joyworkprogress.enums.WorkProgressStatusEnum;
import com.jdl.ljc.joyworkprogress.ui.panel.SearchComboBoxPanel;
import com.jdl.ljc.joyworkprogress.ui.panel.WorkProgressPanel;
import com.jdl.ljc.joyworkprogress.util.RestUtils;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HomeToolWindowPanel extends SimpleToolWindowPanel {
    public HomeToolWindowPanel(@NotNull Project project) {
        super(true);
        initialize(project);
    }

    private void initialize(Project project) {



        WorkProgressPanel panel = new WorkProgressPanel();

        ActionToolbar leftToolbar = createToolbar(panel);
        leftToolbar.setTargetComponent(panel);


        SearchComboBoxPanel searchComboBoxPanel = new SearchComboBoxPanel();

        TwoSideComponent twoSideComponent = new TwoSideComponent(searchComboBoxPanel,leftToolbar.getComponent());

        setToolbar(twoSideComponent);
        setContent(panel);

        WpsQueryDto queryDto = new WpsQueryDto();
        RestUtils.post("/wps/list", queryDto, new FutureCallback<SimpleHttpResponse>() {
            @Override
            public void completed(SimpleHttpResponse result) {
                String responseText = result.getBody().getBodyText();
                ResultDto<List<WpsDto>> resultDto = ResultDto.toResultList(responseText, WpsDto.class);
                List<WorkProgressGridData> gridDataList = new ArrayList<>();
                WorkProgressGridData data;
                for (WpsDto wpsDto : resultDto.getResultValue()) {
                    data = new WorkProgressGridData();
                    gridDataList.add(data);
                    data.setTitle(wpsDto.getProjectName());
                    data.setProgressStatus(WorkProgressStatusEnum.queryStatusEnum(wpsDto.getProgressStatus()).toString());
                    String planWorkHours = wpsDto.getPlanStartTime();
                    if (wpsDto.getPlanEndTime() != null) {
                        planWorkHours += "-" + wpsDto.getPlanEndTime();
                    }
                    data.setPlanWorkHours(planWorkHours);
                    data.setUserCode(wpsDto.getUserCode());
                }
                panel.setData(gridDataList);
            }

            @Override
            public void failed(Exception ex) {

            }

            @Override
            public void cancelled() {

            }
        });

//        List<WorkProgressGridData> gridDataList = new ArrayList<>();
//        WorkProgressGridData data = new WorkProgressGridData();
//        gridDataList.add(data);
//        data.setTitle("上架页面的保质期展示日月管理维度");
//        data.setProgressStatus(WorkProgressStatusEnum.DEV_ING.toString());
//        data.setPlanWorkHours("2023.10.1-2023.10.7");
//        data.setUserCode("liangjichao");
//        for (int i = 0; i < 20; i++) {
//            data = new WorkProgressGridData();
//            gridDataList.add(data);
//            data.setTitle("上架页面的保质期展示日月管理维度");
//            data.setProgressStatus(WorkProgressStatusEnum.DEV_ING.toString());
//            data.setPlanWorkHours("2023.10.1-2023.10.7");
//            data.setUserCode("liangjichao");
//        }
//        panel.setData(gridDataList);

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
