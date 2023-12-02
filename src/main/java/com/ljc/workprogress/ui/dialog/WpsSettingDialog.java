package com.ljc.workprogress.ui.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBUI;
import com.ljc.workprogress.config.WpsPluginSetting;
import com.ljc.workprogress.domain.WpsState;
import com.ljc.workprogress.util.FormUtils;
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
        WpsState state = WpsPluginSetting.getInstance().getState();
        if (state != null) {
            domainField.setText(state.domain);
        }
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
        constraints.insets = JBUI.insets(5);
        domainField = new JBTextField();
        domainField.setPreferredSize(JBUI.size(200, 30));
        FormUtils.addRowForm(formPanel, constraints, new JLabel("域名："), domainField, 0);
        return formPanel;
    }

    @Override
    protected Action @NotNull [] createActions() {
        // 创建对话框的动作按钮
        return new Action[]{
                new DialogWrapperAction("确认") {
                    @Override
                    protected void doAction(ActionEvent e) {
                        WpsState state = WpsPluginSetting.getInstance().getState();
                        if (state != null) {
                            state.domain = domainField.getText();
                            close(DialogWrapper.OK_EXIT_CODE);
                        } else {
                            Messages.showInfoMessage("WpsState is null", "错误提示");
                        }
                    }
                },
                getCancelAction()
        };
    }
}
