package indi.a9043.gree_scanning.swing.pojo;

import javax.swing.table.AbstractTableModel;

public class GreeTableModel extends AbstractTableModel {
    private final String[] colNamesAdm = {"单据号", "产品条码", "日期", "选择"};
    private final String[] colNamesNor = {"单据号", "产品条码", "日期"};
    private Byte power;
    private Object[][] data;
    private Class[] typeArr = {String.class, String.class, String.class, Boolean.class};

    public GreeTableModel(Object[][] data, Byte power) {
        this.data = data;
        this.power = power;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        if ((power & 4) == 4) {
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
        if ((power & 4) == 4) {
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
