package model.dao;

import model.entities.User;
import util.database.Database;

public class UserDA {
    
    /**
     * Update user profile picture
     * @param userId User ID
     * @param roleType User role type
     * @param profilePicturePath Path to the profile picture
     * @return true if update was successful, false otherwise
     */
    public boolean updateProfilePicture(String userId, String roleType, String profilePicturePath) {
        String query = "UPDATE tb_user SET profile_picture = ? WHERE id = ? AND role_type = ?";
        
        return new Database().executeQuery(query, stm -> {
            if (profilePicturePath == null) {
                stm.setNull(1, java.sql.Types.VARCHAR);
            } else {
                stm.setString(1, profilePicturePath);
            }
            stm.setString(2, userId);
            stm.setString(3, roleType.toLowerCase());
        }, null);
    }
    
    /**
     * Update user password
     * @param userId User ID
     * @param roleType User role type
     * @param hashedPassword The hashed password
     * @return true if update was successful, false otherwise
     */
    public boolean updatePassword(String userId, String roleType, String hashedPassword) {
        String query = "UPDATE tb_user SET password = ? WHERE id = ? AND role_type = ?";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, hashedPassword);
            stm.setString(2, userId);
            stm.setString(3, roleType.toLowerCase());
        }, null);
    }
}