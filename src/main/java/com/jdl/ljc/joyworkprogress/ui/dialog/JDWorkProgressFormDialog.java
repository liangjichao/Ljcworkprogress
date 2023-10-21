package com.jdl.ljc.joyworkprogress.ui.dialog;

import com.alibaba.fastjson2.util.DateUtils;
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
import com.jdl.ljc.joyworkprogress.domain.dto.WpsSaveDto;
import com.jdl.ljc.joyworkprogress.enums.WorkProgressStatusEnum;
import com.jdl.ljc.joyworkprogress.ui.calendar.WpsDatePicker;
import com.jdl.ljc.joyworkprogress.ui.panel.ProgressHtmlPanel;
import com.jdl.ljc.joyworkprogress.util.ProjectUtils;
import com.jdl.ljc.joyworkprogress.util.RestUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.net.URI;
import java.util.Date;

public class JDWorkProgressFormDialog extends DialogWrapper {
    private Project project;
    private ProgressHtmlPanel editorPane;
    private ComboBox<WorkProgressStatusEnum> progressStatusComboBox;
    private WpsDatePicker planWorkHoursPickerStart;
    private WpsDatePicker planWorkHoursPickerEnd;
    private JTextField projectNameField;
    private JTextField prdField;
    private JTextField productField;
    private JTextField devBranchField;
    private JTextField appVersionField;
    private JTextField cardField;
    private JTextField devOwnerField;
    private WpsDto formData;

    private JCheckBox dependenceCheckBox;

    public JDWorkProgressFormDialog(@Nullable Project project, WpsDto formData) {
        super(project);
        this.project = project;
        this.formData = formData;
        setTitle("工作进度信息");
        init();
        setFormData(formData);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {

        JPanel rootPanel = new JPanel(new BorderLayout());
        int rootWidth = ToolWindowManager.getInstance(project).getToolWindow("DevWorkProgress").getComponent().getRootPane().getWidth();
        int minWidth = 500;
        int recommondWidth = 873;
        if (rootWidth > recommondWidth) {
            minWidth = recommondWidth;
        }
        rootPanel.setPreferredSize(new Dimension(minWidth, 600));

        JPanel centerPanel = getCenterPanel();
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

        dependenceCheckBox = new JCheckBox("是否有依赖", true);
        dependenceCheckBox.setHorizontalAlignment(SwingConstants.LEFT);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        bottomPanel.add(dependenceCheckBox, constraints);

        return bottomPanel;
    }

    private JPanel getCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JLabel("开发明细："), BorderLayout.NORTH);
        String content = "";
        if (formData != null && !StringUtil.isEmpty(formData.getDevInfo())) {
            content = formData.getDevInfo();
        }
        editorPane = new ProgressHtmlPanel(content);
        centerPanel.add(editorPane.getComponent(), BorderLayout.CENTER);

        return centerPanel;
    }

    @NotNull
    private JPanel getTopPanel() {
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        int y = 0;

        JLabel progressLabel = new JLabel("进度：");
        //为状态添加下拉框编辑器
        progressStatusComboBox = new ComboBox<>();
        WorkProgressStatusEnum selectedStatus = null;
        for (WorkProgressStatusEnum value : WorkProgressStatusEnum.values()) {
            progressStatusComboBox.addItem(value);
            if (formData != null && formData.getProgressStatus() != null) {
                if (formData.getProgressStatus().equals(value.getCode())) {
                    selectedStatus = value;
                }
            }
        }
        if (selectedStatus != null) {
            progressStatusComboBox.setSelectedItem(selectedStatus);
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
        planWorkHoursPickerStart = new WpsDatePicker();
        planWorkHoursPickerEnd = new WpsDatePicker();
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
        JLabel titleLabel = new JLabel("<html>项目名称：<font color=\"red\">*</></html>");
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
        JButton prdLinkBtn = new JButton(AllIcons.Ide.Link);
        prdLinkBtn.setPreferredSize(new Dimension(30, 30));
        JPanel prdPanel = new JPanel(new BorderLayout());
        prdPanel.add(prdField, BorderLayout.CENTER);
        prdPanel.add(prdLinkBtn, BorderLayout.EAST);
        prdLinkBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = prdField.getText();
                openBrowserLink(url);
            }
        });

        constraints.gridx = 0;
        constraints.gridy = y;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        topPanel.add(prdLabel, constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        topPanel.add(prdPanel, constraints);

        y++;
        JLabel productLabel = new JLabel("产品经理：");
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
        JPanel devBranchPanel = new JPanel(new BorderLayout());
        devBranchPanel.add(devBranchField, BorderLayout.CENTER);
        JButton getBranchNameBtn = new JButton(AllIcons.Actions.Checked);
        getBranchNameBtn.setPreferredSize(new Dimension(30, 30));
        getBranchNameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                devBranchField.setText(ProjectUtils.getCurrentBranchName(project));
            }
        });
        devBranchPanel.add(getBranchNameBtn, BorderLayout.EAST);
        topPanel.add(devBranchPanel, constraints);

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
        openLinkBtn.setPreferredSize(new Dimension(30, 30));
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.add(cardField, BorderLayout.CENTER);
        cardPanel.add(openLinkBtn, BorderLayout.EAST);
        openLinkBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = cardField.getText();
                openBrowserLink(url);
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

        y++;
        JLabel devOwnerLabel = new JLabel("开发人员：");
        devOwnerField = new JTextField();
        constraints.gridx = 0;
        constraints.gridy = y;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        topPanel.add(devOwnerLabel, constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        topPanel.add(devOwnerField, constraints);

        return topPanel;
    }

    private static void openBrowserLink(String url) {
        if (StringUtils.isNotBlank(url)) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception ex) {
                Messages.showInfoMessage(String.format("打开失败：%s", ex.getMessage()), "提示");
            }
        }
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
                        String startShortTime = getShortDateTime(planWorkHoursPickerStart);
                        String endShortTime = getShortDateTime(planWorkHoursPickerEnd);
                        if (StringUtils.isNotBlank(startShortTime) && StringUtils.isBlank(endShortTime)) {
                            Messages.showInfoMessage("请完成计划工时!", "提示");
                            return;
                        } else if (StringUtils.isNotBlank(endShortTime) && StringUtils.isBlank(startShortTime)) {
                            Messages.showInfoMessage("请完成计划工时!", "提示");
                            return;
                        }else if (StringUtils.isNotBlank(endShortTime) && StringUtils.isNotBlank(startShortTime)) {
                            if (planWorkHoursPickerEnd.getDate().compareTo(planWorkHoursPickerStart.getDate()) < 0) {
                                Messages.showInfoMessage("结束日期小于开始日期", "提示");
                                return ;
                            }
                        }

                        WpsSaveDto dto = getFormData(projectName);
                        String requestPath = "/wps/insert";
                        if (formData != null && formData.getId() != null && formData.getId() > 0) {
                            requestPath = "/wps/update";
                        }
                        ResultDto<String> resultDto = RestUtils.post(String.class, requestPath, dto);
                        if (resultDto.isSuccess()) {
                            close(DialogWrapper.OK_EXIT_CODE);
                        } else {
                            Messages.showInfoMessage(resultDto.getResultMessage(), "保存失败");
                        }
                    }
                },
                getCancelAction()
        };
    }

    @NotNull
    private WpsSaveDto getFormData(String projectName) {
        String editorContent = editorPane.getEditorContent();
        Object selectedItem = progressStatusComboBox.getSelectedItem();
        WorkProgressStatusEnum statusEnum = (WorkProgressStatusEnum) selectedItem;
        WpsSaveDto dto = new WpsSaveDto();
        if (formData != null) {
            dto.setId(formData.getId());
        }
        dto.setProgressStatus(statusEnum.getCode());
        dto.setStartTime(getDateTime(planWorkHoursPickerStart, " 00:00:00"));
        dto.setEndTime(getDateTime(planWorkHoursPickerEnd, " 23:59:59"));
        dto.setDevInfo(editorContent);
        dto.setProjectName(projectName);
        dto.setPrd(prdField.getText());
        dto.setProductManager(productField.getText());
        dto.setDevBranchName(devBranchField.getText());
        dto.setAppVersion(appVersionField.getText());
        dto.setCardUrl(cardField.getText());
        dto.setUserCode(devOwnerField.getText());
        if (dependenceCheckBox.isSelected()) {
            dto.setForcedDependency(1);
        } else {
            dto.setForcedDependency(0);
        }
        return dto;
    }

    private String getDateTime(WpsDatePicker picker, String fillTime) {
        String shortDateTime = getShortDateTime(picker);
        if (StringUtils.isNoneBlank(shortDateTime)) {
            return shortDateTime + fillTime;
        }
        return null;
    }

    private static String getShortDateTime(WpsDatePicker picker) {
        Date date = picker.getDate();
        if(date==null){
            return "";
        }
        return picker.getDateFormat().format(date);
    }

    private void setFormData(WpsDto wpsDto) {
        if (wpsDto == null) {
            devOwnerField.setText(WpsConfig.getInstance().getCurrentUserCode());
            dependenceCheckBox.setSelected(false);
            return;
        }
        projectNameField.setText(wpsDto.getProjectName());
        if (!StringUtil.isEmpty(wpsDto.getPlanStartTime())) {

            Date startDate = DateUtils.parseDate(wpsDto.getPlanStartTime(), WpsConfig.dateFormatPattern);
            try {
                planWorkHoursPickerStart.setDate(startDate);
            } catch (PropertyVetoException e) {

            }
        }
        if (!StringUtil.isEmpty(wpsDto.getPlanEndTime())) {
            Date endDate = DateUtils.parseDate(wpsDto.getPlanEndTime(), WpsConfig.dateFormatPattern);
            try {
                planWorkHoursPickerEnd.setDate(endDate);
            } catch (PropertyVetoException e) {

            }
        }
        productField.setText(wpsDto.getProductManager());
        prdField.setText(wpsDto.getPrd());
        cardField.setText(wpsDto.getCard());
        devBranchField.setText(wpsDto.getDevBranchName());
        appVersionField.setText(wpsDto.getAppVersion());
        devOwnerField.setText(wpsDto.getUserCode());
        dependenceCheckBox.setSelected((wpsDto.getForcedDependency()!=null&&wpsDto.getForcedDependency()==1));
    }

    @Override
    protected void doOKAction() {

        //super.doOKAction();
    }
}
