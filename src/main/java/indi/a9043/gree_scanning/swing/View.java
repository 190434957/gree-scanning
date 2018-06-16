package indi.a9043.gree_scanning.swing;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import indi.a9043.gree_scanning.pojo.GreeScanning;
import indi.a9043.gree_scanning.pojo.GreeUser;
import indi.a9043.gree_scanning.service.DataService;
import indi.a9043.gree_scanning.swing.pojo.GreeTableModel;
import indi.a9043.gree_scanning.swing.pojo.SearchData;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author a9043 卢学能 zzz13129180808@gmail.com
 */
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
    public View(final DataService dataService) {
        this.dataService = dataService;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        nowDateStr = simpleDateFormat.format(new Date());
        startDate.setText(nowDateStr);
        endDate.setText(nowDateStr);
        selectButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        deleteButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));

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
                if (startDate.getText().length() > 10) {
                    e.consume();
                    startDate.setText(startDate.getText().substring(0, 10));
                }
            }
        });
        endDate.addKeyListener(new KeyAdapter() {
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
                if (endDate.getText().length() > 10) {
                    e.consume();
                    endDate.setText(endDate.getText().substring(0, 10));
                }
            }
        });
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((greeUser.getUsrPower() & 1) == 1 || (greeUser.getUsrPower() & 4) == 4) {
                    View.this.selectData();
                } else {
                    JOptionPane.showMessageDialog(viewPanel, "你没有查询权限! ", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        startDate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (startDate.getText().equals(nowDateStr)) {
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
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowCount = table1.getModel().getRowCount();
                List<String> voucherList = new ArrayList<String>();
                for (int i = 0; i < rowCount; i++) {
                    if (table1.getModel().getValueAt(i, 3).equals(Boolean.TRUE)) {
                        voucherList.add(table1.getModel().getValueAt(i, 0).toString());
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("以下单据\n");
                for (String aVoucherList : voucherList) {
                    stringBuilder.append(aVoucherList).append("\n");
                }
                stringBuilder.append("确定删除？");
                if (JOptionPane.showConfirmDialog(viewPanel, stringBuilder.toString()) == JOptionPane.YES_OPTION) {
                    dataService.deleteGreeScanning(voucherList);
                    View.this.selectData();
                }
            }
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

        String tempDateStr = searchData.getStartDate();
        java.sql.Date startDate = null;
        if (tempDateStr != null && !tempDateStr.equals("") && tempDateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
            startDate = java.sql.Date.valueOf(searchData.getStartDate());
        }


        tempDateStr = searchData.getEndDate();
        java.sql.Date endDate = null;
        if (tempDateStr != null && !tempDateStr.equals("") && tempDateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
            endDate = java.sql.Date.valueOf(searchData.getEndDate());
        }

        List<GreeScanning> greeScanningList = dataService.selectGreeScanning(searchData.getVoucher(), searchData.getBarcode(), startDate, endDate);
        Object[][] rows;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if ((greeUser.getUsrPower() & 4) == 4) {
            rows = new Object[greeScanningList.size()][4];
            int idx = 0;
            for (GreeScanning greeScanning : greeScanningList) {
                rows[idx][0] = greeScanning.getVoucher().toString();
                rows[idx][1] = greeScanning.getBarcode();
                rows[idx][2] = simpleDateFormat.format(greeScanning.getDateTime());
                rows[idx][3] = Boolean.FALSE;
                idx++;
            }
        } else {
            rows = new Object[greeScanningList.size()][3];
            int idx = 0;
            for (GreeScanning greeScanning : greeScanningList) {
                rows[idx][0] = greeScanning.getVoucher().toString();
                rows[idx][1] = greeScanning.getBarcode();
                rows[idx][2] = simpleDateFormat.format(greeScanning.getDateTime());
                idx++;
            }
        }
        GreeTableModel tableModel = new GreeTableModel(rows, greeUser.getUsrPower());
        table1.setModel(tableModel);
        table1.updateUI();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        viewPanel = new JPanel();
        viewPanel.setLayout(new GridLayoutManager(20, 7, new Insets(0, 0, 0, 0), -1, -1));
        viewPanel.setMaximumSize(new Dimension(750, 610));
        viewPanel.setMinimumSize(new Dimension(750, 610));
        viewPanel.setPreferredSize(new Dimension(750, 610));
        final Spacer spacer1 = new Spacer();
        viewPanel.add(spacer1, new GridConstraints(0, 2, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        startDate = new JTextField();
        startDate.setText("0000-00-00");
        startDate.setToolTipText("开始时间");
        viewPanel.add(startDate, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 35), null, 0, false));
        voucher = new JTextField();
        viewPanel.add(voucher, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 35), null, 0, false));
        barcode = new JTextField();
        viewPanel.add(barcode, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 35), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        viewPanel.add(scrollPane1, new GridConstraints(2, 0, 18, 7, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table1 = new JTable();
        scrollPane1.setViewportView(table1);
        final Spacer spacer2 = new Spacer();
        viewPanel.add(spacer2, new GridConstraints(0, 5, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        selectButton = new JButton();
        selectButton.setText("搜索");
        viewPanel.add(selectButton, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(70, 35), new Dimension(70, 35), new Dimension(70, 35), 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("单据号");
        viewPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("从");
        viewPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("条码");
        viewPanel.add(label3, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        endDate = new JTextField();
        endDate.setText("0000-00-00");
        endDate.setToolTipText("结束时间");
        viewPanel.add(endDate, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 35), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("到");
        viewPanel.add(label4, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteButton = new JButton();
        deleteButton.setText("删除");
        viewPanel.add(deleteButton, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(70, 35), new Dimension(70, 35), new Dimension(70, 35), 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return viewPanel;
    }
}
