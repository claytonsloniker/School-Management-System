package model.entities;

public class Admin {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String profilePicture;
    
    /**
     * Constructor for Admin
     * @param id Admin ID
     * @param firstName First name
     * @param lastName Last name
     * @param email Email address
     */
    public Admin(String id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }
    
    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * @return the profilePicture
     */
    public String getProfilePicture() {
        return profilePicture;
    }
    
    /**
     * @param profilePicture the profilePicture to set
     */
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    
    /**
     * @return the full name (first + last)
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public String toString() {
        return "Admin [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + 
               ", email=" + email + "]";
    }
}