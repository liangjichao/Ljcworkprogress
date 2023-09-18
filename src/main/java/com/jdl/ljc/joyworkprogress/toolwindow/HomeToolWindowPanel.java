package com.jdl.ljc.joyworkprogress.toolwindow;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.table.JBTable;
import com.jdl.ljc.joyworkprogress.action.AddWorkProgressDialogAction;
import com.jdl.ljc.joyworkprogress.ui.panel.ProgressHtmlPanel;
import com.jdl.ljc.joyworkprogress.ui.panel.WorkProgressPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HomeToolWindowPanel extends SimpleToolWindowPanel {
    public HomeToolWindowPanel(@NotNull Project project) {
        super(true);
        initialize(project);
    }

    private void initialize(Project project) {
//        ProgressHtmlPanel jsp=new ProgressHtmlPanel(project,"<h1>Hello World!!</h1>");
        WorkProgressPanel panel = new WorkProgressPanel();

        ActionToolbar toolbar = createToolbar(project);
        toolbar.setTargetComponent(panel);

        setToolbar(toolbar.getComponent());
        setContent(panel);
    }

    private ActionToolbar createToolbar(Project project) {
        DefaultActionGroup actionGroup = new DefaultActionGroup("WORK_BAR_GROUP",false);
        AddWorkProgressDialogAction addAction=new AddWorkProgressDialogAction(AllIcons.Actions.AddFile);
        actionGroup.add(addAction);

        ActionManager actionManager = ActionManager.getInstance();
        return actionManager.createActionToolbar("NAV_DevWorkToolbar", actionGroup,false);
    }
}
