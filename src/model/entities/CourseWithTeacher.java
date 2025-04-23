package model.entities;

public class CourseWithTeacher {
    private Course course;
    private Teacher teacher;
    
    public CourseWithTeacher(Course course, Teacher teacher) {
        this.course = course;
        this.teacher = teacher;
    }
    
    public Course getCourse() {
        return course;
    }
    
    public void setCourse(Course course) {
        this.course = course;
    }
    
    public Teacher getTeacher() {
        return teacher;
    }
    
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
    
    public String getCode() {
        return course.getCode();
    }
    
    public String getName() {
        return course.getName();
    }
    
    public int getMaxCapacity() {
        return course.getMax_capacity();
    }
    
    public String getTeacherName() {
        return teacher != null ? teacher.getFullName() : "Not Assigned";
    }
}