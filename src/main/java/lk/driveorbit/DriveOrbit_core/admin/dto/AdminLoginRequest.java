package lk.driveorbit.DriveOrbit_core.admin.dto;

import javax.validation.constraints.NotBlank;

/**
 * Data Transfer Object for admin login requests
 */
public class AdminLoginRequest {
    
    @NotBlank(message = "ID token is required")
    private String idToken;

    // Default constructor
    public AdminLoginRequest() {}

    // Constructor
    public AdminLoginRequest(String idToken) {
        this.idToken = idToken;
    }

    // Getter and setter
    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    @Override
    public String toString() {
        return "AdminLoginRequest{" +
                "idToken='" + (idToken != null ? "[HIDDEN]" : "null") + '\'' +
                '}';
    }
}
