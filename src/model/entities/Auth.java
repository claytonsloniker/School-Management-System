package model.entities;

public class Auth {
    private String id;
    private String roleType;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String password;
    
    /**
     * @param id
     * @param roleType
     * @param firstName
     * @param lastName
     */
    public Auth(String id, String roleType, String firstName, String lastName) {
        this.id = id;
        this.roleType = roleType;
        this.firstName = firstName;
        this.lastName = lastName;
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
    
    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public String toString() {
        return "Auth [id=" + id + ", roleType=" + roleType + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }
}