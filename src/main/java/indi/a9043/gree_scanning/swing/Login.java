package indi.a9043.gree_scanning.swing;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import indi.a9043.gree_scanning.pojo.GreeUser;
import indi.a9043.gree_scanning.service.LoginService;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ExecutionException;

/**
 * @author a9043 卢学能 zzz13129180808@gmail.com
 */
@Component
public class Login {
    private JFrame jFrame;
    private JPanel login;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private DataSourceSetting dataSourceSetting;
    private GreeTrayIcon greeTrayIcon;

    @Autowired
    public Login(final LoginService loginService, final Main main, DataSourceSetting dataSourceSetting, GreeTrayIcon greeTrayIcon1) {
        this.dataSourceSetting = dataSourceSetting;
        this.greeTrayIcon = greeTrayIcon1;
        loginButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
        final ActionListener loginListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final ActionListener _this = this;
                loginButton.removeActionListener(_this);
                final InfiniteProgressPanel infiniteProgressPanel = new InfiniteProgressPanel();
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                infiniteProgressPanel.setBounds(100, 100, (dimension.width) / 2, (dimension.height) / 2);
                jFrame.setGlassPane(infiniteProgressPanel);
                jFrame.validate();
                jFrame.setVisible(true);
                infiniteProgressPanel.start();
                SwingWorker swingWorker = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        try {
                            GreeUser greeUser = new GreeUser();
                            Login.this.getData(greeUser);
                            return loginService.doLogin(greeUser);
                        } catch (CannotGetJdbcConnectionException ee) {
                            ee.printStackTrace();
                            JOptionPane.showMessageDialog(login, "获取数据源失败, 请联系管理员! ", "Error", JOptionPane.ERROR_MESSAGE);
                            infiniteProgressPanel.stop();
                            initDb();
                            System.exit(0);
                            return null;
                        }
                    }

                    @Override
                    protected void done() {
                        loginButton.addActionListener(_this);
                        infiniteProgressPanel.stop();
                        GreeUser standardGreeUser = null;
                        try {
                            standardGreeUser = (GreeUser) get();
                        } catch (InterruptedException | ExecutionException e1) {
                            e1.printStackTrace();
                            JOptionPane.showMessageDialog(login, "未知错误! ", "Error", JOptionPane.ERROR_MESSAGE);
                            infiniteProgressPanel.stop();
                            initDb();
                            System.exit(0);
                        }
                        if (standardGreeUser == null) {
                            JOptionPane.showMessageDialog(login, "用户名或密码错误! ", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        Byte power = standardGreeUser.getUsrPower();
                        if ((power & 1) != 1 && (power & 2) != 2 && (power & 4) != 4) {
                            JOptionPane.showMessageDialog(login, "用户没有权限! ", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        main.show(standardGreeUser);
                        jFrame.setVisible(false);
                        super.done();
                    }
                };
                swingWorker.execute();
            }
        };
        loginButton.addActionListener(loginListener);
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n' || e.getKeyChar() == '\r') {
                    if (passwordField1.getPassword() != null && !String.valueOf(passwordField1.getPassword()).equals("")) {
                        loginButton.doClick();
                    } else {
                        JOptionPane.showMessageDialog(login, "密码不能为空! ", "Warn", JOptionPane.WARNING_MESSAGE);
                    }
                }
                super.keyTyped(e);
            }
        });
        loginButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n' || e.getKeyChar() == '\r') {
                    loginButton.doClick();
                }
                super.keyTyped(e);
            }
        });
        passwordField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n' || e.getKeyChar() == '\r') {
                    loginButton.doClick();
                }
                super.keyTyped(e);
            }
        });
    }

    public void show() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                jFrame = new JFrame("登录");
                greeTrayIcon.setMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            jFrame.setVisible(true);
                        }
                    }
                });
                jFrame.setContentPane(login);
                jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                jFrame.setResizable(false);
                jFrame.setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
                jFrame.pack();
                int windowWidth = jFrame.getWidth();
                int windowHeight = jFrame.getHeight();
                Toolkit kit = Toolkit.getDefaultToolkit();
                Dimension screenSize = kit.getScreenSize();
                int screenWidth = screenSize.width;
                int screenHeight = screenSize.height;
                jFrame.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);
                jFrame.setVisible(true);
            }
        });
    }

    public void setData(GreeUser data) {
        textField1.setText(data.getUsrId());
        passwordField1.setText(data.getUsrPwd());
    }

    private void getData(GreeUser data) {
        data.setUsrId(textField1.getText());
        data.setUsrPwd(String.valueOf(passwordField1.getPassword()));
    }

    public boolean isModified(GreeUser data) {
        if (textField1.getText() != null ? !textField1.getText().equals(data.getUsrId()) : data.getUsrId() != null)
            return true;
        if (passwordField1.getPassword() != null ? !String.valueOf(passwordField1.getPassword()).equals(data.getUsrPwd()) : data.getUsrPwd() != null)
            return true;
        return false;
    }

    private void initDb() {
        Main.DataSourceSetting(login, dataSourceSetting);
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
        login = new JPanel();
        login.setLayout(new GridLayoutManager(3, 2, new Insets(50, 50, 50, 50), 20, 20));
        login.setMinimumSize(new Dimension(640, 480));
        textField1 = new JTextField();
        login.add(textField1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        passwordField1 = new JPasswordField();
        login.add(passwordField1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        loginButton = new JButton();
        loginButton.setText("登录");
        login.add(loginButton, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("用户名");
        login.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("密码");
        login.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return login;
    }
}
