package app.util;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;
import java.util.List;

public class CustomTableModel<T> extends AbstractTableModel {

    private final Class<T> cls;
    private final String[] columnNames;
    private List<T> values;

    public CustomTableModel(Class<T> cls, String[] columnNames, List<T> values) {
        this.cls = cls;
        this.columnNames = columnNames;
        this.values = values;
    }

    public void removeValues() {
        this.values.clear();
    }

    @Override
    public int getRowCount() {
        return this.values.size();
    }

    @Override
    public int getColumnCount() {
        return cls.getDeclaredFields().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            Field field = cls.getDeclaredFields()[columnIndex];
            field.setAccessible(true);
            return field.get(this.values.get(rowIndex));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "Error while getting value at";
        }
    }


    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return cls.getDeclaredFields()[columnIndex].getType();
    }

    @Override
    public String getColumnName(int column) {
        return column >= columnNames.length ? "NAME" : columnNames[column];
    }
}
