package indi.a9043.gree_scanning.swing;

import indi.a9043.gree_scanning.GreeScanningApplication;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

@Component
public class GreeTrayIcon extends TrayIcon {
    private static final Image image = Toolkit.getDefaultToolkit().getImage(GreeScanningApplication.class.getResource("/icon.gif"));
    private static boolean isSupported = SystemTray.isSupported();
    private MouseAdapter mouseAdapter;

    public GreeTrayIcon() {

        super(image, "Gree Voucher Manager");
        MenuItem menuItem = new MenuItem("退出");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "确定退出? ", "退出", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        PopupMenu popupMenu = new PopupMenu();
        popupMenu.add(menuItem);
        setImageAutoSize(true);
        setPopupMenu(popupMenu);

        if (isSupported) {
            SystemTray st = SystemTray.getSystemTray();
            try {
                st.add(this);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

    void setMouseListener(MouseAdapter newMouseAdapter) {
        this.removeMouseListener(mouseAdapter);
        mouseAdapter = newMouseAdapter;
        this.addMouseListener(newMouseAdapter);
    }
}
