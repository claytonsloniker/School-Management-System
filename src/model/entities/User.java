package model.entities;

/**
 * Base User class for all user types in the system
 */
public abstract class User {
    protected String id;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected String profilePicture;
    protected String roleType;
    
    /**
     * Constructor for User
     * @param id User ID
     * @param roleType Role type (admin, teacher, student)
     * @param firstName First name
     * @param lastName Last name
     * @param email Email address
     * @param password Password (hashed)
     */
    public User(String id, String roleType, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.roleType = roleType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
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
     * @return the roleType
     */
    public String getRoleType() {
        return roleType;
    }
    
    /**
     * @param roleType the roleType to set
     */
    public void setRoleType(String roleType) {
        this.roleType = roleType;
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
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * @return the full name (first + last)
     */
    public String getFullName() {
        return firstName + " " + lastName;
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
    
    @Override
    public String toString() {
        return "User [id=" + id + ", roleType=" + roleType + ", firstName=" + firstName + 
               ", lastName=" + lastName + ", email=" + email + "]";
    }
}