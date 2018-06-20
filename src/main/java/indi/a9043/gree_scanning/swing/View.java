package indi.a9043.gree_scanning.swing;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import indi.a9043.gree_scanning.pojo.Comm;
import indi.a9043.gree_scanning.pojo.GreeUser;
import indi.a9043.gree_scanning.service.DataService;
import indi.a9043.gree_scanning.swing.pojo.GreeTableModel;
import indi.a9043.gree_scanning.swing.pojo.SearchData;
import indi.a9043.gree_scanning.util.NumberUtils;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author a9043 卢学能 zzz13129180808@gmail.com
 */
@Component
public class View {
    private DataService dataService;
    private JPanel viewPanel;
    private JTextField voucher;
    private JTextField barcode;
    private JTable table1;
    private JButton selectButton;
    private JButton deleteButton;
    private JButton prePage;
    private JButton firstPage;
    private JButton lastPage;
    private JButton nextPage;
    private JSpinner pageNumSpinner;
    private JComboBox<Integer> pageSize;
    private JButton jumpPage;
    private JLabel pageCountNum;
    private JSpinner sYear;
    private JSpinner sDay;
    private JSpinner sMonth;
    private JSpinner eYear;
    private JSpinner eDay;
    private JSpinner eMonth;
    private JCheckBox dismissCheckBox;
    private SearchData searchData;
    private GreeUser greeUser;
    private int pageNum = 1;
    private boolean isDissmissDate = false;

    @Autowired
    public View(final DataService dataService) {
        this.dataService = dataService;
        $$$setupUI$$$();
        JSpinner.DefaultEditor defaultEditor = new JSpinner.DefaultEditor(sYear);
        JTextField textField = defaultEditor.getTextField();
        textField.setEditable(true);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                JFormattedTextField jFormattedTextField = ((JSpinner.DefaultEditor) sYear.getEditor()).getTextField();
                if (jFormattedTextField.getText().length() == 3 && (jFormattedTextField.getSelectedText() == null || jFormattedTextField.getSelectedText().length() != 3)) {
                    jFormattedTextField.transferFocus();
                }
                if (jFormattedTextField.getText().length() > 4) {
                    e.consume();
                    jFormattedTextField.setText(jFormattedTextField.getText().substring(0, 4));
                }
            }
        });
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                super.focusGained(e);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JTextField tf = (JTextField) e.getSource();
                        tf.selectAll();
                    }
                });
            }
        });
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!NumberUtils.isInteger(sYear.getModel().getValue().toString()) || Integer.valueOf(sYear.getModel().getValue().toString()) >= 2600) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            sYear.getModel().setValue(1900);
                            ((JSpinner.DefaultEditor) sYear.getEditor()).getTextField().requestFocus();
                            ((JSpinner.DefaultEditor) sYear.getEditor()).getTextField().selectAll();
                        }
                    });
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        sYear.setEditor(defaultEditor);
        defaultEditor = new JSpinner.DefaultEditor(eYear);
        textField = defaultEditor.getTextField();
        textField.setEditable(true);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                JFormattedTextField jFormattedTextField = ((JSpinner.DefaultEditor) eYear.getEditor()).getTextField();
                if (jFormattedTextField.getText().length() == 3 && (jFormattedTextField.getSelectedText() == null || jFormattedTextField.getSelectedText().length() != 3)) {
                    jFormattedTextField.transferFocus();
                }
                if (jFormattedTextField.getText().length() > 4) {
                    e.consume();
                    jFormattedTextField.setText(jFormattedTextField.getText().substring(0, 4));
                }
            }
        });
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                super.focusGained(e);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JTextField tf = (JTextField) e.getSource();
                        tf.selectAll();
                    }
                });
            }
        });
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!NumberUtils.isInteger(eYear.getModel().getValue().toString()) || Integer.valueOf(eYear.getModel().getValue().toString()) >= 2600) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            eYear.getModel().setValue(1900);
                            ((JSpinner.DefaultEditor) eYear.getEditor()).getTextField().requestFocus();
                            ((JSpinner.DefaultEditor) eYear.getEditor()).getTextField().selectAll();
                        }
                    });
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        eYear.setEditor(defaultEditor);
        defaultEditor = new JSpinner.DefaultEditor(sMonth);
        textField = defaultEditor.getTextField();
        textField.setEditable(true);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                JFormattedTextField jFormattedTextField = ((JSpinner.DefaultEditor) sMonth.getEditor()).getTextField();
                if (jFormattedTextField.getText().length() == 1 && (jFormattedTextField.getSelectedText() == null || jFormattedTextField.getSelectedText().length() != 1)) {
                    jFormattedTextField.transferFocus();
                }
                if (jFormattedTextField.getText().length() > 2) {
                    e.consume();
                    jFormattedTextField.setText(jFormattedTextField.getText().substring(0, 2));
                }
            }
        });
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                super.focusGained(e);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JTextField tf = (JTextField) e.getSource();
                        tf.selectAll();
                    }
                });
            }
        });
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!NumberUtils.isInteger(sMonth.getModel().getValue().toString()) || Integer.valueOf(sMonth.getModel().getValue().toString()) > 12) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            sMonth.getModel().setValue(1);
                            ((JSpinner.DefaultEditor) sMonth.getEditor()).getTextField().requestFocus();
                            ((JSpinner.DefaultEditor) sMonth.getEditor()).getTextField().selectAll();
                        }
                    });
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        sMonth.setEditor(defaultEditor);
        defaultEditor = new JSpinner.DefaultEditor(eMonth);
        textField = defaultEditor.getTextField();
        textField.setEditable(true);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                JFormattedTextField jFormattedTextField = ((JSpinner.DefaultEditor) eMonth.getEditor()).getTextField();
                if (jFormattedTextField.getText().length() == 1 && (jFormattedTextField.getSelectedText() == null || jFormattedTextField.getSelectedText().length() != 1)) {
                    jFormattedTextField.transferFocus();
                }
                if (jFormattedTextField.getText().length() > 2) {
                    e.consume();
                    jFormattedTextField.setText(jFormattedTextField.getText().substring(0, 2));
                }
            }
        });
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                super.focusGained(e);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JTextField tf = (JTextField) e.getSource();
                        tf.selectAll();
                    }
                });
            }
        });
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!NumberUtils.isInteger(eMonth.getModel().getValue().toString()) || Integer.valueOf(eMonth.getModel().getValue().toString()) > 12) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            eMonth.getModel().setValue(1);
                            ((JSpinner.DefaultEditor) eMonth.getEditor()).getTextField().requestFocus();
                            ((JSpinner.DefaultEditor) eMonth.getEditor()).getTextField().selectAll();
                        }
                    });
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        eMonth.setEditor(defaultEditor);
        defaultEditor = new JSpinner.DefaultEditor(sDay);
        textField = defaultEditor.getTextField();
        textField.setEditable(true);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                JFormattedTextField jFormattedTextField = ((JSpinner.DefaultEditor) sDay.getEditor()).getTextField();
                if (jFormattedTextField.getText().length() == 1 && (jFormattedTextField.getSelectedText() == null || jFormattedTextField.getSelectedText().length() != 1)) {
                    jFormattedTextField.transferFocus();
                }
                if (jFormattedTextField.getText().length() > 2) {
                    e.consume();
                    jFormattedTextField.setText(jFormattedTextField.getText().substring(0, 2));
                }
            }
        });
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                super.focusGained(e);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JTextField tf = (JTextField) e.getSource();
                        tf.selectAll();
                    }
                });
            }
        });
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!NumberUtils.isInteger(sDay.getModel().getValue().toString()) || Integer.valueOf(sDay.getModel().getValue().toString()) > 31) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            sDay.getModel().setValue(1);
                            ((JSpinner.DefaultEditor) sDay.getEditor()).getTextField().requestFocus();
                            ((JSpinner.DefaultEditor) sDay.getEditor()).getTextField().selectAll();
                        }
                    });
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        sDay.setEditor(defaultEditor);
        defaultEditor = new JSpinner.DefaultEditor(eDay);
        textField = defaultEditor.getTextField();
        textField.setEditable(true);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                JFormattedTextField jFormattedTextField = ((JSpinner.DefaultEditor) eDay.getEditor()).getTextField();
                if (jFormattedTextField.getText().length() == 1 && (jFormattedTextField.getSelectedText() == null || jFormattedTextField.getSelectedText().length() != 1)) {
                    jFormattedTextField.transferFocus();
                }
                if (jFormattedTextField.getText().length() > 2) {
                    e.consume();
                    jFormattedTextField.setText(jFormattedTextField.getText().substring(0, 2));
                }
            }
        });
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                super.focusGained(e);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JTextField tf = (JTextField) e.getSource();
                        tf.selectAll();
                    }
                });
            }
        });
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!NumberUtils.isInteger(eDay.getModel().getValue().toString()) || Integer.valueOf(eDay.getModel().getValue().toString()) > 31) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            eDay.getModel().setValue(1);
                            ((JSpinner.DefaultEditor) eDay.getEditor()).getTextField().requestFocus();
                            ((JSpinner.DefaultEditor) eDay.getEditor()).getTextField().selectAll();
                        }
                    });
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        eDay.setEditor(defaultEditor);
        selectButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        deleteButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));

        searchData = new SearchData();
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
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowCount = table1.getModel().getRowCount();
                final List<Map<String, String>> mapList = new ArrayList<>();
                for (int i = 0; i < rowCount; i++) {
                    if (table1.getModel().getValueAt(i, 4).equals(Boolean.TRUE)) {
                        Map<String, String> map = new HashMap<>();
                        map.put("voucher", table1.getModel().getValueAt(i, 1).toString());
                        map.put("barcode", table1.getModel().getValueAt(i, 2).toString());
                        mapList.add(map);
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("以下单据\n");
                for (Map map : mapList) {
                    stringBuilder.append(map.get("voucher")).append(" ").append(map.get("barcode")).append("\n");
                }
                stringBuilder.append("确定删除？");
                if (JOptionPane.showConfirmDialog(viewPanel, stringBuilder.toString()) == JOptionPane.YES_OPTION) {
                    final InfiniteProgressPanel infiniteProgressPanel = new InfiniteProgressPanel();
                    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                    infiniteProgressPanel.setBounds(100, 100, (dimension.width) / 2, (dimension.height) / 2);
                    viewPanel.getRootPane().setGlassPane(infiniteProgressPanel);
                    viewPanel.getRootPane().validate();
                    viewPanel.getRootPane().setVisible(true);
                    infiniteProgressPanel.start();
                    SwingWorker swingWorker = new SwingWorker() {
                        @Override
                        protected Object doInBackground() throws Exception {
                            return dataService.deleteComm(mapList);
                        }

                        @Override
                        protected void done() {
                            infiniteProgressPanel.stop();
                            infiniteProgressPanel.setVisible(false);
                            View.this.selectData();
                            super.done();
                        }
                    };
                    swingWorker.execute();
                }
            }
        });
        firstPage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageNum = 1;
                pageNumSpinner.getModel().setValue(pageNum);
                selectData();
            }
        });
        lastPage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageNum = Integer.valueOf(pageCountNum.getText());
                pageNumSpinner.getModel().setValue(pageNum);
                selectData();
            }
        });
        jumpPage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectData();
            }
        });
        prePage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageNum = pageNum == 1 ? 1 : pageNum - 1;
                pageNumSpinner.getModel().setValue(pageNum);
                selectData();
            }
        });
        nextPage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageNum = pageNum == Integer.valueOf(pageCountNum.getText()) ? Integer.valueOf(pageCountNum.getText()) : pageNum + 1;
                pageNumSpinner.getModel().setValue(pageNum);
                selectData();
            }
        });
        pageNumSpinner.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() > 0) {
                    pageNum = pageNum == 1 ? 1 : pageNum - 1;
                } else {
                    pageNum = pageNum == Integer.valueOf(pageCountNum.getText()) ? Integer.valueOf(pageCountNum.getText()) : pageNum + 1;
                }
                pageNumSpinner.getModel().setValue(pageNum);
            }
        });
        sYear.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() > 0) {
                    int val = Integer.valueOf(sYear.getModel().getValue().toString());
                    sYear.getModel().setValue(val > 0 ? val - 1 : val);
                } else {
                    int val = Integer.valueOf(sYear.getModel().getValue().toString());
                    sYear.getModel().setValue(val < 9999 ? val + 1 : val);
                }
            }
        });
        eYear.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() > 0) {
                    int val = Integer.valueOf(eYear.getModel().getValue().toString());
                    eYear.getModel().setValue(val > 0 ? val - 1 : val);
                } else {
                    int val = Integer.valueOf(eYear.getModel().getValue().toString());
                    eYear.getModel().setValue(val < 9999 ? val + 1 : val);
                }
            }
        });
        sMonth.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() > 0) {
                    int val = Integer.valueOf(sMonth.getModel().getValue().toString());
                    sMonth.getModel().setValue(val > 0 ? val - 1 : val);
                } else {
                    int val = Integer.valueOf(sMonth.getModel().getValue().toString());
                    sMonth.getModel().setValue(val < 12 ? val + 1 : val);
                }
            }
        });
        eMonth.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() > 0) {
                    int val = Integer.valueOf(eMonth.getModel().getValue().toString());
                    eMonth.getModel().setValue(val > 0 ? val - 1 : val);
                } else {
                    int val = Integer.valueOf(eMonth.getModel().getValue().toString());
                    eMonth.getModel().setValue(val < 12 ? val + 1 : val);
                }
            }
        });
        sDay.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() > 0) {
                    int val = Integer.valueOf(sDay.getModel().getValue().toString());
                    sDay.getModel().setValue(val > 0 ? val - 1 : val);
                } else {
                    int val = Integer.valueOf(sDay.getModel().getValue().toString());
                    sDay.getModel().setValue(val < 31 ? val + 1 : val);
                }
            }
        });
        eDay.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() > 0) {
                    int val = Integer.valueOf(eDay.getModel().getValue().toString());
                    eDay.getModel().setValue(val > 0 ? val - 1 : val);
                } else {
                    int val = Integer.valueOf(eDay.getModel().getValue().toString());
                    eDay.getModel().setValue(val < 31 ? val + 1 : val);
                }
            }
        });
        dismissCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (dismissCheckBox.isSelected()) {
                    sYear.setEnabled(false);
                    eYear.setEnabled(false);
                    sMonth.setEnabled(false);
                    eMonth.setEnabled(false);
                    sDay.setEnabled(false);
                    eDay.setEnabled(false);
                    isDissmissDate = true;
                } else {
                    sYear.setEnabled(true);
                    eYear.setEnabled(true);
                    sMonth.setEnabled(true);
                    eMonth.setEnabled(true);
                    sDay.setEnabled(true);
                    eDay.setEnabled(true);
                    isDissmissDate = false;
                }
            }
        });
    }

    JPanel getSuccess(GreeUser greeUser) {
        this.greeUser = greeUser;
        register();
        return viewPanel;
    }

    private void selectData() {
        final InfiniteProgressPanel infiniteProgressPanel = new InfiniteProgressPanel();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        infiniteProgressPanel.setBounds(100, 100, (dimension.width) / 2, (dimension.height) / 2);
        if (viewPanel.getRootPane() != null) {
            viewPanel.getRootPane().setGlassPane(infiniteProgressPanel);
            viewPanel.getRootPane().validate();
            viewPanel.getRootPane().setVisible(true);
            infiniteProgressPanel.start();
        }
        getData(searchData);

        try {
            final java.sql.Date startDate = java.sql.Date.valueOf(String.format("%s-%s-%s",
                    sYear.getModel().getValue(),
                    sMonth.getModel().getValue(),
                    sDay.getModel().getValue()));
            final java.sql.Date endDate = java.sql.Date.valueOf(String.format("%s-%s-%s",
                    eYear.getModel().getValue(),
                    eMonth.getModel().getValue(),
                    eDay.getModel().getValue()));

            new SwingWorker() {
                @Override
                protected Void doInBackground() throws Exception {
                    long pageCount = dataService.getPageCount(Integer.valueOf(pageSize.getModel().getSelectedItem().toString()), searchData.getVoucher(), searchData.getBarcode(), startDate, endDate);
                    pageCountNum.setText(String.valueOf(pageCount));
                    ((SpinnerNumberModel) pageNumSpinner.getModel()).setMinimum(1);
                    ((SpinnerNumberModel) pageNumSpinner.getModel()).setMaximum(Integer.valueOf(pageCountNum.getText()));
                    return null;
                }
            }.execute();
            SwingWorker<List<Comm>, Object> swingWorker = new SwingWorker<List<Comm>, Object>() {
                @Override
                protected List<Comm> doInBackground() throws Exception {
                    return dataService.selectComm(searchData.getVoucher(),
                            searchData.getBarcode(),
                            isDissmissDate ? null : startDate,
                            isDissmissDate ? null : endDate,
                            (pageNum - 1) * Long.valueOf(pageSize.getModel().getSelectedItem().toString()) + 1,
                            pageNum * Long.valueOf(pageSize.getModel().getSelectedItem().toString()));
                }

                @Override
                protected void done() {
                    infiniteProgressPanel.stop();
                    List<Comm> commList;
                    try {
                        commList = get();
                        Object[][] rows;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        if ((greeUser.getUsrPower() & 4) == 4) {
                            rows = new Object[commList.size()][5];
                            int idx = 0;
                            for (Comm comm : commList) {
                                rows[idx][0] = String.valueOf(idx + 1);
                                rows[idx][1] = comm.getVoucher();
                                rows[idx][2] = comm.getBarcode();
                                rows[idx][3] = simpleDateFormat.format(comm.getDateTime());
                                rows[idx][4] = Boolean.FALSE;
                                idx++;
                            }
                        } else {
                            rows = new Object[commList.size()][4];
                            int idx = 0;
                            for (Comm comm : commList) {
                                rows[idx][0] = String.valueOf(idx + 1);
                                rows[idx][1] = comm.getVoucher();
                                rows[idx][2] = comm.getBarcode();
                                rows[idx][3] = simpleDateFormat.format(comm.getDateTime());
                                idx++;
                            }
                        }
                        GreeTableModel tableModel = new GreeTableModel(rows, greeUser.getUsrPower());
                        table1.setModel(tableModel);
                        if ((greeUser.getUsrPower() & 4) == 4) {
                            table1.getColumnModel().getColumn(0).setPreferredWidth(10);
                            table1.getColumnModel().getColumn(1).setPreferredWidth(50);
                            table1.getColumnModel().getColumn(3).setPreferredWidth(110);
                        }
                        table1.updateUI();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            };
            swingWorker.execute();
        } catch (IllegalArgumentException e) {
            infiniteProgressPanel.stop();
            infiniteProgressPanel.setVisible(false);
            JOptionPane.showMessageDialog(viewPanel, "搜索条件非法", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void register() {
        //register date picker
        SpinnerNumberModel sYearModel = new SpinnerNumberModel();
        sYearModel.setMinimum(1900);
        sYearModel.setMaximum(2500);
        sYear.setModel(sYearModel);
        SpinnerNumberModel eYearModel = new SpinnerNumberModel();
        eYearModel.setMinimum(1900);
        eYearModel.setMaximum(2500);
        eYear.setModel(eYearModel);
        SpinnerNumberModel sMonthModel = new SpinnerNumberModel();
        sMonthModel.setMinimum(1);
        sMonthModel.setMaximum(12);
        sMonth.setModel(sMonthModel);
        SpinnerNumberModel eMonthModel = new SpinnerNumberModel();
        eMonthModel.setMinimum(1);
        eMonthModel.setMaximum(12);
        eMonth.setModel(eMonthModel);
        SpinnerNumberModel sDayModel = new SpinnerNumberModel();
        sDayModel.setMinimum(1);
        sDayModel.setMaximum(31);
        sDay.setModel(sDayModel);
        SpinnerNumberModel eDayModel = new SpinnerNumberModel();
        eDayModel.setMinimum(1);
        eDayModel.setMaximum(31);
        eDay.setModel(eDayModel);
        Calendar calendar = Calendar.getInstance();
        sYear.setValue(calendar.get(Calendar.YEAR));
        sMonth.setValue(calendar.get(Calendar.MONTH) + 1);
        sDay.setValue(calendar.get(Calendar.DAY_OF_MONTH));
        eYear.setValue(calendar.get(Calendar.YEAR));
        eMonth.setValue(calendar.get(Calendar.MONTH) + 1);
        eDay.setValue(calendar.get(Calendar.DAY_OF_MONTH));

        //register pageHelper
        DefaultComboBoxModel<Integer> comboBoxModel = new DefaultComboBoxModel<>();
        comboBoxModel.addElement(50);
        comboBoxModel.addElement(100);
        comboBoxModel.addElement(200);
        comboBoxModel.addElement(350);
        comboBoxModel.addElement(500);
        comboBoxModel.setSelectedItem(comboBoxModel.getElementAt(1));
        pageSize.setModel(comboBoxModel);

        getData(searchData);

        final java.sql.Date startDate = java.sql.Date.valueOf(String.format("%s-%s-%s",
                sYear.getModel().getValue(),
                sMonth.getModel().getValue(),
                sDay.getModel().getValue()));
        final java.sql.Date endDate = java.sql.Date.valueOf(String.format("%s-%s-%s",
                eYear.getModel().getValue(),
                eMonth.getModel().getValue(),
                eDay.getModel().getValue()));

        new SwingWorker() {
            @Override
            protected Void doInBackground() throws Exception {
                long pageCount = dataService.getPageCount(
                        Integer.valueOf(pageSize.getModel().getSelectedItem().toString()),
                        searchData.getVoucher(),
                        searchData.getBarcode(),
                        startDate,
                        endDate);
                pageCountNum.setText(String.valueOf(pageCount));

                //register pageNumSpinner
                final SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel();
                spinnerNumberModel.setMinimum(1);
                spinnerNumberModel.setValue(1);
                spinnerNumberModel.setMaximum(Integer.valueOf(pageCountNum.getText()));
                spinnerNumberModel.setStepSize(1);
                spinnerNumberModel.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        pageNum = Integer.valueOf(spinnerNumberModel.getValue().toString());
                    }
                });
                pageNumSpinner.setModel(spinnerNumberModel);
                if ((greeUser.getUsrPower() & 4) != 4) {
                    deleteButton.setVisible(false);
                }
                selectData();
                return null;
            }
        }.execute();
    }


    public void setData(SearchData data) {
        voucher.setText(data.getVoucher());
        barcode.setText(data.getBarcode());
    }

    public void getData(SearchData data) {
        data.setVoucher(voucher.getText());
        data.setBarcode(barcode.getText());
    }

    public boolean isModified(SearchData data) {
        if (voucher.getText() != null ? !voucher.getText().equals(data.getVoucher()) : data.getVoucher() != null)
            return true;
        if (barcode.getText() != null ? !barcode.getText().equals(data.getBarcode()) : data.getBarcode() != null)
            return true;
        return false;
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
        viewPanel.setLayout(new GridLayoutManager(21, 5, new Insets(0, 0, 0, 0), -1, -1));
        viewPanel.setMaximumSize(new Dimension(740, 610));
        viewPanel.setMinimumSize(new Dimension(740, 610));
        viewPanel.setPreferredSize(new Dimension(740, 610));
        voucher = new JTextField();
        viewPanel.add(voucher, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(250, 35), new Dimension(250, 35), new Dimension(250, 35), 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        viewPanel.add(scrollPane1, new GridConstraints(3, 0, 17, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table1 = new JTable();
        scrollPane1.setViewportView(table1);
        selectButton = new JButton();
        selectButton.setText("搜索");
        viewPanel.add(selectButton, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(70, 35), new Dimension(70, 35), new Dimension(70, 35), 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("单据号");
        viewPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("从");
        viewPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteButton = new JButton();
        deleteButton.setText("删除");
        viewPanel.add(deleteButton, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(70, 35), new Dimension(70, 35), new Dimension(70, 35), 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 14, new Insets(0, 0, 0, 0), -1, -1));
        viewPanel.add(panel1, new GridConstraints(20, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lastPage = new JButton();
        lastPage.setText("尾页");
        panel1.add(lastPage, new GridConstraints(0, 10, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nextPage = new JButton();
        nextPage.setText("下一页");
        panel1.add(nextPage, new GridConstraints(0, 9, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        prePage = new JButton();
        prePage.setText("上一页");
        panel1.add(prePage, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("总");
        panel1.add(label3, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pageCountNum = new JLabel();
        pageCountNum.setText("0");
        panel1.add(pageCountNum, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("页");
        panel1.add(label4, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        firstPage = new JButton();
        firstPage.setText("首页");
        panel1.add(firstPage, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("每页");
        panel1.add(label5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pageSize = new JComboBox();
        panel1.add(pageSize, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("条");
        panel1.add(label6, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pageNumSpinner = new JSpinner();
        panel1.add(pageNumSpinner, new GridConstraints(0, 12, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("当前页");
        panel1.add(label7, new GridConstraints(0, 11, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        jumpPage = new JButton();
        jumpPage.setText("跳转");
        panel1.add(jumpPage, new GridConstraints(0, 13, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
        viewPanel.add(panel2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(250, 35), new Dimension(250, 35), new Dimension(250, 35), 0, false));
        sYear = new JSpinner();
        panel2.add(sYear, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sDay = new JSpinner();
        panel2.add(sDay, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sMonth = new JSpinner();
        panel2.add(sMonth, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("年");
        panel2.add(label8, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("月");
        panel2.add(label9, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("日");
        panel2.add(label10, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        barcode = new JTextField();
        viewPanel.add(barcode, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(250, 35), new Dimension(250, 35), new Dimension(250, 35), 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
        viewPanel.add(panel3, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(250, 35), new Dimension(250, 35), new Dimension(250, 35), 0, false));
        eYear = new JSpinner();
        panel3.add(eYear, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        eDay = new JSpinner();
        panel3.add(eDay, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        eMonth = new JSpinner();
        panel3.add(eMonth, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("年");
        panel3.add(label11, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("月");
        panel3.add(label12, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("日");
        panel3.add(label13, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("条码");
        viewPanel.add(label14, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label15 = new JLabel();
        label15.setText("到");
        viewPanel.add(label15, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dismissCheckBox = new JCheckBox();
        dismissCheckBox.setText("忽略日期");
        viewPanel.add(dismissCheckBox, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return viewPanel;
    }
}
