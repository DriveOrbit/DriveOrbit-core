package lk.driveorbit.DriveOrbit_core.dto;

public class LoginRequest {
    private String companyId;
    private String password;

    // Getters and Setters
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}