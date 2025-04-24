package model.dao;

import java.util.ArrayList;
import model.entities.Course;
import model.entities.CourseWithTeacher;
import model.entities.Teacher;
import util.database.Database;

public class CourseDA {
	
	public ArrayList<Course> getCourseList() {

		String query = "SELECT * FROM tb_course";

		return new Database().executeQuery(query, null, results -> {
			ArrayList<Course> courseList = new ArrayList<>();

			while (results.next()) {
				String cd = results.getString("code");
				String nm = results.getString("name");
				String dc = results.getString("description");
				int mc = results.getInt("max_capacity");
				String st = results.getString("status");

				Course course = new Course(cd, nm, dc, mc, st);
				courseList.add(course);
			}
			return courseList;
		});
	}
	
	/**
     * Add a new course to the database
     * @param course The course to add
     * @return true if the course was added successfully, false otherwise
     */
    public boolean addCourse(Course course) {
        String query = "INSERT INTO tb_course (code, name, description, max_capacity, status) VALUES (?, ?, ?, ?, 'active')";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, course.getCode());
            stm.setString(2, course.getName());
            stm.setString(3, course.getDescription());
            stm.setInt(4, course.getMax_capacity());
        }, null);
    }
    
    /**
     * Update existing course in the db
     * @param course The course to update
     * @return true if the course was updated successfully, false otherwise
     */
    public boolean editCourse(Course course) {
        String query = "UPDATE tb_course \r\n"
        		+ "SET name = ?, description = ?, max_capacity = ?, status = ? \r\n"
        		+ "WHERE code = ?";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, course.getName());
            stm.setString(2, course.getDescription());
            stm.setInt(3, course.getMax_capacity());
            stm.setString(4, course.getStatus());
            stm.setString(5, course.getCode());
        }, null);
    }
    
    /**
     * Delete existing course in the db
     * @param course The course to delete
     * @return true if the course was deleted successfully, false otherwise
     */
    public boolean deleteCourse(Course course) {
        String query = "DELETE FROM tb_course WHERE code = ?";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, course.getCode());
        }, null);
    }
	
	public ArrayList<CourseWithTeacher> getCoursesWithTeachers() {
        ArrayList<Course> courses = getCourseList();
        ArrayList<CourseWithTeacher> coursesWithTeachers = new ArrayList<>();
        TeacherCourseDA teacherCourseDA = new TeacherCourseDA();
        
        for (Course course : courses) {
            Teacher teacher = teacherCourseDA.getTeacherForCourse(course.getCode());
            CourseWithTeacher courseWithTeacher = new CourseWithTeacher(course, teacher);
            coursesWithTeachers.add(courseWithTeacher);
        }
        
        return coursesWithTeachers;
    }
}
