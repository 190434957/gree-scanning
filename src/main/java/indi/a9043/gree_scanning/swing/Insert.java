package indi.a9043.gree_scanning.swing;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import indi.a9043.gree_scanning.pojo.Comm;
import indi.a9043.gree_scanning.pojo.GreeUser;
import indi.a9043.gree_scanning.service.DataService;
import indi.a9043.gree_scanning.swing.pojo.GreeTableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author a9043 卢学能 zzz13129180808@gmail.com
 */
@Component
public class Insert {
    private final String newPat = "No.1 (单号,条形码,日期时间)";
    private final String oldPat = "No.2 (条形码;单号;日期)";
    private JPanel insertPanel;
    private JTable table1;
    private JPanel fileText;
    private JButton selectButton;
    private JTextField textField;
    private JButton insertButton;
    private JComboBox<String> typeBox;
    private JScrollPane tablePane;
    private File file;
    private Object[][] rows;

    @Autowired
    public Insert(final DataService dataService) {
        tablePane.setDropTarget(new DropTarget(tablePane, DnDConstants.ACTION_COPY_OR_MOVE,
                new DropTargetAdapter() {
                    @Override
                    public void drop(DropTargetDropEvent dtde) {
                        try {
                            if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                                @SuppressWarnings("unchecked")
                                List<File> list = (List<File>) (dtde.getTransferable()
                                        .getTransferData(DataFlavor.javaFileListFlavor));
                                if (list.size() > 1) {
                                    dtde.dropComplete(true);
                                    return;
                                }
                                file = list.get(0);
                                if (file != null) {
                                    textField.setText(file.getAbsolutePath());
                                    Insert.this.readFile();
                                }
                                dtde.dropComplete(true);
                            } else {
                                dtde.rejectDrop();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }));
        table1.setDropTarget(new DropTarget(table1, DnDConstants.ACTION_COPY_OR_MOVE,
                new DropTargetAdapter() {
                    @Override
                    public void drop(DropTargetDropEvent dtde) {
                        try {
                            if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                                @SuppressWarnings("unchecked")
                                List<File> list = (List<File>) (dtde.getTransferable()
                                        .getTransferData(DataFlavor.javaFileListFlavor));
                                if (list.size() > 1) {
                                    dtde.dropComplete(true);
                                    return;
                                }
                                file = list.get(0);
                                if (file != null) {
                                    textField.setText(file.getAbsolutePath());
                                    Insert.this.readFile();
                                }
                                dtde.dropComplete(true);
                            } else {
                                dtde.rejectDrop();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
        );
        textField.setDropTarget(new DropTarget(textField, DnDConstants.ACTION_COPY_OR_MOVE,
                new DropTargetAdapter() {
                    @Override
                    public void drop(DropTargetDropEvent dtde) {
                        try {
                            if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                                @SuppressWarnings("unchecked")
                                List<File> list = (List<File>) (dtde.getTransferable()
                                        .getTransferData(DataFlavor.javaFileListFlavor));
                                if (list.size() > 1) {
                                    dtde.dropComplete(true);
                                    return;
                                }
                                file = list.get(0);
                                if (file != null) {
                                    textField.setText(file.getAbsolutePath());
                                    Insert.this.readFile();
                                }
                                dtde.dropComplete(true);
                            } else {
                                dtde.rejectDrop();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
        );
        textField.setEditable(false);
        typeBox.addItem(newPat);
        typeBox.addItem(oldPat);
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jFileChooser.showOpenDialog(insertPanel);
                file = jFileChooser.getSelectedFile();
                if (file != null) {
                    textField.setText(file.getAbsolutePath());
                    Insert.this.readFile();
                }
            }
        });
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final InfiniteProgressPanel infiniteProgressPanel = new InfiniteProgressPanel();
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                infiniteProgressPanel.setBounds(100, 100, (dimension.width) / 2, (dimension.height) / 2);
                insertPanel.getRootPane().setGlassPane(infiniteProgressPanel);
                insertPanel.getRootPane().validate();
                insertPanel.getRootPane().setVisible(true);
                infiniteProgressPanel.start();
                if (rows != null && rows.length > 0) {
                    String patStr = Objects.requireNonNull(typeBox.getSelectedItem()).toString();
                    SimpleDateFormat simpleDateFormat;
                    switch (patStr) {
                        case newPat:
                            simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            break;
                        case oldPat:
                            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            break;
                        default:
                            return;
                    }
                    final List<Comm> commList = new ArrayList<Comm>();
                    for (Object[] row : rows) {
                        Comm comm = new Comm();
                        comm.setVoucher(row[1].toString());
                        comm.setBarcode(row[2].toString());
                        try {
                            comm.setDateTime(simpleDateFormat.parse(row[3].toString()));
                        } catch (ParseException e1) {
                            // TODO
                            e1.printStackTrace();
                        }
                        commList.add(comm);
                    }
                    SwingWorker<int[], Object> swingWorker = new SwingWorker<int[], Object>() {
                        @Override
                        protected int[] doInBackground() throws Exception {
                            return dataService.addNewData(commList);
                        }

                        @Override
                        protected void done() {
                            infiniteProgressPanel.stop();
                            int[] res = new int[2];
                            try {
                                res = get();
                            } catch (InterruptedException | ExecutionException e1) {
                                e1.printStackTrace();
                            }
                            JOptionPane.showMessageDialog(insertPanel,
                                    "总" +
                                            commList.size() +
                                            "条\n" +
                                            "成功插入" +
                                            res[1]
                                            + "条! ",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                            file = null;
                            textField.setText("");
                            rows = new Object[0][4];
                            GreeTableModel greeTableModel = new GreeTableModel(rows, Integer.valueOf(1).byteValue());
                            table1.setModel(greeTableModel);
                            table1.updateUI();
                            super.done();
                        }
                    };
                    swingWorker.execute();
                } else {
                    infiniteProgressPanel.stop();
                    infiniteProgressPanel.setVisible(false);
                    JOptionPane.showMessageDialog(insertPanel, "文件无数据! ", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    JPanel getInsertPanel(GreeUser greeUser) {
        GreeUser greeUser1 = greeUser;
        return insertPanel;
    }

    private void readFile() {
        try {
            String patStr = Objects.requireNonNull(typeBox.getSelectedItem()).toString();
            List<String[]> rowList = new ArrayList<>();
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            Pattern pattern = null;
            if (patStr.equals(newPat)) {
                pattern = Pattern.compile("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}");
            } else if (patStr.equals(oldPat)) {
                pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
            }
            String line;
            int num = 1;
            while ((line = bufferedReader.readLine()) != null) {
                String[] strings = null;
                if (patStr.equals(newPat)) {
                    strings = line.split(",");
                } else if (patStr.equals(oldPat)) {
                    strings = line.split(";");
                }
                for (int i = 0; i < Objects.requireNonNull(strings).length; i++) {
                    strings[i] = strings[i].trim();
                }
                String[] strings1 = new String[4];
                strings1[0] = String.valueOf(num);
                if (patStr.equals(newPat)) {
                    strings1[1] = strings[0];
                    strings1[2] = strings[1];
                } else if (patStr.equals(oldPat)) {
                    strings1[1] = strings[1];
                    strings1[2] = strings[0];
                }
                strings1[3] = strings[2];
                if (strings.length == 3) {
                    Matcher matcher = Objects.requireNonNull(pattern).matcher(strings[2]);
                    if (matcher.matches()) {
                        rowList.add(strings1);
                    }
                }
                num++;
            }
            rows = new Object[rowList.size()][4];
            for (int i = 0; i < rowList.size(); i++) {
                rows[i] = rowList.get(i);
            }
            GreeTableModel greeTableModel = new GreeTableModel(rows, Integer.valueOf(1).byteValue());
            table1.setModel(greeTableModel);
            table1.updateUI();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(insertPanel, "读取错误, 请检查文件和格式! ", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
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
        insertPanel = new JPanel();
        insertPanel.setLayout(new GridLayoutManager(4, 6, new Insets(0, 0, 0, 0), -1, -1));
        insertPanel.setInheritsPopupMenu(true);
        insertPanel.setMaximumSize(new Dimension(740, 610));
        insertPanel.setMinimumSize(new Dimension(740, 610));
        insertPanel.setPreferredSize(new Dimension(740, 610));
        final Spacer spacer1 = new Spacer();
        insertPanel.add(spacer1, new GridConstraints(2, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        tablePane = new JScrollPane();
        insertPanel.add(tablePane, new GridConstraints(3, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table1 = new JTable();
        tablePane.setViewportView(table1);
        final JLabel label1 = new JLabel();
        label1.setText("文件");
        insertPanel.add(label1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fileText = new JPanel();
        fileText.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        insertPanel.add(fileText, new GridConstraints(0, 2, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(153, 24), null, 0, false));
        textField = new JTextField();
        textField.setInheritsPopupMenu(true);
        fileText.add(textField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        selectButton = new JButton();
        selectButton.setText("选择");
        fileText.add(selectButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        insertButton = new JButton();
        insertButton.setText("导入");
        fileText.add(insertButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        typeBox = new JComboBox();
        insertPanel.add(typeBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("格式");
        insertPanel.add(label2, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return insertPanel;
    }
}
