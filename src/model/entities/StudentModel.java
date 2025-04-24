package model.entities;

import java.util.ArrayList;

public class StudentModel {
	
	private ArrayList<Student> students;
	
	public StudentModel() {
		students = new ArrayList<Student>();
	}
	
	/**
	 * Add method to add a new student
	 * to the students array list
	 */
	public void addStudent(Student newStudent) {
		students.add(newStudent);
	}
	
	public void removeStudent(Student studentToRemove) {
		students.remove(studentToRemove);
	}
	
	public ArrayList<Student> getStudentList() {
		return students;
	}
}
