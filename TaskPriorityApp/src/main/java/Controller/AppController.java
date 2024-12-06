/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.AppModel;
import Model.rowModel;
import com.github.lgooddatepicker.components.DateTimePicker;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.Date;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

/**
 *
 * @author PREDATOR
 */
public class AppController {
    // Global Use
    private final AppModel model;
    private boolean saveType = true;
    private final File tableFile = new File("tableSave.txt");
    private int row;
    private int column;
    private String sort = "INSERTION";
    
    public AppController(TableModel model) {
        // Initialize Model
        this.model = new AppModel<rowModel>(model);
    }
    
    public void reloadModel() {
        this.model.getActualModel().setRowCount(0);
        
        this.model.getDataArray().forEach(n -> {
            this.model.getActualModel().addRow(((rowModel)n).toStringArray());
        });
    }
    
    public rowModel fieldSanitize(JTextField mainTGroupField, JTextField mainTNameField, DateTimePicker mainDateField) {
        rowModel sanitizedData = new rowModel(mainTGroupField, mainTNameField, mainDateField);
        
        // Check if input is invalid (Sanitize)
        if(sanitizedData.getGroup().isBlank() || sanitizedData.getTask().isBlank() || sanitizedData.getDate() == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error: Fill Group, Task Name, and Date!");
            return null;
        }
        
        return sanitizedData;
    }

    public void addPerformed(JTextField mainTGroupField, JTextField mainTNameField, DateTimePicker mainDateField) {
        rowModel data = fieldSanitize(mainTGroupField, mainTNameField, mainDateField);
        
        if(data != null) {
            model.getDataArray().add(data);
            sort();
            reloadModel();
        }
    }
    
    public void addPerformed(rowModel splitter) {
        model.getDataArray().add(splitter);
        sort();
        reloadModel();
    }

    public void removePerformed(int[] selectedRows) {
        model.removeRows(selectedRows);
        reloadModel();
    }

    public void updatePerformed(JTextField mainTGroupField, JTextField mainTNameField, DateTimePicker mainDateField, int selectedRow) {
        rowModel data = fieldSanitize(mainTGroupField, mainTNameField, mainDateField);
        
        if(data != null && selectedRow > -1) {
            model.updateRow(data, selectedRow);
            sort();
            reloadModel();
        }
    }

    public void resetPerformed() {
        model.resetModel();
        reloadModel();
    }

    public void setSave(String type) {
        saveType = (type.equalsIgnoreCase("LOCAL"));
    }

    public void savePerform() {
        if(saveType) {
            saveLocal();
        } else {
            saveDatabase();
        }
    }
    
    private void saveLocal() {
        if (javax.swing.JOptionPane.showConfirmDialog(null, "Ini akan menimpa save yang ada! lanjutkan?", "Overwrite save", javax.swing.JOptionPane.YES_NO_OPTION) == 0) {
            if(!tableFile.exists()) {
                try {
                    tableFile.createNewFile();
                } catch (IOException e) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Error: Gagal membuat file save");
                }
            }
            
            try {
                PrintWriter writer = new PrintWriter(tableFile);
                row = model.getActualModel().getRowCount();
                column = model.getActualModel().getColumnCount();
                int count = 1;
                for (int j = 0; j  < row; j++) {
                    for (int i = 0; i  < column; i++) {
                        switch(count) {
                            case 5:
                                writer.println();
                                count = 1;

                            default:
                                writer.append(String.valueOf(model.getActualModel().getValueAt(j, i)));
                                writer.append(",");
                                writer.flush();
                                count++;
                                break;
                        }
                    }
                }
                writer.close();
            } catch (FileNotFoundException ex) {
                javax.swing.JOptionPane.showMessageDialog(null, "Error: Gagal menyimpan");
            }
        }
    }
    
    public void loadLocal() throws ParseException {
        if (javax.swing.JOptionPane.showConfirmDialog(null, "Ini akan menimpa save yang ada! lanjutkan?", "Overwrite save", javax.swing.JOptionPane.YES_NO_OPTION) == 0) {
            if(tableFile.exists()) {
                try {
                    model.getActualModel().setRowCount(0);
                    resetPerformed();
                    FileReader reader = new FileReader(tableFile);
                    BufferedReader read = new BufferedReader(reader);
                    String x = read.readLine();
                    while(x != null) {
                        String[] Splitter = x.split(",");
                        model.getActualModel().addRow(Splitter);
                        addPerformed(new rowModel(Splitter));
                        x = read.readLine();
                    }
                    read.close();
                    reader.close();
                } catch(IOException e) { 
                    javax.swing.JOptionPane.showMessageDialog(null, "Error: Gagal load");
                }
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Error: File save tidak ditemukan");
            }
        }
    }
    
    private void saveDatabase() {
        // ...
    }

    public void setSort(String type) {
        this.sort = type;
    }
    
    public void sort() {
        // minimum 2 data inside array
        int size = model.getDataArray().size();

        // Pick Sorting Method
        if (size > 1) {
            switch(this.sort.toUpperCase()) {
                case "INSERTION" -> insertionSort(size);
                case "BUBBLE" -> bubbleSort(size);
                case "SELECTION" -> selectionSort(size);
                default -> javax.swing.JOptionPane.showMessageDialog(null, "Error: UNKNOWN SORTING PARAMETER!");
            }
        }
        reloadModel();
    }
    
    private void insertionSort(int size) {
        rowModel temp = (rowModel) model.getDataArray().get(size - 1); // Get the last element
        model.getDataArray().remove(size - 1); // Remove the last element temporarily

        // Start by finding the correct position based on date
        int i = 0;
        while (i < size - 1 && dateCompare(temp.getDate(), ((rowModel) model.getDataArray().get(i)).getDate(), ">")) {
            i++;
        }

        // If there are multiple elements with the same date, compare by time
        int n = i - 1; // Start with the last valid index for same-date comparison
        while (n >= 0 && dateCompare(((rowModel)model.getDataArray().get(n)).getDate(), temp.getDate(), "=")) {
            // If dates are equal, compare times
            if (timeCompare(temp.getTime(), ((rowModel)model.getDataArray().get(n)).getTime(), "<")) {
                i = n; // Move `i` left if `temp`'s time is earlier than `dataArray.get(n).getTime()`
            }
            n--;
        }

        // Insert `temp` at the correct position
        model.getDataArray().add(i, temp);
    }
    
    private void bubbleSort(int size) {
        boolean swapped;
        // Continue sorting until no swaps are made
        for (int i = size - 1; i > 0; i--) {
            swapped = false;

            // Iterate backward from end to the beginning
            for (int j = i; j > 0; j--) {
                rowModel current = (rowModel) model.getDataArray().get(j);
                rowModel prev = (rowModel) model.getDataArray().get(j - 1);

                // Compare by date first
                if (dateCompare(current.getDate(), prev.getDate(), "<")) {
                    // Swap current and previous if current date is earlier than previous
                    model.getDataArray().set(j, prev);
                    model.getDataArray().set(j - 1, current);
                    swapped = true;
                } 
                // If dates are equal, compare by time
                else if (dateCompare(current.getDate(), prev.getDate(), "=") && 
                         timeCompare(current.getTime(), prev.getTime(), "<")) {
                    // Swap if current time is earlier than previous
                    model.getDataArray().set(j, prev);
                    model.getDataArray().set(j - 1, current);
                    swapped = true;
                }
            }

            // If no swaps were made, the array is sorted, stop loop early
            if (!swapped) {
                break;
            }
        }
    }

    
    private void selectionSort(int size) {
        for (int i = 0; i < size - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < size; j++) {
                rowModel current = (rowModel) model.getDataArray().get(minIndex);
                rowModel next = (rowModel) model.getDataArray().get(j);

                // Compare by date first
                if (dateCompare(next.getDate(), current.getDate(), "<")) {
                    minIndex = j;
                } 
                // If dates are equal, compare by time
                else if (dateCompare(next.getDate(), current.getDate(), "=") && 
                         timeCompare(next.getTime(), current.getTime(), "<")) {
                    minIndex = j;
                }
            }

            // Swap the smallest element with the element at the current position
            if (minIndex != i) {
                rowModel temp = (rowModel) model.getDataArray().get(i);
                model.getDataArray().set(i, model.getDataArray().get(minIndex));
                model.getDataArray().set(minIndex, temp);
            }
        }
    }
    
    private boolean dateCompare(Date currentDate, Date anotherDate, String operand) {
        switch(operand.toLowerCase().trim()) {
            case ">" -> {
                return (currentDate).compareTo(anotherDate) >= 0;
            }
            case "<" -> {
                return (currentDate).compareTo(anotherDate) < 0;
            }
            case "=" -> {
                return (currentDate).compareTo(anotherDate) == 0;
            }
            default -> javax.swing.JOptionPane.showMessageDialog(null, "Error: Date Comparator Mismatch!");
        }
        return false;
    }
    
    private boolean timeCompare(LocalTime currentTime, LocalTime anotherTime, String operand) {
        switch(operand.toLowerCase().trim()) {
            case ">" -> {
                return (currentTime).compareTo(anotherTime) >= 0;
            }
            case "<" -> {
                return (currentTime).compareTo(anotherTime) < 0;
            }
            case "=" -> {
                return (currentTime).compareTo(anotherTime) == 0;
            }
            default -> javax.swing.JOptionPane.showMessageDialog(null, "Error: Time Comparator Mismatch!");
        }
        return false;
    }
}