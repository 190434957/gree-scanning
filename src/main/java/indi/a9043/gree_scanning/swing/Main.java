package indi.a9043.gree_scanning.swing;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import indi.a9043.gree_scanning.pojo.GreeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author a9043 卢学能 zzz13129180808@gmail.com
 */
@Component
public class Main {
    private JPanel mainPanel;
    private JButton viewButton;
    private JButton insertButton;
    private JPanel secondPanel;
    private GreeUser greeUser;
    private View view;
    private Insert insert;
    private Setting setting;
    private About about;


    @Autowired
    public Main(View view, Insert insert, Setting setting, About about) {
        this.view = view;
        this.insert = insert;
        this.setting = setting;
        this.about = about;
        $$$setupUI$$$();
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.this.showView();
            }
        });
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.this.showInsert();
            }
        });

    }

    void show(final GreeUser greeUser) {
        final JFrame frame = new JFrame("单据管理 用户: " + greeUser.getUsrName());
        this.greeUser = greeUser;
        if ((greeUser.getUsrPower() & 1) == 1 || (greeUser.getUsrPower() & 4) == 4) {
            showView();
        } else if ((greeUser.getUsrPower() & 2) == 2) {
            showInsert();
        }
        if ((greeUser.getUsrPower() & 1) != 1 && (greeUser.getUsrPower() & 4) != 4) {
            viewButton.setVisible(false);
        } else if ((greeUser.getUsrPower() & 2) != 2) {
            insertButton.setVisible(false);
        }
        JMenuItem jMenuItem1 = new JMenuItem("数据源配置");
        jMenuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = JOptionPane.showInputDialog(frame, "请输入数据源IP地址", "数据源配置", JOptionPane.WARNING_MESSAGE);
                if (ip == null) {
                    return;
                }
                if (ip.matches("\\d+.\\d+.\\d+.\\d+")) {
                    System.out.println(ip);
                } else {
                    JOptionPane.showMessageDialog(frame, "地址不合法！ ", "Error", JOptionPane.ERROR_MESSAGE);
                    this.actionPerformed(e);
                }
            }
        });
        JMenuItem jMenuItem2 = new JMenuItem("修改密码");
        jMenuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JDialog dialog = new JDialog(frame, "修改密码", true);
                dialog.setSize(250, 400);
                dialog.setResizable(false);
                dialog.setLocationRelativeTo(mainPanel);
                JPanel panel = new JPanel();
                panel.add(setting.getPanel(greeUser, dialog));
                dialog.setContentPane(panel);
                dialog.setVisible(true);
            }
        });
        JMenuItem jMenuItem3 = new JMenuItem("退出");
        jMenuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });
        JMenuItem jMenuItem4 = new JMenuItem("关于");
        jMenuItem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JDialog dialog = new JDialog(frame, "关于", true);
                dialog.setSize(530, 340);
                dialog.setResizable(false);
                dialog.setLocationRelativeTo(mainPanel);
                JPanel panel = new JPanel();
                panel.add(about.getMainPanel());
                dialog.setContentPane(panel);
                dialog.setVisible(true);
            }
        });
        JMenu jMenu1 = new JMenu("菜单");
        JMenu jMenu2 = new JMenu("帮助");
        JMenu jMenu3 = new JMenu("");
        jMenu3.setEnabled(false);
        jMenu1.add(jMenuItem1);
        jMenu1.add(jMenuItem2);
        jMenu1.add(jMenuItem3);
        jMenu2.add(jMenuItem4);
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.add(jMenu3);
        jMenuBar.add(jMenu1);
        jMenuBar.add(jMenu2);
        jMenuBar.setVisible(true);
        frame.setJMenuBar(jMenuBar);
        frame.setResizable(false);
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        int windowWidth = frame.getWidth();
        int windowHeight = frame.getHeight();
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        frame.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);
        frame.setVisible(true);
    }

    private void showView() {
        secondPanel.removeAll();
        secondPanel.add(view.getSuccess(greeUser));
        secondPanel.validate();
        secondPanel.updateUI();
    }

    private void showInsert() {
        secondPanel.removeAll();
        secondPanel.add(insert.getInsertPanel(greeUser));
        secondPanel.validate();
        secondPanel.updateUI();
    }

    private void createUIComponents() {
        secondPanel = new JPanel();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(3, 3, new Insets(10, 10, 10, 10), -1, -1));
        mainPanel.setEnabled(true);
        mainPanel.setMaximumSize(new Dimension(860, 640));
        mainPanel.setMinimumSize(new Dimension(860, 640));
        mainPanel.setName("主页面");
        mainPanel.setPreferredSize(new Dimension(860, 640));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(5, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(88, 109), null, 0, false));
        insertButton = new JButton();
        insertButton.setText("导入");
        panel1.add(insertButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        viewButton = new JButton();
        viewButton.setText("查看");
        panel1.add(viewButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mainPanel.add(secondPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
