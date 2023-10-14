package com.jdl.ljc.joyworkprogress.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.jdl.ljc.joyworkprogress.enums.WorkProgressStatusEnum;
import com.jdl.ljc.joyworkprogress.ui.panel.WorkProgressPanel;
import com.jdl.ljc.joyworkprogress.domain.vo.WorkProgressGridData;
import org.apache.commons.lang3.RandomUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class FlushProgressDialogAction extends AnAction {
    private WorkProgressPanel panel;

    public FlushProgressDialogAction(Icon icon, WorkProgressPanel panel) {
        super(icon);
        this.panel = panel;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        List<WorkProgressGridData> gridDataList = new ArrayList<>();
        WorkProgressGridData data = new WorkProgressGridData();
        gridDataList.add(data);
        data.setTitle("存拣模式库存查询页面增加箱件数RF");
        data.setProgressStatus(WorkProgressStatusEnum.ON_LINE_FINISH.toString());
        data.setPlanWorkHours("2023.10.1-2023.10.7");
        int max = RandomUtils.nextInt(1, 30);
        for (int i = 0; i < max; i++) {
            data = new WorkProgressGridData();
            gridDataList.add(data);
            data.setTitle("存拣模式库存查询页面增加箱件数RF");
            data.setProgressStatus(WorkProgressStatusEnum.DEV_ING.toString());
            data.setPlanWorkHours("2023.10.1-2023.10.7");
        }
        this.panel.refreshTable(gridDataList);
    }
}
