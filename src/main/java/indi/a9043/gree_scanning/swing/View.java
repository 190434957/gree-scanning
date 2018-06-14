package indi.a9043.gree_scanning.swing;

import indi.a9043.gree_scanning.pojo.GreeScanning;
import indi.a9043.gree_scanning.pojo.GreeUser;
import indi.a9043.gree_scanning.service.DataService;
import indi.a9043.gree_scanning.swing.pojo.GreeTableModel;
import indi.a9043.gree_scanning.swing.pojo.SearchData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class View {
    private String nowDateStr;
    private DataService dataService;
    private JPanel viewPanel;
    private JTextField endDate;
    private JTextField startDate;
    private JTextField voucher;
    private JTextField barcode;
    private JTable table1;
    private JButton selectButton;
    private JButton deleteButton;
    private SearchData searchData;
    private GreeUser greeUser;

    @Autowired
    public View(DataService dataService) {
        this.dataService = dataService;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime localDateTime = LocalDateTime.now();
        nowDateStr = dateTimeFormatter.format(localDateTime);
        startDate.setText(nowDateStr);
        endDate.setText(nowDateStr);

        searchData = new SearchData();
        startDate.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char t = e.getKeyChar();
                if ((t >= '0' && t <= '9')) {
                    super.keyTyped(e);
                } else if (t == '-') {
                    super.keyTyped(e);
                } else {
                    e.consume();
                }
                if (startDate.getText().length() >= 10) {
                    e.consume();
                    startDate.setText(startDate.getText().substring(0, 10));
                }
            }
        });
        selectButton.addActionListener(e -> {
            if ((greeUser.getUsrPower() & 1) == 1 || (greeUser.getUsrPower() & 4) == 4) {
                selectData();
            } else {
                JOptionPane.showMessageDialog(viewPanel, "你没有查询权限! ", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        startDate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (startDate.getText().equals("nowDateStr")) {
                    startDate.setSelectionStart(0);
                    startDate.setSelectionEnd(startDate.getText().length());
                }
                super.mouseClicked(e);
            }
        });
        endDate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (endDate.getText().equals(nowDateStr)) {
                    endDate.setSelectionStart(0);
                    endDate.setSelectionEnd(endDate.getText().length());
                }
                super.mouseClicked(e);
            }
        });
        deleteButton.addActionListener(e -> {
            int rowCount = table1.getModel().getRowCount();
            List<String> voucherList = new ArrayList<>();
            for (int i = 0; i < rowCount; i++) {
                if (table1.getModel().getValueAt(i, 3).equals(Boolean.TRUE)) {
                    voucherList.add(table1.getModel().getValueAt(i, 0).toString());
                }
            }
            dataService.deleteGreeScanning(voucherList);
            selectData();
        });
    }

    JPanel getSuccess(GreeUser greeUser) {
        this.greeUser = greeUser;
        if ((greeUser.getUsrPower() & 4) != 4) {
            deleteButton.setVisible(false);
        }
        selectData();
        return viewPanel;
    }

    public void setData(SearchData data) {
        startDate.setText(data.getStartDate());
        endDate.setText(data.getEndDate());
        voucher.setText(data.getVoucher());
        barcode.setText(data.getBarcode());
    }

    private void getData(SearchData data) {
        data.setStartDate(startDate.getText());
        data.setEndDate(endDate.getText());
        data.setVoucher(voucher.getText());
        data.setBarcode(barcode.getText());
    }

    public boolean isModified(SearchData data) {
        if (startDate.getText() != null ? !startDate.getText().equals(data.getStartDate()) : data.getStartDate() != null)
            return true;
        if (endDate.getText() != null ? !endDate.getText().equals(data.getEndDate()) : data.getEndDate() != null)
            return true;
        if (voucher.getText() != null ? !voucher.getText().equals(data.getVoucher()) : data.getVoucher() != null)
            return true;
        if (barcode.getText() != null ? !barcode.getText().equals(data.getBarcode()) : data.getBarcode() != null)
            return true;
        return false;
    }

    private void selectData() {
        getData(searchData);
        List<Integer> startDateInt = Arrays
                .stream(searchData.getStartDate().split("-"))
                .filter(s -> !s.equals(""))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        List<Integer> endDateInt = Arrays
                .stream(searchData.getEndDate().split("-"))
                .filter(s -> !s.equals(""))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        LocalDate startDate = Optional
                .ofNullable(startDateInt)
                .filter(sdt -> sdt.size() == 3)
                .map(sdt -> LocalDate.of(sdt.get(0), sdt.get(1), sdt.get(2)))
                .orElse(null);
        LocalDate endDate = Optional
                .ofNullable(endDateInt)
                .filter(edt -> edt.size() == 3)
                .map(edt -> LocalDate.of(edt.get(0), edt.get(1), edt.get(2)))
                .orElse(null);
        List<GreeScanning> greeScanningList = dataService.selectGreeScanning(searchData.getVoucher(), searchData.getBarcode(), startDate, endDate);
        Object[][] rows;
        if ((greeUser.getUsrPower() & 4) == 4) {
            rows = new Object[greeScanningList.size()][4];
            int idx = 0;
            for (GreeScanning greeScanning : greeScanningList) {
                rows[idx][0] = greeScanning.getVoucher().toString();
                rows[idx][1] = greeScanning.getBarcode();
                rows[idx][2] = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.ofInstant(greeScanning.getDateTime().toInstant(), ZoneId.systemDefault()));
                rows[idx][3] = Boolean.FALSE;
                idx++;
            }
        } else {
            rows = new Object[greeScanningList.size()][3];
            int idx = 0;
            for (GreeScanning greeScanning : greeScanningList) {
                rows[idx][0] = greeScanning.getVoucher().toString();
                rows[idx][1] = greeScanning.getBarcode();
                rows[idx][2] = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.ofInstant(greeScanning.getDateTime().toInstant(), ZoneId.systemDefault()));
                idx++;
            }
        }
        GreeTableModel tableModel = new GreeTableModel(rows, greeUser.getUsrPower());
        table1.setModel(tableModel);
        table1.updateUI();
    }

}
