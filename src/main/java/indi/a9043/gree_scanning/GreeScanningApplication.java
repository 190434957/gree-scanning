package indi.a9043.gree_scanning;

import indi.a9043.gree_scanning.swing.Login;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("indi.a9043.gree_scanning.mapper")
public class GreeScanningApplication {

    @Autowired
    public GreeScanningApplication(Login login) {
        login.show();
    }

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(GreeScanningApplication.class, args);
    }
}
