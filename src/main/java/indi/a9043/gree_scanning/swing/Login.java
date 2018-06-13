package indi.a9043.gree_scanning.swing;

import indi.a9043.gree_scanning.pojo.GreeUser;
import indi.a9043.gree_scanning.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@Component
public class Login {
    private LoginService loginService;
    private Success success;
    private JFrame jFrame;
    private JPanel login;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;

    @Autowired
    public Login(LoginService loginService, Success success) {
        loginButton.addActionListener(e -> {
            GreeUser greeUser = new GreeUser();
            getData(greeUser);
            GreeUser standardGreeUser = loginService.doLogin(greeUser);
            if(standardGreeUser == null) {
                JOptionPane.showMessageDialog(login, "用户名或密码错误! ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            success.show();
            jFrame.setVisible(false);
        });
        this.loginService = loginService;
        this.success = success;
        loginButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == '\n' || e.getKeyChar() == '\r') {
                    loginButton.doClick();
                }
                super.keyTyped(e);
            }
        });
        passwordField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == '\n' || e.getKeyChar() == '\r') {
                    loginButton.doClick();
                }
                super.keyTyped(e);
            }
        });
    }

    public void show() {
        jFrame = new JFrame("Login");
        int windowWidth = jFrame.getWidth();
        int windowHeight = jFrame.getHeight();
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        jFrame.setLocation(screenWidth/2-windowWidth/2, screenHeight/2-windowHeight/2);
        jFrame.setContentPane(login);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setVisible(true);
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
}
