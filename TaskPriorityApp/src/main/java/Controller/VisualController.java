/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author PREDATOR
 */
public class VisualController {
    // Global Variable
    private final DefaultTableModel model;
    private final ArrayList<visualModel> dataArray;
    private String sort = "INSERTION";
    
    
    public VisualController(TableModel model) {
        this.model = (DefaultTableModel) model;
        this.dataArray = new ArrayList<>();
        modelToArray();
        reloadModel();
    }

    public void sort() {
        // minimum 2 data inside array
        int size = dataArray.size();

        // Pick Sorting Method
        if (size > 1) {
            switch(this.sort.toUpperCase()) {
                case "INSERTION":
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            insertionSort();
                        }
                    }).start();
                    break;
                case "BUBBLE":
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            bubbleSort();
                        }
                    }).start();
                    break;
                case "SELECTION":
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            selectionSort();
                        }
                    }).start();
                    break;
                default:
                    javax.swing.JOptionPane.showMessageDialog(null, "Error: UNKNOWN SORTING PARAMETER!");
                    break;
            }
        }
        reloadModel();
    }
    
    private void insertionSort() {
        for (int i = 1; i < dataArray.size(); i++) {
            int key = dataArray.get(i).getComparable(); // The element to be inserted into the sorted portion
            int j = i - 1;

            // Compare only the integer values and move elements that are greater than key
            while (j >= 0 && dataArray.get(j).getComparable() > key) {
                j--;
            }
            
            if (j + 1 != i) {
                // Remove the element at index i
                visualModel removedElement = dataArray.remove(i);

                // Insert the removed element at index j + 1 (the correct position)
                dataArray.add(j + 1, removedElement);
                
                reloadModel();
                sleep();
            }
        }
    }
    
    private void bubbleSort() {
        boolean swapped;
        for (int i = 0; i < dataArray.size() - 1; i++) {
            swapped = false;

            // Compare adjacent elements and swap them if necessary
            for (int j = 0; j < dataArray.size() - i - 1; j++) {
                if (dataArray.get(j).getComparable() > dataArray.get(j + 1).getComparable()) {
                    // Swap the elements at indices j and j+1
                    visualModel temp = dataArray.get(j);
                    dataArray.set(j, dataArray.get(j + 1));
                    dataArray.set(j + 1, temp);
                    swapped = true;
                    
                    reloadModel();
                    sleep();
                }
            }

            // If no elements were swapped, the list is sorted
            if (!swapped) {
                break;
            }
        }
    }
    
    private void selectionSort() {
        for (int i = 0; i < dataArray.size() - 1; i++) {
            int minIndex = i; // Assume the first element is the minimum

            // Find the index of the minimum element in the unsorted portion of the list
            for (int j = i + 1; j < dataArray.size(); j++) {
                if (dataArray.get(j).getComparable() < dataArray.get(minIndex).getComparable()) {
                    minIndex = j; // Update minIndex if a smaller element is found
                }
            }

            // Swap only if minIndex is not the current index i
            if (minIndex != i) {
                visualModel temp = dataArray.get(i);
                dataArray.set(i, dataArray.get(minIndex));
                dataArray.set(minIndex, temp);
                
                reloadModel();
                sleep();
            }
        }
    }

    public void setSort(String type) {
        this.sort = type;
    }
    
    public void modelToArray() {
        String[] Splitter = new String[this.model.getRowCount()];
        for(int i = 0; i < this.model.getRowCount(); i++) {
            for(int j = 0; j < this.model.getColumnCount(); j++) {
                Splitter[j] = this.model.getValueAt(i, j).toString();
            }
            
            dataArray.add(new visualModel(Splitter));
        }
    }
    
    public void reloadModel() {
        this.model.setRowCount(0);
        
        this.dataArray.forEach(n -> {
            this.model.addRow((n).toStringArray());
        });
    }
    
    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            //...
        }
    }

    public void reset() {
        model.setRowCount(0);
        dataArray.clear();
        dataArray.add(new visualModel(new String[]{"1","Alpha","65"}));
        dataArray.add(new visualModel(new String[]{"2","Bravo","25"}));
        dataArray.add(new visualModel(new String[]{"3","Charlie","95"}));
        dataArray.add(new visualModel(new String[]{"4","Delta","20"}));
        dataArray.add(new visualModel(new String[]{"5","Echo","35"}));
        dataArray.add(new visualModel(new String[]{"6","Foxtrot","100"}));
        dataArray.add(new visualModel(new String[]{"7","Golf","105"}));
        dataArray.add(new visualModel(new String[]{"8","Hotel","230"}));
        dataArray.add(new visualModel(new String[]{"9","India","10"}));
        dataArray.add(new visualModel(new String[]{"10","Juliet","5"}));
        reloadModel();
    }
}

class visualModel {
    private final int orgPosition;
    private final String name;
    private final int comparable;
    
    public visualModel(String[] data) {
        this.orgPosition = Integer.parseInt(data[0]);
        this.name = data[1];
        this.comparable = Integer.parseInt(data[2]);
    }
    
    public int getOrgPosition() {
        return this.orgPosition;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getComparable() {
        return this.comparable;
    }
    
    public String[] toStringArray() {
        return new String[]{String.valueOf(this.orgPosition), this.name, String.valueOf(this.comparable)};
    }
}
