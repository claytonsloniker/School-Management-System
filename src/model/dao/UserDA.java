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
    
    /**
     * Check if an email exists in the system
     * @param email The email to check
     * @return true if the email exists, false otherwise
     */
    public boolean doesEmailExist(String email) {
        String query = "SELECT COUNT(*) AS count FROM tb_user WHERE email = ?";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, email);
        }, results -> {
            if (results.next()) {
                int count = results.getInt("count");
                return count > 0;
            }
            return false;
        });
    }
    
    /**
     * Update a user's password
     * @param email The user's email
     * @param hashedPassword The new hashed password
     * @return true if the update was successful, false otherwise
     */
    public boolean updatePasswordByEmail(String email, String hashedPassword) {
        String query = "UPDATE tb_user SET password = ? WHERE email = ?";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, hashedPassword);
            stm.setString(2, email);
        }, null);
    }
    
    /**
     * Get user role by email
     * @param email The user's email
     * @return The user's role (student, teacher, admin) or null if not found
     */
    public String getUserRoleByEmail(String email) {
        String query = "SELECT role_type FROM tb_user WHERE email = ?";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, email);
        }, results -> {
            if (results.next()) {
                return results.getString("role_type");
            }
            return null;
        });
    }
    
    /**
     * Check if this is the user's first login
     * @param userId The user ID
     * @return true if this is the first login, false otherwise
     */
    public boolean isFirstLogin(String userId) {
        String query = "SELECT first_login FROM tb_user WHERE id = ?";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, userId);
        }, results -> {
            if (results.next()) {
                return results.getBoolean("first_login");
            }
            return false;
        });
    }

    /**
     * Update the user's first login status
     * @param userId The user ID
     * @param firstLogin The first login status
     * @return true if the update was successful, false otherwise
     */
    public boolean updateFirstLoginStatus(String userId, boolean firstLogin) {
        String query = "UPDATE tb_user SET first_login = ? WHERE id = ?";
        
        return new Database().executeQuery(query, stm -> {
            stm.setBoolean(1, firstLogin);
            stm.setString(2, userId);
        }, null);
    }

    /**
     * Update user password and first login status
     * @param userId The user ID
     * @param hashedPassword The new hashed password
     * @return true if the update was successful, false otherwise
     */
    public boolean updatePasswordAndFirstLoginStatus(String userId, String hashedPassword) {
        String query = "UPDATE tb_user SET password = ?, first_login = FALSE WHERE id = ?";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, hashedPassword);
            stm.setString(2, userId);
        }, null);
    }
}