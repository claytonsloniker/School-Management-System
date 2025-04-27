package model.entities;

public class Student extends User {
    
    private String status;
    
    /**
     * Constructor for Student
     * @param id Student ID
     * @param firstName First name
     * @param lastName Last name
     * @param email Email address
     * @param password Password (hashed)
     * @param status Account status (active/inactive)
     */
    public Student(String id, String firstName, String lastName, String email, String password, String status) {
        super(id, "student", firstName, lastName, email, password);
        this.status = status;
    }
    
    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Student [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + 
               ", email=" + email + ", status=" + status + "]";
    }
}