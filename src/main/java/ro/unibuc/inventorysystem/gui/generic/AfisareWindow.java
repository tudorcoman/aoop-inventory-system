package ro.unibuc.inventorysystem.gui.generic;

import ro.unibuc.inventorysystem.gui.GuiWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class AfisareWindow extends GuiWindow {
    private JTable table1;
    private JPanel panel1;

    private List<Object> entities;
    private List<String> columnNames;

    public AfisareWindow(List<Object> entities, List<String> columnNames) {
        this.entities = entities;
        this.columnNames = columnNames;

        DefaultTableModel model = new DefaultTableModel(columnNames.toArray(), 0);

        for (Object entity : entities) {
            List<Object> rowData = new ArrayList<>();
            for (String columnName : columnNames) {
                try {
                    rowData.add(entity.getClass().getMethod("get" + columnName).invoke(entity));
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
            model.addRow(rowData.toArray());
        }

        table1.setModel(model);
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        initializeWindow();
    }

    @Override
    protected JPanel getPanel() {
        return panel1;
    }
}
