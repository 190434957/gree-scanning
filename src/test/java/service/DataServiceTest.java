package service;

import indi.a9043.gree_scanning.GreeScanningApplication;
import indi.a9043.gree_scanning.pojo.GreeScanning;
import indi.a9043.gree_scanning.service.DataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GreeScanningApplication.class)
public class DataServiceTest {
    @Autowired
    private DataService dataService;

    @Test
    public void addNewData() {
        for (int i = 0; i < 1000; i++) {
            GreeScanning greeScanning = new GreeScanning();
            greeScanning.setBarcode(String.valueOf(i + 1));
            greeScanning.setVoucher(i + 1);
            greeScanning.setDateTime(Date.valueOf(LocalDate.now()));
            dataService.addNewData(greeScanning);
        }
    }
}
