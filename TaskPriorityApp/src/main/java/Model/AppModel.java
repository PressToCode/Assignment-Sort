/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author PREDATOR
 */
public class AppModel<T> {
    // Global Use
    private final DefaultTableModel model;
    private final ArrayList<T> dataArray;
    
    public AppModel(TableModel model) {
        // Initialize Model
        this.model = (DefaultTableModel) model;
        
        // Initialize dataArray
        this.dataArray = new ArrayList<>();
    }

    public void removeRows(int[] selectedRows) {
        for(int i = selectedRows.length - 1; i >= 0; i--) {
            model.removeRow(selectedRows[i]);
            dataArray.remove(selectedRows[i]);
        }
    }

    public void updateRow(T data, int selectedRow) {
        dataArray.remove(selectedRow);
        dataArray.add(data);
    }

    public void resetModel() {
        dataArray.clear();
    }
    
    public DefaultTableModel getActualModel() {
        return this.model;
    }
    
    public ArrayList<T> getDataArray() {
        return this.dataArray;
    }
}