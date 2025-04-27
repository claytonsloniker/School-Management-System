package model.table;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import model.entities.CourseWithTeacher;

public class CourseTableModel extends AbstractTableModel {
    
	private String[] columnNames = {"Code", "Name", "Max Capacity", "Teacher"};
    private ArrayList<CourseWithTeacher> courses;

    public CourseTableModel(ArrayList<CourseWithTeacher> courses) {
        this.courses = courses;
    }
    
    public CourseTableModel() {
        this.courses = new ArrayList<CourseWithTeacher>();
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
        CourseWithTeacher courseWithTeacher = courses.get(row);
        switch (col) {
            case 0:
                return courseWithTeacher.getCode();
            case 1:
                return courseWithTeacher.getName();
            case 2:
                return courseWithTeacher.getMaxCapacity();
            case 3:
                return courseWithTeacher.getTeacherName();
            default:
                return null;
        }
    }

    public void addRow(CourseWithTeacher courseWithTeacher) {
        courses.add(courseWithTeacher);
        fireTableRowsInserted(courses.size() - 1, courses.size() - 1);
    }

    public void removeRow(int row) {
        courses.remove(row);
        fireTableRowsDeleted(row, row);
    }
    
    public CourseWithTeacher getCourseAt(int row) {
        return courses.get(row);
    }
    
    public void updateCourseList(ArrayList<CourseWithTeacher> courses) {
        this.courses = courses;
        fireTableDataChanged();
    }
}