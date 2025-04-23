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
