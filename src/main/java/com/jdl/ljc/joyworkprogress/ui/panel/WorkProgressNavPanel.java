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
import com.jdl.ljc.joyworkprogress.enums.NavButtonEnum;
import org.apache.commons.lang3.math.NumberUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author liangjichao
 * @date 2023/10/17 9:42 AM
 */
public class WorkProgressNavPanel extends JBPanel {
    private static final Integer DEFAULT_PAGE_SIZE = 10;
    public static final String place = "WPS_NAV_Toolbar";
    private WorkProgressPanel panel;

    private Long cpage;
    private WpsPageDto pageDto;

    private JBTextField pageSizeField;

    public WorkProgressNavPanel(WorkProgressPanel panel) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cpage = 1L;
        setBorder(JBUI.Borders.customLineTop(JBUI.CurrentTheme.ToolWindow.headerBorderBackground()));
        this.panel = panel;
        ActionToolbar leftToolbar = createToolbar();
        leftToolbar.setTargetComponent(this);
        add(leftToolbar.getComponent());
        add(new JBLabel("Page Size:"));
        pageSizeField = new JBTextField();
        pageSizeField.setText(String.valueOf(DEFAULT_PAGE_SIZE));
        pageSizeField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    cpage=1L;
                    updateNav();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        add(pageSizeField);
    }

    private ActionToolbar createToolbar() {
        DefaultActionGroup actionGroup = new DefaultActionGroup("WPS_NAV_BAR_GROUP", false);

        actionGroup.add(new NavButtonAction(NavButtonEnum.FIRST.name(), AllIcons.Actions.Play_first,this));
        actionGroup.add(new NavButtonAction(NavButtonEnum.PREV.name(), AllIcons.General.ArrowLeft,this));
        actionGroup.add(new NavPageButtonAction(this));
        actionGroup.add(new NavButtonAction(NavButtonEnum.NEXT.name(), AllIcons.General.ArrowRight,this));
        actionGroup.add(new NavButtonAction(NavButtonEnum.LAST.name(), AllIcons.Actions.Play_last,this));
        FlushProgressDialogAction flushAction = new FlushProgressDialogAction(AllIcons.Actions.Refresh, panel);
        actionGroup.add(flushAction);
        ActionManager actionManager = ActionManager.getInstance();
        return actionManager.createActionToolbar(place, actionGroup, true);
    }

    public Long getCpage() {
        return cpage;
    }

    public Integer getPageSize() {
        return NumberUtils.toInt(pageSizeField.getText(), DEFAULT_PAGE_SIZE);
    }

    public void setCpage(Long cpage) {
        this.cpage = cpage;
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
