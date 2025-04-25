package model.table;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import model.entities.Teacher;

public class TeacherTableModel extends AbstractTableModel {
    private String[] columnNames = {"Teacher ID", "First Name", "Last Name", "Email"};
    private ArrayList<Teacher> teachers;

    public TeacherTableModel(ArrayList<Teacher> teachers) {
        this.teachers = teachers;
    }
    
    public TeacherTableModel() {
        this.teachers = new ArrayList<Teacher>();
    }

    @Override
    public int getRowCount() {
        return teachers.size();
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
        Teacher teacher = teachers.get(row);
        switch (col) {
            case 0:
                return teacher.getId();
            case 1:
                return teacher.getFirstName();
            case 2:
                return teacher.getLastName();
            case 3:
                return teacher.getEmail();
            default:
                return null;
        }
    }

    public void addRow(Teacher teacher) {
        teachers.add(teacher);
        fireTableRowsInserted(teachers.size() - 1, teachers.size() - 1);
    }

    public void removeRow(int row) {
        teachers.remove(row);
        fireTableRowsDeleted(row, row);
    }
    
    public Teacher getTeacherAt(int row) {
        return teachers.get(row);
    }
    
    public void updateTeacherList(ArrayList<Teacher> teachers) {
        this.teachers = teachers;
        fireTableDataChanged();
    }
}