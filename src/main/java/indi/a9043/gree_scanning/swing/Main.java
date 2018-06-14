package indi.a9043.gree_scanning.swing;

import indi.a9043.gree_scanning.pojo.GreeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

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


    @Autowired
    public Main(View view, Insert insert) {
        this.view = view;
        this.insert = insert;
        viewButton.addActionListener(e -> showView());
        insertButton.addActionListener(e -> showInsert());

    }

    void show(GreeUser greeUser) {
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
        JFrame frame = new JFrame("单据管理 用户: " + greeUser.getUsrName());
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
}
