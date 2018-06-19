package indi.a9043.gree_scanning.swing;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import indi.a9043.gree_scanning.pojo.GreeUser;
import indi.a9043.gree_scanning.service.LoginService;
import indi.a9043.gree_scanning.swing.pojo.UserTableModel;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class UserManager {
    private final String initPassword = "4QrcOUm6Wau+VuBX8g+IPg==";
    private LoginService loginService;
    private JPanel mainPanel;
    private JTable manageTable;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton newUserButton;
    private List<GreeUser> greeUserList;


    @Autowired
    public UserManager(final LoginService loginService, final NewUserForm newUserForm) {
        this.loginService = loginService;
        saveButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        newUserButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectData();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ObjectOutputStream obs = null;
                try {
                    obs = new ObjectOutputStream(out);
                    obs.writeObject(greeUserList);
                    obs.close();
                    ByteArrayInputStream ios = new ByteArrayInputStream(out.toByteArray());
                    ObjectInputStream ois = new ObjectInputStream(ios);
                    //返回生成的新对象
                    final List<GreeUser> opGreeUserList = (List<GreeUser>) ois.readObject();
                    ois.close();
                    TableModel tableModel = manageTable.getModel();
                    StringBuilder msgBuilder = new StringBuilder();
                    msgBuilder.append("以下修改\n");
                    for (int i = 0; i < opGreeUserList.size(); i++) {
                        StringBuilder stringBuilder = new StringBuilder();
                        GreeUser greeUser = opGreeUserList.get(i);
                        boolean isModify = false;

                        if (tableModel.getValueAt(i, 2).equals(false) && tableModel.getValueAt(i, 3).equals(false) && tableModel.getValueAt(i, 4).equals(false)) {
                            JOptionPane.showMessageDialog(mainPanel, "权限不能为空 在第" + (i + 1) + "行", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        String newUsrName = tableModel.getValueAt(i, 1).toString();
                        if (!greeUser.getUsrName().equals(newUsrName)) {
                            isModify = true;
                            stringBuilder.append(greeUser.getUsrId()).append(": 昵称 ").append(greeUser.getUsrName()).append(" -> ").append(newUsrName).append("; ");
                            greeUser.setUsrName(newUsrName);
                        } else {
                            greeUser.setUsrName(null);
                        }

                        if (!tableModel.getValueAt(i, 2).equals((greeUser.getUsrPower() & 1) == 1)) {
                            if (isModify) {
                                stringBuilder.append("读权限 ").append((greeUser.getUsrPower() & 1) == 1).append(" -> ").append(tableModel.getValueAt(i, 2)).append("; ");
                            } else {
                                isModify = true;
                                stringBuilder.append(greeUser.getUsrId()).append(": 读权限 ").append((greeUser.getUsrPower() & 1) == 1).append(" -> ").append(tableModel.getValueAt(i, 2)).append("; ");
                            }
                            greeUser.setUsrPower((byte) (greeUser.getUsrPower() ^ (1 << (0))));
                        }

                        if (!tableModel.getValueAt(i, 3).equals((greeUser.getUsrPower() & 2) == 2)) {
                            if (isModify) {
                                stringBuilder.append("增权限 ").append((greeUser.getUsrPower() & 2) == 2).append(" -> ").append(tableModel.getValueAt(i, 3)).append("; ");
                            } else {
                                isModify = true;
                                stringBuilder.append(greeUser.getUsrId()).append(": 增权限 ").append((greeUser.getUsrPower() & 2) == 2).append(" -> ").append(tableModel.getValueAt(i, 3)).append("; ");
                            }
                            greeUser.setUsrPower((byte) (greeUser.getUsrPower() ^ (1 << (2 - 1))));
                        }

                        if (!tableModel.getValueAt(i, 4).equals((greeUser.getUsrPower() & 4) == 4)) {
                            if (isModify) {
                                stringBuilder.append("删权限 ").append((greeUser.getUsrPower() & 4) == 4).append(" -> ").append(tableModel.getValueAt(i, 4)).append("; ");
                            } else {
                                isModify = true;
                                stringBuilder.append(greeUser.getUsrId()).append(": 删权限 ").append((greeUser.getUsrPower() & 4) == 4).append(" -> ").append(tableModel.getValueAt(i, 4)).append("; ");
                            }
                            greeUser.setUsrPower((byte) (greeUser.getUsrPower() ^ (1 << (3 - 1))));
                        }

                        if (tableModel.getValueAt(i, 5).equals(Boolean.TRUE)) {
                            if (isModify) {
                                stringBuilder.append("重置密码; ");
                            } else {
                                isModify = true;
                                stringBuilder.append(greeUser.getUsrId()).append(": 重置密码; ");
                            }
                            greeUser.setUsrPwd(initPassword);
                        } else {
                            greeUser.setUsrPwd(null);
                        }

                        if (tableModel.getValueAt(i, 6).equals(Boolean.TRUE)) {
                            if (isModify) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(greeUser.getUsrId()).append(": 删除账户; ");
                            } else {
                                isModify = true;
                                stringBuilder.append(greeUser.getUsrId()).append(": 删除账户; ");
                            }
                            greeUser.setUsrPwd("");
                        } else {
                            greeUser.setUsrPwd(null);
                        }
                        msgBuilder.append(stringBuilder.append("\n"));
                        if (!isModify) {
                            opGreeUserList.set(i, null);
                        }
                    }
                    msgBuilder.append("是否保存? ");

                    if (JOptionPane.showConfirmDialog(mainPanel, msgBuilder.toString(), "提示", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                        final InfiniteProgressPanel infiniteProgressPanel = new InfiniteProgressPanel();
                        if (mainPanel.getRootPane() != null) {
                            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                            infiniteProgressPanel.setBounds(100, 100, (dimension.width) / 2, (dimension.height) / 2);
                            mainPanel.getRootPane().setGlassPane(infiniteProgressPanel);
                            mainPanel.getRootPane().validate();
                            mainPanel.getRootPane().setVisible(true);
                            infiniteProgressPanel.start();
                        }
                        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() {
                            @Override
                            protected Void doInBackground() throws Exception {
                                loginService.updateUserList(opGreeUserList);
                                return null;
                            }

                            @Override
                            protected void done() {
                                infiniteProgressPanel.stop();
                                selectData();
                                super.done();
                            }
                        };
                        swingWorker.execute();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(mainPanel, "I/O 出错!", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (ClassNotFoundException e1) {
                    JOptionPane.showMessageDialog(mainPanel, "I/O 出错!", "Error", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                }
            }
        });
        newUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JDialog dialog = new JDialog((JFrame) mainPanel.getRootPane().getParent(), "新建用户", true);
                dialog.setSize(250, 500);
                dialog.setResizable(false);
                dialog.setLocationRelativeTo(mainPanel);
                JPanel panel = new JPanel();
                panel.add(newUserForm.getMainPanel(dialog, UserManager.this));
                dialog.setContentPane(panel);
                dialog.setVisible(true);
            }
        });
    }

    JPanel getMainPanel() {
        return mainPanel;
    }

    void show() {
        selectData();
    }

    private void selectData() {
        final InfiniteProgressPanel infiniteProgressPanel = new InfiniteProgressPanel();
        if (mainPanel.getRootPane() != null) {
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            infiniteProgressPanel.setBounds(100, 100, (dimension.width) / 2, (dimension.height) / 2);
            mainPanel.getRootPane().setGlassPane(infiniteProgressPanel);
            mainPanel.getRootPane().validate();
            mainPanel.getRootPane().setVisible(true);
            infiniteProgressPanel.start();
        }

        SwingWorker<List<GreeUser>, Object> swingWorker = new SwingWorker<List<GreeUser>, Object>() {
            @Override
            protected List<GreeUser> doInBackground() throws Exception {
                return loginService.getAllUser();
            }

            @Override
            protected void done() {
                infiniteProgressPanel.stop();
                try {
                    List<GreeUser> tempGreeUserList = greeUserList = get();
                    Object[][] rows = new Object[tempGreeUserList.size()][7];
                    int idx = 0;
                    for (GreeUser greeUser : tempGreeUserList) {
                        rows[idx][0] = greeUser.getUsrId();
                        rows[idx][1] = greeUser.getUsrName();
                        rows[idx][2] = (greeUser.getUsrPower() & 1) == 1;
                        rows[idx][3] = (greeUser.getUsrPower() & 2) == 2;
                        rows[idx][4] = (greeUser.getUsrPower() & 4) == 4;
                        rows[idx][5] = Boolean.FALSE;
                        rows[idx][6] = Boolean.FALSE;
                        idx++;
                    }
                    UserTableModel userTableModel = new UserTableModel(rows);
                    manageTable.setModel(userTableModel);
                    manageTable.updateUI();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        swingWorker.execute();
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
        mainPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setMaximumSize(new Dimension(740, 610));
        mainPanel.setMinimumSize(new Dimension(740, 610));
        mainPanel.setPreferredSize(new Dimension(740, 610));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainPanel.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        manageTable = new JTable();
        scrollPane1.setViewportView(manageTable);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        saveButton = new JButton();
        saveButton.setText("保存");
        panel1.add(saveButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cancelButton = new JButton();
        cancelButton.setText("撤销/刷新");
        panel1.add(cancelButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        newUserButton = new JButton();
        newUserButton.setText("新增");
        panel1.add(newUserButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
