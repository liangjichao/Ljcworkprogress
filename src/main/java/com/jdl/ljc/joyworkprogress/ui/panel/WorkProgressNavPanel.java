package com.jdl.ljc.joyworkprogress.ui.panel;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.util.NlsActions;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.components.JBPanel;
import com.jdl.ljc.joyworkprogress.action.AddWorkProgressDialogAction;
import com.jdl.ljc.joyworkprogress.action.DeleteWorkProgressDialogAction;
import com.jdl.ljc.joyworkprogress.action.EditWorkProgressDialogAction;
import com.jdl.ljc.joyworkprogress.action.FlushProgressDialogAction;
import com.jdl.ljc.joyworkprogress.action.LocateWorkProgressDialogAction;
import com.jdl.ljc.joyworkprogress.action.SearchWorkProgressDialogAction;
import org.jdesktop.swingx.JXButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * @author liangjichao
 * @date 2023/10/17 9:42 AM
 */
public class WorkProgressNavPanel extends JBPanel {
    public WorkProgressNavPanel() {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
//        JXButton btn = new JXButton(AllIcons.Actions.Play_first);
//        add(btn);
        ActionToolbar leftToolbar = createToolbar();
        leftToolbar.setTargetComponent(this);
        add(leftToolbar.getComponent());
    }

    private ActionToolbar createToolbar() {
        DefaultActionGroup actionGroup = new DefaultActionGroup("WPS_NAV_BAR_GROUP", false);

        AnAction action = new AnAction("首页") {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                setToolTipText("test");
            }

            @Override
            public boolean displayTextInToolbar() {
                return true;
            }


        };
        actionGroup.add(action);

        ActionManager actionManager = ActionManager.getInstance();
        return actionManager.createActionToolbar("WPS_NAV_Toolbar", actionGroup, true);
    }
}
