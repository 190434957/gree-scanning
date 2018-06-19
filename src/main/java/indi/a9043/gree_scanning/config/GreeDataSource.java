package indi.a9043.gree_scanning.config;

import indi.a9043.gree_scanning.swing.DataSourceSetting;
import indi.a9043.gree_scanning.swing.Main;
import indi.a9043.gree_scanning.util.AESUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * @author a9043 卢学能 zzz13129180808@gmail.com
 */
@Configuration
public class GreeDataSource {
    private DataSourceSetting dataSourceSetting;

    @Autowired
    public GreeDataSource(DataSourceSetting dataSourceSetting) {
        this.dataSourceSetting = dataSourceSetting;
    }

    @Bean
    @Primary
    public DataSource getGreeDataSource() {
        File file = new File(System.getProperty("user.dir") + File.separator + "db.set");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "数据源配置不存在, 请联系管理员配置", "Warn", JOptionPane.WARNING_MESSAGE);
            initFile(file);
            System.exit(0);
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONObject dbObj = null;
            try {
                String data = AESUtil.decrypt(stringBuilder.toString());
                dbObj = new JSONObject(data);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "数据源配置损坏, 请联系管理员配置", "Warn", JOptionPane.WARNING_MESSAGE);
                initFile(file);
                e.printStackTrace();
                System.exit(0);
            }
            DataSourceProperties dataSourceProperties = new DataSourceProperties();
            dataSourceProperties.setUrl(
                    String.format("jdbc:sqlserver://%s:%d;DatabaseName=%s",
                            dbObj.getString("db_ip"),
                            dbObj.getInt("db_port"),
                            dbObj.getString("db_name")));
            dataSourceProperties.setUsername(dbObj.getString("db_username"));
            dataSourceProperties.setPassword(dbObj.getString("db_password"));
            dataSourceProperties.setDriverClassName(com.microsoft.sqlserver.jdbc.SQLServerDriver.class.getName());
            return dataSourceProperties.initializeDataSourceBuilder().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initFile(File file) {
        final JFrame jFrame = new JFrame("配置");
        final JPanel jPanel = new JPanel();
        jFrame.setContentPane(jPanel);
        jFrame.setResizable(false);
        jFrame.pack();
        int windowWidth = jFrame.getWidth();
        int windowHeight = jFrame.getHeight();
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        jFrame.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);
        jFrame.setVisible(true);
        Main.DataSourceSetting(jPanel, dataSourceSetting);
    }
}
