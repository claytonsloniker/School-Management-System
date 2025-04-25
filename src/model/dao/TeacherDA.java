package model.dao;

import java.util.ArrayList;
import model.entities.Teacher;
import util.database.Database;

public class TeacherDA {
    
    public ArrayList<Teacher> getTeacherList() {
        String query = "SELECT id, first_name, last_name, email " +
                      "FROM tb_user " +
                      "WHERE role_type = 'teacher'";

        return new Database().executeQuery(query, null, results -> {
            ArrayList<Teacher> teacherList = new ArrayList<>();

            while (results.next()) {
                String id = results.getString("id");
                String firstName = results.getString("first_name");
                String lastName = results.getString("last_name");
                String email = results.getString("email");
                
                // Create Teacher with default password and status since they're not in the query
                Teacher teacher = new Teacher(id, firstName, lastName, email);
                teacherList.add(teacher);
            }
            return teacherList;
        });
    }
    
    public boolean addTeacher(Teacher teacher) {
        String query = "INSERT INTO tb_user (first_name, last_name, email, password, role_type) " +
                      "VALUES (?, ?, ?, ?, 'teacher')";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, teacher.getFirstName());
            stm.setString(2, teacher.getLastName());
            stm.setString(3, teacher.getEmail());
            stm.setString(4, teacher.getPassword());
        }, null);
    }
    
    public boolean updateTeacher(Teacher teacher) {
        String query = "UPDATE tb_user " +
                      "SET first_name = ?, last_name = ?, email = ? " +
                      "WHERE id = ? AND role_type = 'teacher'";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, teacher.getFirstName());
            stm.setString(2, teacher.getLastName());
            stm.setString(3, teacher.getEmail());
            stm.setString(4, teacher.getId());
        }, null);
    }
    
    public boolean deleteTeacher(String teacherId) {
        String query = "DELETE FROM tb_user WHERE id = ? AND role_type = 'teacher'";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, teacherId);
        }, null);
    }
}