package model.dao;

import model.entities.Auth;
import util.database.Database;
import util.security.PasswordUtil;

public class AuthDA {
    
    public Auth authenticateUser(String email, String password) {
        // First, get the user by email to retrieve the stored hash
        String userQuery = "SELECT id, role_type, first_name, last_name, password FROM tb_user WHERE email = ?";
        
        return new Database().executeQuery(userQuery, stm -> {
            stm.setString(1, email);
        }, results -> {
            // If user with this email exists
            if (results.next()) {
                String id = results.getString("id");
                String roleType = results.getString("role_type");
                String firstName = results.getString("first_name");
                String lastName = results.getString("last_name");
                String storedHash = results.getString("password");
                
                // Verify the password
                if (PasswordUtil.verifyPassword(password, storedHash)) {
                    return new Auth(id, roleType, firstName, lastName);
                }
            }
            // No matching user found or password doesn't match
            return null;
        });
    }
}