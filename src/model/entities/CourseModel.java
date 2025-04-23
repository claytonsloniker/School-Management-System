package model.entities;

import java.util.ArrayList;

public class CourseModel {
	
	private ArrayList<Course> courses;
	
	public CourseModel() {
		courses = new ArrayList<Course>();
	}
	
	/**
	 * Add method to add a new course
	 * to the course array list
	 */
	public void addCourse(Course newCourse) {
		courses.add(newCourse);
	}
	
	public void removeCourse(Course courseToRemove) {
		courses.remove(courseToRemove);
	}
	
	public ArrayList<Course> getCourseList() {
		return courses;
	}
	
}
