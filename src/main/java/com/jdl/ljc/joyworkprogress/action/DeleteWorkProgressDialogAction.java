package com.jdl.ljc.joyworkprogress.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.jdl.ljc.joyworkprogress.domain.dto.ResultDto;
import com.jdl.ljc.joyworkprogress.domain.dto.WpsDto;
import com.jdl.ljc.joyworkprogress.ui.panel.WorkProgressPanel;
import com.jdl.ljc.joyworkprogress.util.RestUtils;

import javax.swing.*;

public class DeleteWorkProgressDialogAction extends AnAction {
    private WorkProgressPanel panel;

    public DeleteWorkProgressDialogAction(Icon icon, WorkProgressPanel panel) {
        super(icon);
        this.panel = panel;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
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
