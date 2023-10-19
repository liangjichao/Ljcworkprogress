package com.jdl.ljc.joyworkprogress.ui.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBTextField;
import com.jdl.ljc.joyworkprogress.config.WpsPluginSetting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author liangjichao
 * @date 2023/10/18 8:26 PM
 */
public class WpsSettingDialog extends DialogWrapper {
    private JBTextField domainField;

    public WpsSettingDialog(@Nullable Project project) {
        super(project);
        setTitle("设置");
        init();
        initData();
    }

    private void initData() {
        domainField.setText(WpsPluginSetting.getInstance().getState().domain);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setPreferredSize(new Dimension(350, 80));
        JPanel topPanel = getFormPanel();
        rootPanel.add(topPanel, BorderLayout.CENTER);
        return rootPanel;
    }

    private JPanel getFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        int y = 0;
        JLabel domainLabel = new JLabel("域名：");
        domainField = new JBTextField();
        constraints.gridx = 0;
        constraints.gridy = y;
        constraints.anchor = GridBagConstraints.WEST;
        formPanel.add(domainLabel, constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        formPanel.add(domainField, constraints);

        return formPanel;
    }


    @Override
    protected Action @NotNull [] createActions() {
        // 创建对话框的动作按钮
        return new Action[]{
                new DialogWrapperAction("确认") {
                    @Override
                    protected void doAction(ActionEvent e) {
                        WpsPluginSetting.getInstance().getState().domain = domainField.getText();
                        close(DialogWrapper.OK_EXIT_CODE);
                    }
                },
                getCancelAction()
        };
    }
}
