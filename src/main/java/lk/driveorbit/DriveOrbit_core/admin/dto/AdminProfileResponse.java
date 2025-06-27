package lk.driveorbit.DriveOrbit_core.admin.dto;

/**
 * Data Transfer Object for admin profile responses
 */
public class AdminProfileResponse {
    
    private String uid;
    private String email;
    private String name;
    private String role;
    private boolean isAdmin;
    private boolean isSuperAdmin;

    // Default constructor
    public AdminProfileResponse() {}

    // Constructor with all fields
    public AdminProfileResponse(String uid, String email, String name, String role, boolean isAdmin, boolean isSuperAdmin) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.role = role;
        this.isAdmin = isAdmin;
        this.isSuperAdmin = isSuperAdmin;
    }

    // Getters and setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isSuperAdmin() {
        return isSuperAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        isSuperAdmin = superAdmin;
    }

    @Override
    public String toString() {
        return "AdminProfileResponse{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", isAdmin=" + isAdmin +
                ", isSuperAdmin=" + isSuperAdmin +
                '}';
    }
}
