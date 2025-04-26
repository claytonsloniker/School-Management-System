package model.dao;

import model.entities.Admin;
import util.database.Database;

public class AdminDA {
    
    /**
     * Get admin user by ID
     * @param adminId The admin ID
     * @return Admin object or null if not found
     */
    public Admin getAdminById(String adminId) {
        String query = "SELECT id, first_name, last_name, email, profile_picture " +
                      "FROM tb_user " +
                      "WHERE id = ? AND role_type = 'admin'";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, adminId);
        }, results -> {
            if (results.next()) {
                String id = results.getString("id");
                String firstName = results.getString("first_name");
                String lastName = results.getString("last_name");
                String email = results.getString("email");
                String profilePicture = results.getString("profile_picture");
                
                Admin admin = new Admin(id, firstName, lastName, email);
                admin.setProfilePicture(profilePicture);
                return admin;
            }
            return null;
        });
    }
    
    /**
     * Update admin profile picture
     * @param adminId The admin ID
     * @param profilePicturePath Path to the profile picture
     * @return true if update was successful, false otherwise
     */
    public boolean updateAdminProfilePicture(String adminId, String profilePicturePath) {
        String query = "UPDATE tb_user SET profile_picture = ? WHERE id = ? AND role_type = 'admin'";
        
        return new Database().executeQuery(query, stm -> {
            if (profilePicturePath == null) {
                stm.setNull(1, java.sql.Types.VARCHAR);
            } else {
                stm.setString(1, profilePicturePath);
            }
            stm.setString(2, adminId);
        }, null);
    }
    
    /**
     * Update admin information
     * @param admin The admin object with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateAdmin(Admin admin) {
        String query = "UPDATE tb_user " +
                      "SET first_name = ?, last_name = ?, email = ? " +
                      "WHERE id = ? AND role_type = 'admin'";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, admin.getFirstName());
            stm.setString(2, admin.getLastName());
            stm.setString(3, admin.getEmail());
            stm.setString(4, admin.getId());
        }, null);
    }
    
    /**
     * Update admin password
     * @param adminId The admin ID
     * @param hashedPassword The hashed password
     * @return true if update was successful, false otherwise
     */
    public boolean updateAdminPassword(String adminId, String hashedPassword) {
        String query = "UPDATE tb_user SET password = ? WHERE id = ? AND role_type = 'admin'";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, hashedPassword);
            stm.setString(2, adminId);
        }, null);
    }
    
    /**
     * Get admin statistics for dashboard
     * @return Statistics object with counts of students, teachers, courses, etc.
     */
    public AdminStatistics getAdminStatistics() {
        String query = "SELECT " +
                      "(SELECT COUNT(*) FROM tb_user WHERE role_type = 'student') AS student_count, " +
                      "(SELECT COUNT(*) FROM tb_user WHERE role_type = 'teacher') AS teacher_count, " +
                      "(SELECT COUNT(*) FROM tb_course) AS course_count";
        
        return new Database().executeQuery(query, null, results -> {
            if (results.next()) {
                int studentCount = results.getInt("student_count");
                int teacherCount = results.getInt("teacher_count");
                int courseCount = results.getInt("course_count");
                
                return new AdminStatistics(studentCount, teacherCount, courseCount);
            }
            return new AdminStatistics(0, 0, 0);
        });
    }
    
    /**
     * Inner class to hold admin statistics
     */
    public class AdminStatistics {
        private int studentCount;
        private int teacherCount;
        private int courseCount;
        
        public AdminStatistics(int studentCount, int teacherCount, int courseCount) {
            this.studentCount = studentCount;
            this.teacherCount = teacherCount;
            this.courseCount = courseCount;
        }
        
        public int getStudentCount() {
            return studentCount;
        }
        
        public int getTeacherCount() {
            return teacherCount;
        }
        
        public int getCourseCount() {
            return courseCount;
        }
    }
}