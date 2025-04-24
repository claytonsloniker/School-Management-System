package model.table;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import model.entities.Student;

public class StudentTableModel extends AbstractTableModel {
    private String[] columnNames = {"Student ID", "First Name", "Last Name", "Email", "Password"};
    private ArrayList<Student> students;

    public StudentTableModel(ArrayList<Student> students) {
        this.students = students;
    }
    
    public StudentTableModel() {
        this.students = new ArrayList<Student>();
    }

    @Override
    public int getRowCount() {
        return students.size();
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
        Student student = students.get(row);
        switch (col) {
            case 0:
                return student.getId();
            case 1:
                return student.getFirstName();
            case 2:
                return student.getLastName();
            case 3:
                return student.getEmail();
            case 4:
                return "********"; // For security, don't show actual password
            default:
                return null;
        }
    }

    public void addRow(Student student) {
        students.add(student);
        fireTableRowsInserted(students.size() - 1, students.size() - 1);
    }

    public void removeRow(int row) {
        students.remove(row);
        fireTableRowsDeleted(row, row);
    }
    
    public Student getStudentAt(int row) {
        return students.get(row);
    }
    
    public void updateStudentList(ArrayList<Student> students) {
        this.students = students;
        fireTableDataChanged();
    }
}