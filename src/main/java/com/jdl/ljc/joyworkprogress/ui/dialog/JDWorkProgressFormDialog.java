package com.jdl.ljc.joyworkprogress.ui.dialog;

import com.google.common.base.Joiner;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.wm.ToolWindowManager;
import com.jdl.ljc.joyworkprogress.ui.panel.ProgressHtmlPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLOutput;

public class JDWorkProgressFormDialog extends DialogWrapper {
    private Project project;
    private ProgressHtmlPanel editorPane;

    public JDWorkProgressFormDialog(@Nullable Project project) {
        super(project);
        this.project = project;
        init();
        setTitle("工作进度信息");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {

        JPanel rootPanel = new JPanel(new BorderLayout());
        int rootWidth = ToolWindowManager.getInstance(project).getToolWindow("DevWorkProgress").getComponent().getRootPane().getWidth();
        int minWidth = 500;
        int recommondWidth = 1173;
        if (rootWidth > recommondWidth) {
            minWidth = recommondWidth;
        }
        rootPanel.setPreferredSize(new Dimension(minWidth, 600));

        JPanel topPanel = getTopPanel();
        rootPanel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = getCenterPanel(rootPanel);
        rootPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = getBottomPanel();
        rootPanel.add(bottomPanel, BorderLayout.SOUTH);


        return rootPanel;
    }

    private JPanel getBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        JCheckBox dependenceServerCheckBox = new JCheckBox("依赖后端", true);
        dependenceServerCheckBox.setHorizontalAlignment(SwingConstants.LEFT);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        bottomPanel.add(dependenceServerCheckBox, constraints);

        return bottomPanel;
    }

    private JPanel getCenterPanel(JPanel rootPanel) {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JLabel("开发明细："), BorderLayout.NORTH);
        editorPane = new ProgressHtmlPanel("Hello");
        centerPanel.add(editorPane.getComponent(), BorderLayout.CENTER);

        return centerPanel;
    }

    @NotNull
    private JPanel getTopPanel() {
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel nameLabel = new JLabel("标题：");
        JTextField nameField = new JTextField(10);

        nameField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // 处理键盘按键被按下的事件
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // 防止回车键提交
                    e.consume();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        JLabel prdLabel = new JLabel("PRD：");
        JTextField prdField = new JTextField(10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        topPanel.add(nameLabel, constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        topPanel.add(nameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        topPanel.add(prdLabel, constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        topPanel.add(prdField, constraints);
        return topPanel;
    }

    @Override
    protected void createDefaultActions() {
        super.createDefaultActions();
        getOKAction().putValue(Action.NAME, "保存");
        getCancelAction().putValue(Action.NAME, "取消");


    }

    @Override
    protected Action @NotNull [] createActions() {
        // 创建对话框的动作按钮
        return new Action[]{
                new DialogWrapperAction("确认") {
                    @Override
                    protected void doAction(ActionEvent e) {
                        String editorContent = editorPane.getEditorContent();
                        System.out.println(editorContent);
                        close(DialogWrapper.OK_EXIT_CODE);
                    }

                },
                getCancelAction()
        };
    }

    @Override
    protected void doOKAction() {

        //super.doOKAction();
    }
}
