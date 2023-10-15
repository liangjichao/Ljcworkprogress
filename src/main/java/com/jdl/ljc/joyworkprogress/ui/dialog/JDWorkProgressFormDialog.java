package com.jdl.ljc.joyworkprogress.ui.dialog;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.wm.ToolWindowManager;
import com.jdl.ljc.joyworkprogress.domain.WpsConfig;
import com.jdl.ljc.joyworkprogress.domain.dto.ResultDto;
import com.jdl.ljc.joyworkprogress.domain.dto.WpsDto;
import com.jdl.ljc.joyworkprogress.domain.vo.WpsQueryDto;
import com.jdl.ljc.joyworkprogress.enums.WorkProgressStatusEnum;
import com.jdl.ljc.joyworkprogress.ui.panel.ProgressHtmlPanel;
import com.jdl.ljc.joyworkprogress.util.RestUtils;
import com.jdl.ljc.joyworkprogress.util.StringUtils;
import org.jdesktop.swingx.JXDatePicker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class JDWorkProgressFormDialog extends DialogWrapper {
    private Project project;
    private ProgressHtmlPanel editorPane;

    private ComboBox<WorkProgressStatusEnum> progressStatusComboBox;
    private JXDatePicker planWorkHoursPickerStart;
    private JXDatePicker planWorkHoursPickerEnd;
    private JTextField projectNameField;
    private JTextField prdField;
    private JTextField productField;
    private JTextField devBranchField;
    private JTextField appVersionField;
    private JTextField cardField;

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

        JPanel centerPanel = getCenterPanel(rootPanel);
        rootPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel topPanel = getTopPanel();
        rootPanel.add(topPanel, BorderLayout.NORTH);

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
        editorPane = new ProgressHtmlPanel("");
        centerPanel.add(editorPane.getComponent(), BorderLayout.CENTER);

        return centerPanel;
    }

    @NotNull
    private JPanel getTopPanel() {
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        int y=0;

        JLabel progressLabel = new JLabel("进度：");
        //为状态添加下拉框编辑器
        progressStatusComboBox = new ComboBox<>();
        for (WorkProgressStatusEnum value : WorkProgressStatusEnum.values()) {
            progressStatusComboBox.addItem(value);
        }
        constraints.gridx = 0;
        constraints.gridy = y;
        constraints.anchor = GridBagConstraints.WEST;
        topPanel.add(progressLabel, constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        topPanel.add(progressStatusComboBox, constraints);

        y++;
        JLabel planWorkHoursLabel = new JLabel("计划工时：");
        JPanel dataPickerPanel = new JPanel();
        planWorkHoursPickerStart = new JXDatePicker();
        planWorkHoursPickerStart.setFormats("yyyy.MM.dd");
        planWorkHoursPickerEnd = new JXDatePicker();
        planWorkHoursPickerEnd.setFormats("yyyy.MM.dd");
        UIManager.put("JXDatePicker.todayButtonText", "今日");
        dataPickerPanel.add(planWorkHoursPickerStart);
        dataPickerPanel.add(new JLabel("至"));
        dataPickerPanel.add(planWorkHoursPickerEnd);
        constraints.gridx = 0;
        constraints.gridy = y;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        topPanel.add(planWorkHoursLabel, constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        topPanel.add(dataPickerPanel, constraints);

        y++;
        JLabel titleLabel = new JLabel("项目名称：");
        projectNameField = new JTextField();
        constraints.gridx = 0;
        constraints.gridy = y;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        topPanel.add(titleLabel, constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        topPanel.add(projectNameField, constraints);

        y++;
        JLabel prdLabel = new JLabel("PRD：");
        prdField = new JTextField();
        constraints.gridx = 0;
        constraints.gridy = y;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        topPanel.add(prdLabel, constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        topPanel.add(prdField, constraints);

        y++;
        JLabel productLabel = new JLabel("产品：");
        productField = new JTextField();
        constraints.gridx = 0;
        constraints.gridy = y;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        topPanel.add(productLabel, constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        topPanel.add(productField, constraints);


        y++;
        JLabel devBranchLabel = new JLabel("开发分支：");
        devBranchField = new JTextField();
        constraints.gridx = 0;
        constraints.gridy = y;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        topPanel.add(devBranchLabel, constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        topPanel.add(devBranchField, constraints);

        y++;
        JLabel appVersionLabel = new JLabel("应用版本：");
        appVersionField = new JTextField();
        constraints.gridx = 0;
        constraints.gridy = y;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        topPanel.add(appVersionLabel, constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        topPanel.add(appVersionField, constraints);

        y++;
        JLabel cardLabel = new JLabel("卡片：");
        cardField = new JTextField();
        JButton openLinkBtn = new JButton(AllIcons.Ide.Link);
        openLinkBtn.setPreferredSize(new Dimension(30,30));
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.add(cardField,BorderLayout.CENTER);
        cardPanel.add(openLinkBtn,BorderLayout.EAST);
        openLinkBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(cardField.getText());
            }
        });

        constraints.gridx = 0;
        constraints.gridy = y;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        topPanel.add(cardLabel, constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        topPanel.add(cardPanel, constraints);



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
                        String projectName = projectNameField.getText();
                        if (StringUtil.isEmpty(projectName)) {
                            Messages.showInfoMessage("请输入项目名称!", "提示");
                            return;
                        }
                        String editorContent = editorPane.getEditorContent();
                        Object selectedItem = progressStatusComboBox.getSelectedItem();
                        WorkProgressStatusEnum statusEnum=(WorkProgressStatusEnum)selectedItem;

                        WpsDto dto = getFormData(projectName, editorContent, statusEnum);
                        ResultDto<String> resultDto = RestUtils.post(String.class, "/wps/insert", dto);
                        if (resultDto.isSuccess()) {
                            close(DialogWrapper.OK_EXIT_CODE);
                        }else{
                            Messages.showInfoMessage(resultDto.getResultMessage(), "保存失败");
                        }
                    }
                },
                getCancelAction()
        };
    }

    @NotNull
    private WpsDto getFormData(String projectName, String editorContent, WorkProgressStatusEnum statusEnum) {
        WpsDto dto = new WpsDto();
        dto.setProgressStatus(statusEnum.getCode());
        dto.setPlanStartTime(planWorkHoursPickerStart.getEditor().getText()+" 00:00:00");
        dto.setPlanEndTime(planWorkHoursPickerEnd.getEditor().getText()+" 23:59:59");
        dto.setDevInfo(editorContent);
        dto.setProjectName(projectName);
        dto.setPrd(prdField.getText());
        dto.setProductManager(productField.getText());
        dto.setDevBranchName(devBranchField.getText());
        dto.setAppVersion(appVersionField.getText());
        dto.setCardUrl(cardField.getText());
        dto.setUserCode(WpsConfig.getInstance().getCurrentUserCode());
        return dto;
    }

    @Override
    protected void doOKAction() {

        //super.doOKAction();
    }
}
