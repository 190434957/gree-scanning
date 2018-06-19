package indi.a9043.gree_scanning.swing.pojo;

import javax.swing.table.AbstractTableModel;

public class UserTableModel extends AbstractTableModel {
    private final String[] colNames = {"用户名", "昵称", "查权限", "增权限", "删权限", "重置密码", "删除"};
    private Class[] typeArr = {String.class, String.class, Boolean.class, Boolean.class, Boolean.class, Boolean.class, Boolean.class};
    private Object[][] data;

    public UserTableModel(Object[][] data) {
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return colNames[column];
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
