package indi.a9043.gree_scanning;

import indi.a9043.gree_scanning.swing.Login;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.swing.*;
import java.awt.*;

/**
 * @author a9043 卢学能 zzz13129180808@gmail.com
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableTransactionManagement
@MapperScan("indi.a9043.gree_scanning.mapper")
public class GreeScanningApplication {
    private static Logger logger = LoggerFactory.getLogger(GreeScanningApplication.class);
    private static String[] DEFAULT_FONT = new String[]{
            "Table.font"
            , "TableHeader.font"
            , "CheckBox.font"
            , "Tree.font"
            , "Viewport.font"
            , "ProgressBar.font"
            , "RadioButtonMenuItem.font"
            , "ToolBar.font"
            , "ColorChooser.font"
            , "ToggleButton.font"
            , "Panel.font"
            , "TextArea.font"
            , "Menu.font"
            , "TableHeader.font"
            , "TextField.font"
            , "OptionPane.font"
            , "MenuBar.font"
            , "Button.font"
            , "Label.font"
            , "PasswordField.font"
            , "ScrollPane.font"
            , "MenuItem.font"
            , "ToolTip.font"
            , "List.font"
            , "EditorPane.font"
            , "Table.font"
            , "TabbedPane.font"
            , "RadioButton.font"
            , "CheckBoxMenuItem.font"
            , "TextPane.font"
            , "PopupMenu.font"
            , "TitledBorder.font"
            , "ComboBox.font"
    };

    @Autowired
    public GreeScanningApplication(Login login) {
        login.show();
        logger.info(System.getProperty("user.dir"));
    }

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        try {
            for (int i = 0; i < DEFAULT_FONT.length; i++)
                UIManager.put(DEFAULT_FONT[i], new Font("黑体", Font.PLAIN, 16));
            UIManager.put("RootPane.setupButtonVisible", false);
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencySmallShadow;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SpringApplication.run(GreeScanningApplication.class, args);
    }
}
