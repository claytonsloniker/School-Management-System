package app;

import java.util.ArrayList;

import model.*;
import model.dao.CourseDA;
import model.entities.Course;

public class Main {

	public static void main(String[] args) {
		
		ArrayList<Course> courses = new CourseDA().getCourseList();
		
		System.out.println(courses);

	}

}
