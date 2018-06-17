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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public Insert(final DataService dataService) {
        textField.setEditable(false);
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
                if (rows != null && rows.length > 0) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    List<Comm> commList = new ArrayList<Comm>();
                    for (Object[] row : rows) {
                        Comm comm = new Comm();
                        comm.setVoucher(row[0].toString());
                        comm.setBarcode(row[1].toString());
                        try {
                            comm.setDateTime(simpleDateFormat.parse(row[2].toString()));
                        } catch (ParseException e1) {
                            // TODO
                            e1.printStackTrace();
                        }
                        commList.add(comm);
                    }
                    int[] res = dataService.addNewData(commList);
                    JOptionPane.showMessageDialog(insertPanel,
                            "总" +
                                    commList.size() +
                                    "条\n重复" +
                                    res[0] +
                                    "条\n成功插入" +
                                    res[1]
                                    + "条! ",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
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
            List<String[]> rowList = new ArrayList<String[]>();
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
        insertPanel.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        insertPanel.setInheritsPopupMenu(true);
        insertPanel.setMaximumSize(new Dimension(750, 610));
        insertPanel.setMinimumSize(new Dimension(750, 610));
        insertPanel.setPreferredSize(new Dimension(750, 610));
        final Spacer spacer1 = new Spacer();
        insertPanel.add(spacer1, new GridConstraints(1, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        insertPanel.add(scrollPane1, new GridConstraints(2, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table1 = new JTable();
        scrollPane1.setViewportView(table1);
        final JLabel label1 = new JLabel();
        label1.setText("文件");
        insertPanel.add(label1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fileText = new JPanel();
        fileText.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        insertPanel.add(fileText, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(153, 24), null, 0, false));
        textField = new JTextField();
        textField.setInheritsPopupMenu(true);
        fileText.add(textField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        selectButton = new JButton();
        selectButton.setText("选择");
        fileText.add(selectButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        insertButton = new JButton();
        insertButton.setText("导入");
        fileText.add(insertButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return insertPanel;
    }
}
