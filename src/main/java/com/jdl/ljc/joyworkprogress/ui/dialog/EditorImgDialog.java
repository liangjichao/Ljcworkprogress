package com.jdl.ljc.joyworkprogress.ui.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * @author liangjichao
 * @date 2023/10/18 8:26 PM
 */
public class EditorImgDialog extends DialogWrapper {
    private JBTextField urlField;

    public EditorImgDialog() {
        super(true);
        setModal(true);
        setTitle("设置图片");
        init();
        initData();
    }

    private void initData() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        if (clipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
            try {
                Transferable transferable = clipboard.getContents(null);
                if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                    Image image = (Image) transferable.getTransferData(DataFlavor.imageFlavor);

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setPreferredSize(new Dimension(400, 80));
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
        urlField.setPreferredSize(JBUI.size(200, 30));
        constraints.gridx = 0;
        constraints.gridy = y;
        constraints.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("URL："), constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        formPanel.add(urlField, constraints);

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

    public String getURLText() {
        return urlField.getText();
    }
}
