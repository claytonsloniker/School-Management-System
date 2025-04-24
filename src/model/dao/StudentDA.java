package model.dao;

import java.util.ArrayList;
import model.entities.*;
import util.database.Database;

public class StudentDA {
    
	public ArrayList<Student> getStudentList() {
	    String query = "SELECT id, first_name, last_name, email " +
	                  "FROM tb_user " +
	                  "WHERE role_type = 'student'";

	    return new Database().executeQuery(query, null, results -> {
	        ArrayList<Student> studentList = new ArrayList<>();

	        while (results.next()) {
	            String id = results.getString("id");
	            String firstName = results.getString("first_name");
	            String lastName = results.getString("last_name");
	            String email = results.getString("email");
	            
	            // Create Student with default password and status since they're not in the query
	            Student student = new Student(id, firstName, lastName, email, "", "active");
	            studentList.add(student);
	        }
	        return studentList;
	    });
	}
    
    public boolean addStudent(Student student) {
        String query = "INSERT INTO tb_user (first_name, last_name, email, password, role_type) " +
                      "VALUES (?, ?, ?, ?, 'student')";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, student.getFirstName());
            stm.setString(2, student.getLastName());
            stm.setString(3, student.getEmail());
            stm.setString(4, student.getPassword());
        }, null);
    }
    
    public boolean updateStudent(Student student) {
        String query = "UPDATE tb_user " +
                      "SET first_name = ?, last_name = ?, email = ? " +
                      "WHERE id = ? AND role_type = 'student'";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, student.getFirstName());
            stm.setString(2, student.getLastName());
            stm.setString(3, student.getEmail());
            stm.setString(4, student.getId());
        }, null);
    }

    public boolean deleteStudent(String studentId) {
        String query = "DELETE FROM tb_user WHERE id = ? AND role_type = 'student'";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, studentId);
        }, null);
    }
}