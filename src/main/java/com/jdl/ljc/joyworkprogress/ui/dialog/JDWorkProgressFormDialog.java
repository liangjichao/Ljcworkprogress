package com.jdl.ljc.joyworkprogress.ui.dialog;

import com.alibaba.fastjson2.util.DateUtils;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.util.ui.JBUI;
import com.jdl.ljc.joyworkprogress.domain.WpsConfig;
import com.jdl.ljc.joyworkprogress.domain.dto.ResultDto;
import com.jdl.ljc.joyworkprogress.domain.dto.WpsDto;
import com.jdl.ljc.joyworkprogress.domain.dto.WpsSaveDto;
import com.jdl.ljc.joyworkprogress.enums.WorkProgressStatusEnum;
import com.jdl.ljc.joyworkprogress.ui.JDIconButton;
import com.jdl.ljc.joyworkprogress.ui.calendar.WpsDatePicker;
import com.jdl.ljc.joyworkprogress.ui.editor.WpsMarkdownEditor;
import com.jdl.ljc.joyworkprogress.ui.panel.WorkProgressPanel;
import com.jdl.ljc.joyworkprogress.util.DateComputeUtils;
import com.jdl.ljc.joyworkprogress.util.FormUtils;
import com.jdl.ljc.joyworkprogress.util.ProjectUtils;
import com.jdl.ljc.joyworkprogress.util.RestUtils;
import icons.JoyworkprogressIcons;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.net.URI;
import java.util.Date;

public class JDWorkProgressFormDialog extends DialogWrapper {
    private static final Logger LOG = Logger.getInstance(JDWorkProgressFormDialog.class);
    private final Project project;
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
    private final WpsDto formData;
    private JCheckBox dependenceCheckBox;
    private final WorkProgressPanel panel;

    private WpsMarkdownEditor editor;

    private JPanel topPanel;

    private JPanel bottomPanel;

    private JLabel dayLabel;

    public JDWorkProgressFormDialog(@Nullable Project project, WpsDto formData, WorkProgressPanel panel) {
        super(project);
        this.project = project;
        this.formData = formData;
        this.panel = panel;
        setTitle("工作进度信息");
        setModal(false);
        init();
        setFormData(formData);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        ToolWindow wps = ToolWindowManager.getInstance(project).getToolWindow("WPS");
        int minWidth = 500;
        if (wps != null) {
            int rootWidth = wps.getComponent().getRootPane().getWidth();
            int recommendWidth = 873;
            if (rootWidth > recommendWidth) {
                minWidth = recommendWidth;
            }
        }
        rootPanel.setPreferredSize(new Dimension(minWidth, 600));
        JPanel centerPanel = getCenterPanel();
        rootPanel.add(centerPanel, BorderLayout.CENTER);
        topPanel = getTopPanel();
        rootPanel.add(topPanel, BorderLayout.NORTH);
        bottomPanel = getBottomPanel();
        rootPanel.add(bottomPanel, BorderLayout.SOUTH);
        return rootPanel;
    }

    private JPanel getBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = JBUI.insets(5);
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
        String content = "";
        if (formData != null && !StringUtil.isEmpty(formData.getDevInfo())) {
            content = formData.getDevInfo();
        }

        editor = new WpsMarkdownEditor(project, content, this);

        centerPanel.add(editor.getComponent(), BorderLayout.CENTER);
        return centerPanel;
    }

    @NotNull
    private JPanel getTopPanel() {
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = JBUI.insets(5);
        int y = 0;
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
        y = FormUtils.addRowFormUnFill(topPanel, constraints, new JLabel("进度："), progressStatusComboBox, y);

        JPanel dataPickerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        dataPickerPanel.setBorder(JBUI.Borders.empty());
        planWorkHoursPickerStart = new WpsDatePicker();
        planWorkHoursPickerEnd = new WpsDatePicker();
        planWorkHoursPickerStart.addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                calculateDays(planWorkHoursPickerStart, planWorkHoursPickerEnd);
            }
        });
        planWorkHoursPickerStart.addActionListener(e -> {
            if ("value".equals(e.getActionCommand())) {
                WpsDatePicker startDate = (WpsDatePicker) e.getSource();
                calculateDays(startDate, planWorkHoursPickerEnd);
            }
        });
        planWorkHoursPickerEnd.addActionListener(e -> {
            if ("value".equals(e.getActionCommand())) {
                WpsDatePicker endDate = (WpsDatePicker) e.getSource();
                calculateDays(planWorkHoursPickerStart, endDate);
            }
        });
        planWorkHoursPickerEnd.addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                calculateDays(planWorkHoursPickerStart, planWorkHoursPickerEnd);
            }
        });
        dataPickerPanel.add(planWorkHoursPickerStart);
        dataPickerPanel.add(new JLabel("至"));
        dataPickerPanel.add(planWorkHoursPickerEnd);
        dayLabel = new JLabel();
        dataPickerPanel.add(dayLabel);
        JButton dayCopyBtn = createDayCopyButton();
        dataPickerPanel.add(dayCopyBtn);

        y = FormUtils.addRowForm(topPanel, constraints, new JLabel("计划工时："), dataPickerPanel, y);

        JLabel titleLabel = new JLabel("<html>项目名称：<font color=\"red\">*</></html>");
        projectNameField = new JTextField();
        y = FormUtils.addRowForm(topPanel, constraints, titleLabel, projectNameField, y);

        prdField = new JTextField();
        JButton prdLinkBtn = new JButton(AllIcons.Ide.Link);
        prdLinkBtn.setPreferredSize(new Dimension(30, 30));
        prdLinkBtn.addActionListener(e -> {
            String url = prdField.getText();
            openBrowserLink(url);
        });
        JPanel prdPanel = new JPanel(new BorderLayout());
        prdPanel.add(prdField, BorderLayout.CENTER);
        prdPanel.add(prdLinkBtn, BorderLayout.EAST);
        y = FormUtils.addRowForm(topPanel, constraints, new JLabel("PRD："), prdPanel, y);
        productField = new JTextField();
        y = FormUtils.addRowForm(topPanel, constraints, new JLabel("产品经理："), productField, y);
        devBranchField = new JTextField();
        JPanel devBranchPanel = new JPanel(new BorderLayout());
        devBranchPanel.add(devBranchField, BorderLayout.CENTER);
        JButton getBranchNameBtn = new JButton(AllIcons.Actions.Checked);
        getBranchNameBtn.setPreferredSize(new Dimension(30, 30));
        getBranchNameBtn.addActionListener(e -> {
            if (project != null) {
                devBranchField.setText(ProjectUtils.getCurrentBranchName(project));
            }
        });
        devBranchPanel.add(getBranchNameBtn, BorderLayout.EAST);
        y = FormUtils.addRowForm(topPanel, constraints, new JLabel("开发分支："), devBranchPanel, y);
        appVersionField = new JTextField();
        y = FormUtils.addRowForm(topPanel, constraints, new JLabel("应用版本："), appVersionField, y);
        cardField = new JTextField();
        JButton openLinkBtn = new JButton(AllIcons.Ide.Link);
        openLinkBtn.setPreferredSize(new Dimension(30, 30));
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.add(cardField, BorderLayout.CENTER);
        cardPanel.add(openLinkBtn, BorderLayout.EAST);
        openLinkBtn.addActionListener(e -> {
            String url = cardField.getText();
            openBrowserLink(url);
        });
        y = FormUtils.addRowForm(topPanel, constraints, new JLabel("卡片："), cardPanel, y);
        devOwnerField = new JTextField();
        FormUtils.addRowForm(topPanel, constraints, new JLabel("开发人员："), devOwnerField, y);
        return topPanel;
    }

    @NotNull
    private JButton createDayCopyButton() {
        JButton dayCopyBtn = new JDIconButton(JoyworkprogressIcons.COPY);
        dayCopyBtn.addActionListener(e -> {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            String startDate = getShortDateTime(planWorkHoursPickerStart);
            String endDate = getShortDateTime(planWorkHoursPickerEnd);
            if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
                String copyContent = String.format("%s至%s", startDate, endDate);
                StringSelection selection = new StringSelection(copyContent);
                clipboard.setContents(selection, null);
            }
        });
        return dayCopyBtn;
    }

    public void full() {
        topPanel.setVisible(!topPanel.isVisible());
        bottomPanel.setVisible(!bottomPanel.isVisible());
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
                        } else if (StringUtils.isNotBlank(endShortTime) && StringUtils.isNotBlank(startShortTime)) {
                            if (planWorkHoursPickerEnd.getDate().compareTo(planWorkHoursPickerStart.getDate()) < 0) {
                                Messages.showInfoMessage("结束日期小于开始日期", "提示");
                                return;
                            }
                        }
                        WpsSaveDto dto = getFormData(projectName);
                        String requestPath = "/wps/insert";
                        if (formData != null && formData.getId() != null && formData.getId() > 0) {
                            requestPath = "/wps/update";
                        }
                        ResultDto<String> resultDto = RestUtils.post(String.class, requestPath, dto);
                        if (resultDto.isSuccess()) {
                            panel.refreshTableData();
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
        String editorContent = editor.getText();
        WpsSaveDto dto = new WpsSaveDto();
        if (formData != null) {
            dto.setId(formData.getId());
        }
        Object selectedItem = progressStatusComboBox.getSelectedItem();
        if (selectedItem != null) {
            WorkProgressStatusEnum statusEnum = (WorkProgressStatusEnum) selectedItem;
            dto.setProgressStatus(statusEnum.getCode());
        }
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
        if (date == null) {
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
                LOG.error("设置计划开始工时失败", e);
            }
        }
        if (!StringUtil.isEmpty(wpsDto.getPlanEndTime())) {
            Date endDate = DateUtils.parseDate(wpsDto.getPlanEndTime(), WpsConfig.dateFormatPattern);
            try {
                planWorkHoursPickerEnd.setDate(endDate);
            } catch (PropertyVetoException e) {
                LOG.error("设置计划结束工时失败", e);
            }
        }
        productField.setText(wpsDto.getProductManager());
        prdField.setText(wpsDto.getPrd());
        cardField.setText(wpsDto.getCard());
        devBranchField.setText(wpsDto.getDevBranchName());
        appVersionField.setText(wpsDto.getAppVersion());
        devOwnerField.setText(wpsDto.getUserCode());
        dependenceCheckBox.setSelected((wpsDto.getForcedDependency() != null && wpsDto.getForcedDependency() == 1));

        calculateDays(planWorkHoursPickerStart, planWorkHoursPickerEnd);
    }

    private void calculateDays(WpsDatePicker startPicker, WpsDatePicker endPicker) {
        String start = getShortDateTime(startPicker);
        String end = getShortDateTime(endPicker);
        if (StringUtils.isNotBlank(start) && StringUtils.isNotBlank(end)) {
            dayLabel.setText(DateComputeUtils.calculateWorkingDays(start, end) + "天");
        }
    }

    @Override
    protected void doOKAction() {
        //super.doOKAction();
    }
}
