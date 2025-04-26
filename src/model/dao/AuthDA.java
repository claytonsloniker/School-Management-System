package model.dao;

import model.entities.Auth;
import util.database.Database;
import util.security.PasswordUtil;

public class AuthDA {
    
	public Auth authenticateUser(String email, String password) {
	    String userQuery = "SELECT id, role_type, first_name, last_name, password, profile_picture FROM tb_user WHERE email = ?";
	    
	    return new Database().executeQuery(userQuery, stm -> {
	        stm.setString(1, email);
	    }, results -> {
	        if (results.next()) {
	            String id = results.getString("id");
	            String roleType = results.getString("role_type");
	            String firstName = results.getString("first_name");
	            String lastName = results.getString("last_name");
	            String storedHash = results.getString("password");
	            String profilePicture = results.getString("profile_picture");
	            
	            if (PasswordUtil.verifyPassword(password, storedHash)) {
	                Auth auth = new Auth(id, roleType, firstName, lastName);
	                auth.setProfilePicture(profilePicture);
	                return auth;
	            }
	        }
	        return null;
	    });
	}
}