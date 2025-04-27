package util.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for handling password-related operations
 */
public class PasswordUtil {
    
    /**
     * Hash a password using SHA-256
     * @param password The plain-text password to hash
     * @return The hashed password
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(
                password.getBytes(StandardCharsets.UTF_8));
            
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Fall back to plain text if hashing fails (should never happen with SHA-256)
            return password;
        }
    }
    
    /**
     * Converts a byte array to a hexadecimal string
     * @param hash The byte array to convert
     * @return A hexadecimal string representation
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    /**
     * Verify if a provided password matches a stored hash
     * @param providedPassword The password provided by the user
     * @param storedHash The hash stored in the database
     * @return true if the password matches the hash, false otherwise
     */
    public static boolean verifyPassword(String providedPassword, String storedHash) {
        String hashedProvidedPassword = hashPassword(providedPassword);
        return hashedProvidedPassword.equals(storedHash);
    }
    
    /**
     * Generate a random temporary password
     * @param length Length of the password
     * @return Random temporary password
     */
    public static String generateTemporaryPassword(int length) {
        String upperChars = "ABCDEFGHJKLMNPQRSTUVWXYZ";
        String lowerChars = "abcdefghijkmnopqrstuvwxyz";
        String numbers = "23456789";
        String specialChars = "!@#$%^&*";
        
        String allChars = upperChars + lowerChars + numbers + specialChars;
        Random random = new Random();
        
        // Ensure password has at least one character from each set
        StringBuilder password = new StringBuilder();
        password.append(upperChars.charAt(random.nextInt(upperChars.length())));
        password.append(lowerChars.charAt(random.nextInt(lowerChars.length())));
        password.append(numbers.charAt(random.nextInt(numbers.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));
        
        // Fill remaining length with random characters
        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        
        // Shuffle the password characters
        char[] passwordArray = password.toString().toCharArray();
        for (int i = 0; i < passwordArray.length; i++) {
            int j = random.nextInt(passwordArray.length);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }
        
        return new String(passwordArray);
    }
}