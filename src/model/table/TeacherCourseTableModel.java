package model.table;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import model.entities.Course;

public class TeacherCourseTableModel extends AbstractTableModel {
    private String[] columnNames = {"Code", "Name", "Max Capacity", "Status"};
    private ArrayList<Course> courses;

    public TeacherCourseTableModel(ArrayList<Course> courses) {
        this.courses = courses;
    }
    
    public TeacherCourseTableModel() {
        this.courses = new ArrayList<Course>();
    }

    @Override
    public int getRowCount() {
        return courses.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Course course = courses.get(row);
        switch (col) {
            case 0:
                return course.getCode();
            case 1:
                return course.getName();
            case 2:
                return course.getMax_capacity();
            case 3:
                return course.getStatus();
            default:
                return null;
        }
    }

    public void updateCourseList(ArrayList<Course> courses) {
        this.courses = courses;
        fireTableDataChanged();
    }
    
    public Course getCourseAt(int row) {
        return courses.get(row);
    }
}