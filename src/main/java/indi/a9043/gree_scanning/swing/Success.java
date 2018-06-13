package indi.a9043.gree_scanning.swing;

import indi.a9043.gree_scanning.pojo.GreeScanning;
import indi.a9043.gree_scanning.pojo.GreeUser;
import indi.a9043.gree_scanning.service.DataService;
import indi.a9043.gree_scanning.swing.pojo.SearchData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
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
public class Success {
    private final String[] colNamesAdm = {"单据号", "产品条码", "日期", "选择"};
    private final String[] colNamesNor = {"单据号", "产品条码", "日期"};
    private String nowDateStr;
    private DataService dataService;
    private JPanel success;
    private JButton insertButton;
    private JButton searchButton;
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
    public Success(DataService dataService) {
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
            if ((greeUser.getUsrPower() & 1) == 1) {
                selectData();
            } else {
                JOptionPane.showMessageDialog(success, "你没有查询权限! ", "Error", JOptionPane.ERROR_MESSAGE);
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
        startDate.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (startDate.getText().equals("")) {
                    startDate.setText(nowDateStr);
                }
                super.focusLost(e);
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
        endDate.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (endDate.getText().equals("")) {
                    endDate.setText(nowDateStr);
                }
                super.focusLost(e);
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

    void show(GreeUser greeUser) {
        this.greeUser = greeUser;
        if ((greeUser.getUsrPower() & 4) != 4) {
            deleteButton.setVisible(false);
        }
        if ((greeUser.getUsrPower() & 2) != 2) {
            insertButton.setVisible(false);
        }
        JFrame frame = new JFrame("Success");
        int windowWidth = frame.getWidth();
        int windowHeight = frame.getHeight();
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        frame.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);
        frame.setResizable(false);
        frame.setContentPane(success);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
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
        GreeTableModel tableModel = new GreeTableModel(rows);
        table1.setModel(tableModel);
        table1.updateUI();
    }

    private class GreeTableModel extends AbstractTableModel {

        private Object[][] data;

        Class[] typeArr = {String.class, String.class, String.class, Boolean.class};

        public GreeTableModel(Object[][] data) {
            this.data = data;
        }

        public GreeTableModel() {
        }

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            if ((greeUser.getUsrPower() & 4) == 4) {
                return colNamesAdm.length;
            }
            return colNamesNor.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

        @Override
        public String getColumnName(int column) {
            if ((greeUser.getUsrPower() & 4) == 4) {
                return colNamesAdm[column];
            }
            return colNamesNor[column];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            data[rowIndex][columnIndex] = aValue;
            fireTableCellUpdated(rowIndex, columnIndex);
        }

        public Class getColumnClass(int columnIndex) {
            return typeArr[columnIndex];
        }
    }
}
