package com.jdl.ljc.joyworkprogress.ui.panel;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBUI;
import com.jdl.ljc.joyworkprogress.action.FlushProgressDialogAction;
import com.jdl.ljc.joyworkprogress.action.NavButtonAction;
import com.jdl.ljc.joyworkprogress.action.NavPageButtonAction;
import com.jdl.ljc.joyworkprogress.domain.dto.WpsPageDto;
import org.jdesktop.swingx.JXTextField;

import java.awt.*;

/**
 * @author liangjichao
 * @date 2023/10/17 9:42 AM
 */
public class WorkProgressNavPanel extends JBPanel {
    private static final Integer DEFAULT_PAGE_SIZE = 30;
    public static final String place = "WPS_NAV_Toolbar";
    private WorkProgressPanel panel;

    private Long cpage;
    private Integer pageSize;

    private WpsPageDto pageDto;

    public WorkProgressNavPanel(WorkProgressPanel panel) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cpage = 1L;
        pageSize = DEFAULT_PAGE_SIZE;
        setBorder(JBUI.Borders.customLineTop(JBUI.CurrentTheme.ToolWindow.headerBorderBackground()));
        this.panel = panel;
        ActionToolbar leftToolbar = createToolbar();
        leftToolbar.setTargetComponent(this);
        add(leftToolbar.getComponent());
        add(new JBLabel("Page Size:"));
        add(new JBTextField());
    }

    private ActionToolbar createToolbar() {
        DefaultActionGroup actionGroup = new DefaultActionGroup("WPS_NAV_BAR_GROUP", false);

        actionGroup.add(new NavButtonAction("first", AllIcons.Actions.Play_first,this));
        actionGroup.add(new NavButtonAction("prev", AllIcons.General.ArrowLeft,this));
        actionGroup.add(new NavPageButtonAction(this));
        actionGroup.add(new NavButtonAction("next", AllIcons.General.ArrowRight,this));
        actionGroup.add(new NavButtonAction("last", AllIcons.Actions.Play_last,this));
        FlushProgressDialogAction flushAction = new FlushProgressDialogAction(AllIcons.Actions.Refresh, panel);
        actionGroup.add(flushAction);
        ActionManager actionManager = ActionManager.getInstance();
        return actionManager.createActionToolbar(place, actionGroup, true);
    }

    public Long getCpage() {
        return cpage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setCpage(Long cpage) {
        this.cpage = cpage;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public WpsPageDto getPageDto() {
        return pageDto;
    }

    public void setPageDto(WpsPageDto pageDto) {
        this.pageDto = pageDto;
    }

    public void updateNav() {
        this.panel.refreshTableData();
    }
}
