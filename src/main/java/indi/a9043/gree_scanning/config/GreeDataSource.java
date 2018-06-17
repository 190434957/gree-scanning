package indi.a9043.gree_scanning.config;

import org.json.JSONObject;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "数据库配置不存在! ", "Error", JOptionPane.ERROR_MESSAGE);
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
            JSONObject dbObj = new JSONObject(stringBuilder.toString());
            DataSourceProperties dataSourceProperties = new DataSourceProperties();
            dataSourceProperties.setUrl(dbObj.getString("url"));
            dataSourceProperties.setUsername(dbObj.getString("username"));
            dataSourceProperties.setPassword(dbObj.getString("password"));
            dataSourceProperties.setDriverClassName(com.microsoft.sqlserver.jdbc.SQLServerDriver.class.getName());
            return dataSourceProperties.initializeDataSourceBuilder().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
