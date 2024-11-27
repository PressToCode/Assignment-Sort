/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import com.github.lgooddatepicker.components.DateTimePicker;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.Date;
import javax.swing.JTextField;

/**
 *
 * @author PREDATOR
 */
public class rowModel {
    // Private Variables
    private final String groupname;
    private final String taskname;
    private final Date date;
    private final String convertDate;
    private final LocalTime time;
    private final String convertTime;
    
    public rowModel(JTextField mainTGroupField, JTextField mainTNameField, DateTimePicker mainDateField) {
        this.groupname = mainTGroupField.getText();
        this.taskname = mainTNameField.getText();
        this.date = mainDateField.getDatePicker().convert().getDateWithDefaultZone();
        this.convertDate = java.text.SimpleDateFormat.getDateInstance().format(date);
        this.time = mainDateField.getTimePicker().getTime();
        this.convertTime = this.time.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
    }
    
    public rowModel(String group, String name, Date date, LocalTime time) {
        this.groupname = group;
        this.taskname = name;
        this.date = date;
        this.convertDate = java.text.SimpleDateFormat.getDateInstance().format(date);
        this.time = (time == null)? java.time.LocalTime.now():time;
        this.convertTime = this.time.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
    }
    
    public rowModel(String[] Splitter) throws ParseException {
        this.groupname = Splitter[0];
        this.taskname = Splitter[1];
        this.convertDate = Splitter[2];
        this.date = java.text.SimpleDateFormat.getDateInstance().parse(convertDate);
        this.convertTime = Splitter[3];
        this.time = LocalTime.from(java.time.format.DateTimeFormatter.ofPattern("HH:mm").parse(convertTime));
    }
    
    public String getGroup() {
        return this.groupname;
    }
    
    public String getTask() {
        return this.taskname;
    }
    
    public Date getDate() {
        return this.date;        
    }
    
    public LocalTime getTime() {
        return this.time;
    }
    
    public String[] toStringArray() {
        return new String[]{groupname, taskname, convertDate, convertTime};
    }
}
