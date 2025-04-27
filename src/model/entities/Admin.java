package model.entities;

public class Admin extends User {
    
    /**
     * Constructor for Admin
     * @param id Admin ID
     * @param firstName First name
     * @param lastName Last name
     * @param email Email address
     */
    public Admin(String id, String firstName, String lastName, String email) {
        super(id, "admin", firstName, lastName, email, "");
    }
    
    /**
     * Constructor for Admin with password
     * @param id Admin ID
     * @param firstName First name
     * @param lastName Last name
     * @param email Email address
     * @param password Password (hashed)
     */
    public Admin(String id, String firstName, String lastName, String email, String password) {
        super(id, "admin", firstName, lastName, email, password);
    }
    
    @Override
    public String toString() {
        return "Admin [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + 
               ", email=" + email + "]";
    }
}