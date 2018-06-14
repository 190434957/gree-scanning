package indi.a9043.gree_scanning.swing;

import indi.a9043.gree_scanning.pojo.GreeScanning;
import indi.a9043.gree_scanning.pojo.GreeUser;
import indi.a9043.gree_scanning.service.DataService;
import indi.a9043.gree_scanning.swing.pojo.GreeTableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author a9043 卢学能 zzz13129180808@gmail.com
 */
@Component
public class Insert {
    private JPanel insertPanel;
    private JTable table1;
    private JPanel fileText;
    private JButton selectButton;
    private JTextField textField;
    private JButton insertButton;
    private File file;
    private Object[][] rows;

    @Autowired
    public Insert(DataService dataService) {
        textField.setEditable(false);
        selectButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jFileChooser.showOpenDialog(insertPanel);
            file = jFileChooser.getSelectedFile();
            textField.setText(file.getAbsolutePath());
            readFile();
        });
        insertButton.addActionListener(e -> {
            if (rows != null && rows.length > 0) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                List<GreeScanning> greeScanningList = new ArrayList<>();
                Arrays.stream(rows).forEach(row -> {
                    GreeScanning greeScanning = new GreeScanning();
                    greeScanning.setVoucher(Integer.valueOf(row[0].toString()));
                    greeScanning.setBarcode(row[1].toString());
                    greeScanning.setDateTime(Date.from(LocalDateTime.parse(row[2].toString(), dateTimeFormatter).atZone(ZoneId.systemDefault()).toInstant()));
                    greeScanningList.add(greeScanning);
                });
                int[] res = dataService.addNewData(greeScanningList);
                JOptionPane.showMessageDialog(insertPanel,
                        "总" +
                                greeScanningList.size() +
                                "条\n重复" +
                                res[0] +
                                "条\n成功插入" +
                                res[1]
                                + "条! ",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(insertPanel, "文件无数据! ", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    JPanel getInsertPanel(GreeUser greeUser) {
        GreeUser greeUser1 = greeUser;
        return insertPanel;
    }

    private void readFile() {
        try {
            List<String[]> rowList = new ArrayList<>();
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            Pattern pattern = Pattern.compile("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}");
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] strings = line.split(",");
                for (int i = 0; i < strings.length; i++) {
                    strings[i] = strings[i].trim();
                }
                if (strings.length == 3) {
                    Matcher matcher = pattern.matcher(strings[2]);
                    if (matcher.matches()) {
                        rowList.add(strings);
                    }
                }
            }
            rows = new Object[rowList.size()][3];
            for (int i = 0; i < rowList.size(); i++) {
                rows[i] = rowList.get(i);
            }
            GreeTableModel greeTableModel = new GreeTableModel(rows, Integer.valueOf(1).byteValue());
            table1.setModel(greeTableModel);
            table1.updateUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
