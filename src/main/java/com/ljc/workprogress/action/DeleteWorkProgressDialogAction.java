package com.ljc.workprogress.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.ljc.workprogress.domain.dto.ResultDto;
import com.ljc.workprogress.domain.dto.WpsDto;
import com.ljc.workprogress.ui.panel.WorkProgressPanel;
import com.ljc.workprogress.util.RestUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DeleteWorkProgressDialogAction extends AnAction {
    private final WorkProgressPanel panel;

    public DeleteWorkProgressDialogAction(Icon icon, WorkProgressPanel panel) {
        super(icon);
        this.panel = panel;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        WpsDto wpsDto = this.panel.getSelectRow();
        if (wpsDto == null) {
            Messages.showInfoMessage("请选择一条记录!","提示");
        }else {
            ResultDto<String> resultDto = RestUtils.post(String.class, String.format("/wps/delete/%s", wpsDto.getId()), null);
            if (resultDto.isSuccess()) {
                this.panel.resetTableData();
            }else{
                Messages.showInfoMessage(resultDto.getResultMessage(),"删除失败");
            }
        }

    }
}
