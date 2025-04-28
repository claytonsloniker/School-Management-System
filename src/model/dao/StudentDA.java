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
	    String query = "INSERT INTO tb_user (first_name, last_name, email, password, role_type, first_login) " +
	                  "VALUES (?, ?, ?, ?, 'student', TRUE)";
	    
	    return new Database().executeQuery(query, stm -> {
	        stm.setString(1, student.getFirstName());
	        stm.setString(2, student.getLastName());
	        stm.setString(3, student.getEmail());
	        stm.setString(4, student.getPassword());
	    }, null);
	}
    
    public boolean updateStudent(Student student) {
        String query = "UPDATE tb_user " +
                      "SET first_name = ?, last_name = ?, email = ?, password = ? " +
                      "WHERE id = ? AND role_type = 'student'";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, student.getFirstName());
            stm.setString(2, student.getLastName());
            stm.setString(3, student.getEmail());
            stm.setString(4, student.getPassword());
            stm.setString(5, student.getId());
        }, null);
    }

    public boolean deleteStudent(String studentId) {
        String query = "DELETE FROM tb_user WHERE id = ? AND role_type = 'student'";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, studentId);
        }, null);
    }
    
    public boolean updateStudentProfilePicture(String studentId, String profilePicturePath) {
        String query = "UPDATE tb_user SET profile_picture = ? WHERE id = ? AND role_type = 'student'";
        
        return new Database().executeQuery(query, stm -> {
            if (profilePicturePath == null) {
                stm.setNull(1, java.sql.Types.VARCHAR);
            } else {
                stm.setString(1, profilePicturePath);
            }
            stm.setString(2, studentId);
        }, null);
    }
    
    /**
     * Get student by ID
     * @param studentId The student ID
     * @return Student object or null if not found
     */
    public Student getStudentById(String studentId) {
        String query = "SELECT id, first_name, last_name, email, password, profile_picture " +
                      "FROM tb_user " +
                      "WHERE id = ? AND role_type = 'student'";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, studentId);
        }, results -> {
            if (results.next()) {
                String id = results.getString("id");
                String firstName = results.getString("first_name");
                String lastName = results.getString("last_name");
                String email = results.getString("email");
                String password = results.getString("password");
                String profilePicture = results.getString("profile_picture");
                
                Student student = new Student(id, firstName, lastName, email, password, "active");
                student.setProfilePicture(profilePicture);
                return student;
            }
            return null;
        });
    }
    
    /**
     * Get courses in which a student is enrolled
     * @param studentId The student ID
     * @return ArrayList of Course objects
     */
    public ArrayList<Course> getCoursesForStudent(String studentId) {
        String query = "SELECT c.code, c.name, c.description, c.max_capacity, c.status, e.enrollment_date " +
                      "FROM tb_enrollment e " +
                      "JOIN tb_course c ON e.course_code = c.code " +
                      "WHERE e.student_id = ?";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, studentId);
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
     * Get available courses for a student to enroll in (not full and not already enrolled)
     * @param studentId The student ID
     * @return ArrayList of CourseWithTeacher objects
     */
    public ArrayList<CourseWithTeacher> getAvailableCoursesForStudent(String studentId) {
        String query = "SELECT c.code, c.name, c.description, c.max_capacity, c.status, " +
                      "(SELECT COUNT(*) FROM tb_enrollment e WHERE e.course_code = c.code) AS current_enrollment " +
                      "FROM tb_course c " +
                      "WHERE c.status = 'active' " +
                      "AND c.code NOT IN (SELECT course_code FROM tb_enrollment WHERE student_id = ?) " +
                      "HAVING current_enrollment < c.max_capacity OR c.max_capacity IS NULL";
        
        ArrayList<Course> courses = new Database().executeQuery(query, stm -> {
            stm.setString(1, studentId);
        }, results -> {
            ArrayList<Course> courseList = new ArrayList<>();
            
            while (results.next()) {
                String code = results.getString("code");
                String name = results.getString("name");
                String description = results.getString("description");
                int maxCapacity = results.getInt("max_capacity");
                String status = results.getString("status");
                
                Course course = new Course(code, name, description, maxCapacity, status);
                courseList.add(course);
            }
            
            return courseList;
        });
        
        ArrayList<CourseWithTeacher> coursesWithTeachers = new ArrayList<>();
        TeacherCourseDA teacherCourseDA = new TeacherCourseDA();
        
        for (Course course : courses) {
            Teacher teacher = teacherCourseDA.getTeacherForCourse(course.getCode());
            CourseWithTeacher courseWithTeacher = new CourseWithTeacher(course, teacher);
            coursesWithTeachers.add(courseWithTeacher);
        }
        
        return coursesWithTeachers;
    }
    
    /**
     * Check if a student is already enrolled in a course
     * @param studentId The student ID
     * @param courseCode The course code
     * @return true if the student is already enrolled, false otherwise
     */
    public boolean isStudentEnrolledInCourse(String studentId, String courseCode) {
        String query = "SELECT COUNT(*) AS is_enrolled " +
                      "FROM tb_enrollment " +
                      "WHERE student_id = ? AND course_code = ?";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, studentId);
            stm.setString(2, courseCode);
        }, results -> {
            if (results.next()) {
                int count = results.getInt("is_enrolled");
                return count > 0;
            }
            return false;
        });
    }
    
    /**
     * Enroll a student in a course
     * @param studentId The student ID
     * @param courseCode The course code
     * @return true if enrollment was successful, false otherwise
     */
    public boolean enrollStudentInCourse(String studentId, String courseCode) {
        String query = "INSERT INTO tb_enrollment (student_id, course_code, enrollment_date) " +
                      "VALUES (?, ?, CURDATE())";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, studentId);
            stm.setString(2, courseCode);
        }, null);
    }
    
    /**
     * Get teachers for a specific course
     * @param courseCode The course code
     * @return ArrayList of Teacher objects
     */
    public ArrayList<Teacher> getTeachersForCourse(String courseCode) {
        String query = "SELECT u.id, u.first_name, u.last_name, u.email " +
                      "FROM tb_teacher_courses tc " +
                      "JOIN tb_user u ON tc.teacher_id = u.id " +
                      "WHERE tc.course_code = ? AND u.role_type = 'teacher'";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, courseCode);
        }, results -> {
            ArrayList<Teacher> teachers = new ArrayList<>();
            
            while (results.next()) {
                String id = results.getString("id");
                String firstName = results.getString("first_name");
                String lastName = results.getString("last_name");
                String email = results.getString("email");
                
                Teacher teacher = new Teacher(id, firstName, lastName, email);
                teachers.add(teacher);
            }

            return teachers;
        });
    }
}