package com.jdl.ljc.joyworkprogress.ui.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBUI;
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
public class EditorLinkDialog extends DialogWrapper {
    private JBTextField titleField;
    private JBTextField urlField;

    public EditorLinkDialog() {
        super(true);
        setModal(true);
        setTitle("设置链接");
        init();
        initData();
    }

    private void initData() {

    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setPreferredSize(new Dimension(400, 100));
        JPanel topPanel = getFormPanel();
        rootPanel.add(topPanel, BorderLayout.CENTER);
        return rootPanel;
    }

    private JPanel getFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        int y = 0;
        urlField = new JBTextField();
        urlField.setPreferredSize(JBUI.size(200,30));
        constraints.gridx = 0;
        constraints.gridy = y;
        constraints.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("URL："), constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        formPanel.add(urlField, constraints);

        y++;
        titleField = new JBTextField();
        constraints.gridx = 0;
        constraints.gridy = y;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        formPanel.add(new JLabel("标题："), constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        formPanel.add(titleField, constraints);
        return formPanel;
    }


    @Override
    protected Action @NotNull [] createActions() {
        // 创建对话框的动作按钮
        return new Action[]{
                new DialogWrapperAction("确认") {
                    @Override
                    protected void doAction(ActionEvent e) {
                        close(DialogWrapper.OK_EXIT_CODE);
                    }
                },
                getCancelAction()
        };
    }

    public String getTitleText(){
        return titleField.getText();
    }

    public String getURLText(){
        return urlField.getText();
    }
}
