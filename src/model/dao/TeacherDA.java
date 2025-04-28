package model.dao;

import java.util.ArrayList;
import model.entities.Course;
import model.entities.Student;
import model.entities.Teacher;
import util.database.Database;

public class TeacherDA {
    
    /**
     * Get list of all teachers
     * @return ArrayList of Teacher objects
     */
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
    
    /**
     * Add a new teacher to the database
     * @param teacher The teacher to add
     * @return true if the teacher was added successfully, false otherwise
     */
    public boolean addTeacher(Teacher teacher) {
        String query = "INSERT INTO tb_user (first_name, last_name, email, password, role_type, first_login) " +
                      "VALUES (?, ?, ?, ?, 'teacher', TRUE)";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, teacher.getFirstName());
            stm.setString(2, teacher.getLastName());
            stm.setString(3, teacher.getEmail());
            stm.setString(4, teacher.getPassword());
        }, null);
    }
    
    /**
     * Update an existing teacher in the database
     * @param teacher The teacher to update
     * @return true if the teacher was updated successfully, false otherwise
     */
    public boolean updateTeacher(Teacher teacher) {
        String query = "UPDATE tb_user " +
                      "SET first_name = ?, last_name = ?, email = ?, password = ? " +
                      "WHERE id = ? AND role_type = 'teacher'";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, teacher.getFirstName());
            stm.setString(2, teacher.getLastName());
            stm.setString(3, teacher.getEmail());
            stm.setString(4, teacher.getPassword());
            stm.setString(5, teacher.getId());
        }, null);
    }
    
    /**
     * Delete a teacher from the database
     * @param teacherId The ID of the teacher to delete
     * @return true if the teacher was deleted successfully, false otherwise
     */
    public boolean deleteTeacher(String teacherId) {
        String query = "DELETE FROM tb_user WHERE id = ? AND role_type = 'teacher'";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, teacherId);
        }, null);
    }
    
    /**
     * Get a teacher by ID
     * @param teacherId The teacher ID
     * @return Teacher object or null if not found
     */
    public Teacher getTeacherById(String teacherId) {
        String query = "SELECT id, first_name, last_name, email, profile_picture " +
                      "FROM tb_user " +
                      "WHERE id = ? AND role_type = 'teacher'";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, teacherId);
        }, results -> {
            if (results.next()) {
                String id = results.getString("id");
                String firstName = results.getString("first_name");
                String lastName = results.getString("last_name");
                String email = results.getString("email");
                String profilePicture = results.getString("profile_picture");
                
                Teacher teacher = new Teacher(id, firstName, lastName, email);
                teacher.setProfilePicture(profilePicture);
                return teacher;
            }
            return null;
        });
    }
    
    /**
     * Get all courses assigned to a specific teacher
     * @param teacherId The teacher ID
     * @return ArrayList of Course objects
     */
    public ArrayList<Course> getCoursesForTeacher(String teacherId) {
        String query = "SELECT c.code, c.name, c.description, c.max_capacity, c.status " +
                      "FROM tb_teacher_courses tc " +
                      "JOIN tb_course c ON tc.course_code = c.code " +
                      "WHERE tc.teacher_id = ?";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, teacherId);
        }, results -> {
            ArrayList<Course> courses = new ArrayList<>();
            
            while (results.next()) {
                String code = results.getString("code");
                String name = results.getString("name");
                String description = results.getString("description");
                int maxCapacity = results.getInt("max_capacity");
                String status = results.getString("status");
                
                Course course = new Course(code, name, description, maxCapacity, status);
                courses.add(course);
            }
            
            return courses;
        });
    }
    
    /**
     * Get all students enrolled in a specific course
     * @param courseCode The course code
     * @return ArrayList of Student objects
     */
    public ArrayList<Student> getStudentsForCourse(String courseCode) {
        String query = "SELECT u.id, u.first_name, u.last_name, u.email, e.enrollment_date " +
                      "FROM tb_enrollment e " +
                      "JOIN tb_user u ON e.student_id = u.id " +
                      "WHERE e.course_code = ? AND u.role_type = 'student'";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, courseCode);
        }, results -> {
            ArrayList<Student> students = new ArrayList<>();
            
            while (results.next()) {
                String id = results.getString("id");
                String firstName = results.getString("first_name");
                String lastName = results.getString("last_name");
                String email = results.getString("email");
                
                Student student = new Student(id, firstName, lastName, email, "", "active");
                students.add(student);
            }
            
            return students;
        });
    }
    
    /**
     * Update teacher profile picture
     * @param teacherId The teacher ID
     * @param profilePicturePath Path to the profile picture
     * @return true if update was successful, false otherwise
     */
    public boolean updateTeacherProfilePicture(String teacherId, String profilePicturePath) {
        String query = "UPDATE tb_user SET profile_picture = ? WHERE id = ? AND role_type = 'teacher'";
        
        return new Database().executeQuery(query, stm -> {
            if (profilePicturePath == null) {
                stm.setNull(1, java.sql.Types.VARCHAR);
            } else {
                stm.setString(1, profilePicturePath);
            }
            stm.setString(2, teacherId);
        }, null);
    }
    
    /**
     * Update teacher password
     * @param teacherId The teacher ID
     * @param hashedPassword The hashed password
     * @return true if update was successful, false otherwise
     */
    public boolean updateTeacherPassword(String teacherId, String hashedPassword) {
        String query = "UPDATE tb_user SET password = ? WHERE id = ? AND role_type = 'teacher'";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, hashedPassword);
            stm.setString(2, teacherId);
        }, null);
    }
}