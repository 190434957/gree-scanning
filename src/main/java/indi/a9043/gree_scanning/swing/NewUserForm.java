package indi.a9043.gree_scanning.swing;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import indi.a9043.gree_scanning.pojo.GreeUser;
import indi.a9043.gree_scanning.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

@Component
public class NewUserForm {
    private JTextField usrIdField;
    private JTextField usrNameField;
    private JButton newUserButton;
    private JTextField initPwdField;
    private JPanel mainPanel;
    private JCheckBox oneCheckBox;
    private JCheckBox fourCheckBox;
    private JCheckBox twoCheckBox;
    private JDialog jDialog;
    private UserManager userManager;

    @Autowired
    public NewUserForm(final LoginService loginService) {
        initPwdField.setText("123456");
        newUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (usrIdField.getText() == null || usrIdField.getText().equals("")) {
                    JOptionPane.showMessageDialog(mainPanel, "用户名不能为空! ", "Warn", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (usrIdField.getText().length() <= 3) {
                    JOptionPane.showMessageDialog(mainPanel, "用户名长度不能小于3! ", "Warn", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (usrNameField.getText() == null || usrNameField.getText().equals("")) {
                    JOptionPane.showMessageDialog(mainPanel, "昵称不能为空! ", "Warn", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (usrIdField.getText().length() <= 3) {
                    JOptionPane.showMessageDialog(mainPanel, "昵称长度不能小于2! ", "Warn", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!oneCheckBox.isSelected() && !twoCheckBox.isSelected() && !fourCheckBox.isSelected()) {
                    JOptionPane.showMessageDialog(mainPanel, "权限不能为空! ", "Warn", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                byte power = 0;
                if (oneCheckBox.isSelected()) {
                    power += 1;
                }
                if (twoCheckBox.isSelected()) {
                    power += 2;
                }
                if (fourCheckBox.isSelected()) {
                    power += 4;
                }
                final GreeUser greeUser = new GreeUser();
                greeUser.setUsrId(usrIdField.getText());
                greeUser.setUsrName(usrNameField.getText());
                greeUser.setUsrPwd(initPwdField.getText());
                greeUser.setUsrPower(power);
                final InfiniteProgressPanel infiniteProgressPanel = new InfiniteProgressPanel();
                if (mainPanel.getRootPane() != null) {
                    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                    infiniteProgressPanel.setBounds(100, 100, (dimension.width) / 2, (dimension.height) / 2);
                    mainPanel.getRootPane().setGlassPane(infiniteProgressPanel);
                    mainPanel.getRootPane().validate();
                    mainPanel.getRootPane().setVisible(true);
                    infiniteProgressPanel.start();
                }
                SwingWorker<Boolean, Void> swingWorker = new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() throws Exception {
                        return loginService.addGreeUser(greeUser);
                    }

                    @Override
                    protected void done() {
                        infiniteProgressPanel.stop();
                        try {
                            Boolean res = get();
                            if (!res) {
                                JOptionPane.showMessageDialog(mainPanel, "用户名已存在! ", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            } else {
                                JOptionPane.showMessageDialog(mainPanel, "新建成功! ", "Success", JOptionPane.PLAIN_MESSAGE);
                                userManager.show();
                                jDialog.setVisible(false);
                                jDialog.dispose();
                                oneCheckBox.setSelected(false);
                                twoCheckBox.setSelected(false);
                                fourCheckBox.setSelected(false);
                                usrIdField.setText("");
                                usrNameField.setText("");
                                initPwdField.setText("123456");
                            }
                        } catch (InterruptedException | ExecutionException e1) {
                            JOptionPane.showMessageDialog(mainPanel, "未知错误! ", "Error", JOptionPane.ERROR_MESSAGE);
                            e1.printStackTrace();
                        }
                        super.done();
                    }
                };
                swingWorker.execute();
            }
        });
    }

    JPanel getMainPanel(JDialog jDialog, UserManager userManager) {
        this.userManager = userManager;
        this.jDialog = jDialog;
        return mainPanel;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(11, 4, new Insets(20, 20, 20, 20), 5, 20));
        final JLabel label1 = new JLabel();
        label1.setText("用户名");
        mainPanel.add(label1, new GridConstraints(1, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(10, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        usrIdField = new JTextField();
        mainPanel.add(usrIdField, new GridConstraints(2, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("昵称");
        mainPanel.add(label2, new GridConstraints(3, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        usrNameField = new JTextField();
        mainPanel.add(usrNameField, new GridConstraints(4, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("初始密码");
        mainPanel.add(label3, new GridConstraints(7, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        newUserButton = new JButton();
        newUserButton.setText("新建");
        mainPanel.add(newUserButton, new GridConstraints(9, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 30), null, 0, false));
        initPwdField = new JTextField();
        mainPanel.add(initPwdField, new GridConstraints(8, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(9, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        mainPanel.add(spacer3, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("初始权限");
        mainPanel.add(label4, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        oneCheckBox = new JCheckBox();
        oneCheckBox.setText("查");
        mainPanel.add(oneCheckBox, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fourCheckBox = new JCheckBox();
        fourCheckBox.setText("删");
        mainPanel.add(fourCheckBox, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        twoCheckBox = new JCheckBox();
        twoCheckBox.setText("增");
        mainPanel.add(twoCheckBox, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
