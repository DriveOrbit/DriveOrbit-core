package lk.driveorbit.DriveOrbit_core.admin.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Data Transfer Object for admin role update requests
 */
public class AdminRoleUpdateRequest {
    
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(admin|super-admin)$", message = "Role must be either 'admin' or 'super-admin'")
    private String role;

    // Default constructor
    public AdminRoleUpdateRequest() {}

    // Constructor
    public AdminRoleUpdateRequest(String role) {
        this.role = role;
    }

    // Getter and setter
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "AdminRoleUpdateRequest{" +
                "role='" + role + '\'' +
                '}';
    }
}
