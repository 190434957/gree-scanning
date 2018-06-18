package indi.a9043.gree_scanning.config;

import org.json.JSONObject;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import javax.swing.*;
import java.io.*;

/**
 * @author a9043 卢学能 zzz13129180808@gmail.com
 */
@Configuration
public class GreeDataSource {

    @Bean
    @Primary
    public DataSource getGreeDataSource() {
        File file = new File(System.getProperty("user.dir") + File.separator + "db.json");
        initFile(file);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONObject dbObj = new JSONObject(stringBuilder.toString());
            DataSourceProperties dataSourceProperties = new DataSourceProperties();
            dataSourceProperties.setUrl(String.format("jdbc:sqlserver://%s:1433;DatabaseName=guangma", dbObj.getString("db_ip")));
            dataSourceProperties.setUsername("sa");
            dataSourceProperties.setPassword("980524");
            dataSourceProperties.setDriverClassName(com.microsoft.sqlserver.jdbc.SQLServerDriver.class.getName());
            return dataSourceProperties.initializeDataSourceBuilder().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initFile(File file) {
        if (!file.exists()) {
            String ip = JOptionPane.showInputDialog(null, "数据库配置不存在, 请输入数据源IP地址", "数据源配置", JOptionPane.WARNING_MESSAGE);
            if (ip == null) {
                System.exit(0);
            }
            if (ip.matches("\\d+.\\d+.\\d+.\\d+")) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("db_ip", ip);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(jsonObject.toString().getBytes());
                    fileOutputStream.close();
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "文件错误, 请手动设置!", "Error", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                    System.exit(0);
                }
            } else {
                JOptionPane.showMessageDialog(null, "地址不合法！ ", "Error", JOptionPane.ERROR_MESSAGE);
                initFile(file);
            }
        }
    }
}
