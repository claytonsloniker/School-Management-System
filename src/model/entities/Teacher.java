package model.entities;

public class Teacher extends User {
    
    private String status;
    
    /**
     * Constructor for Teacher with minimal information
     * @param id Teacher ID
     * @param firstName First name
     * @param lastName Last name
     */
    public Teacher(String id, String firstName, String lastName) {
        super(id, "teacher", firstName, lastName, "", "");
        this.status = "active";
    }
    
    /**
     * Constructor for Teacher with email
     * @param id Teacher ID
     * @param firstName First name
     * @param lastName Last name
     * @param email Email address
     */
    public Teacher(String id, String firstName, String lastName, String email) {
        super(id, "teacher", firstName, lastName, email, "");
        this.status = "active";
    }
    
    /**
     * Full constructor for Teacher
     * @param id Teacher ID
     * @param firstName First name
     * @param lastName Last name
     * @param email Email address
     * @param password Password (hashed)
     * @param status Account status (active/inactive)
     */
    public Teacher(String id, String firstName, String lastName, String email, String password, String status) {
        super(id, "teacher", firstName, lastName, email, password);
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
        return getFullName();
    }
}