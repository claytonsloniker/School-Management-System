package model.dao;

import java.util.ArrayList;
import model.entities.Course;
import model.entities.Teacher;
import util.database.Database;

public class TeacherCourseDA {
    
    public Teacher getTeacherForCourse(String courseCode) {
        String query = "SELECT t.id, t.first_name, t.last_name " +
                       "FROM tb_user t " +
                       "JOIN tb_teacher_courses tc ON t.id = tc.teacher_id " +
                       "WHERE tc.course_code = ? AND t.role_type = 'TEACHER'";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, courseCode);
        }, results -> {
            if (results.next()) {
                String id = results.getString("id");
                String firstName = results.getString("first_name");
                String lastName = results.getString("last_name");
                
                return new Teacher(id, firstName, lastName);
            }
            return null;
        });
    }
    
    public ArrayList<Teacher> getAllTeachers() {
        String query = "SELECT id, first_name, last_name FROM tb_user WHERE role_type = 'TEACHER'";
        
        return new Database().executeQuery(query, null, results -> {
            ArrayList<Teacher> teachers = new ArrayList<>();
            
            while (results.next()) {
                String id = results.getString("id");
                String firstName = results.getString("first_name");
                String lastName = results.getString("last_name");
                
                Teacher teacher = new Teacher(id, firstName, lastName);
                teachers.add(teacher);
            }
            
            return teachers;
        });
    }
    
    public boolean assignTeacherToCourse(String teacherId, String courseCode) {
        String query = "INSERT INTO tb_teacher_courses (teacher_id, course_code) VALUES (?, ?)";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, teacherId);
            stm.setString(2, courseCode);
        }, null);
    }
    
    public boolean unassignTeacherFromCourse(String courseCode) {
        String query = "DELETE FROM tb_teacher_courses WHERE course_code = ?";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, courseCode);
        }, null);
    }
}