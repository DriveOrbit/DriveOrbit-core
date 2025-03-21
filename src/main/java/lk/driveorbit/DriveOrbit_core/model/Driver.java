package lk.driveorbit.DriveOrbit_core.model;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyId;

    @Column(unique = true)
    private String email;

    private String firstName;

    private String lastName;

    private String profilePicture;
    
    // Add password field
    @Column(nullable = false)
    private String password;
    
    // New fields
    @Column(unique = true)
    private String nicNumber;
    
    private String phoneNumber;
    
    private LocalDate joinDate;
    
    private LocalDate dateOfBirth;
    
    private String address;
    
    private String emergencyContact;
    
    // New emergency contact fields
    private String emergencyContactName;
    
    private String emergencyContactRelation;
    
    private String licenseNumber;
    
    private LocalDate licenseIssueDate;
    
    private String licenseType;
    
    private LocalDate licenseExpireDate;
    
    //getters

    public Long getId() {
        return id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getPassword() {
        return password;
    }

    public String getNicNumber() {
        return nicNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public String getEmergencyContactRelation() {
        return emergencyContactRelation;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public LocalDate getLicenseIssueDate() {
        return licenseIssueDate;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public LocalDate getLicenseExpireDate() {
        return licenseExpireDate;
    }


    //setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNicNumber(String nicNumber) {
        this.nicNumber = nicNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public void setEmergencyContactRelation(String emergencyContactRelation) {
        this.emergencyContactRelation = emergencyContactRelation;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public void setLicenseIssueDate(LocalDate licenseIssueDate) {
        this.licenseIssueDate = licenseIssueDate;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public void setLicenseExpireDate(LocalDate licenseExpireDate) {
        this.licenseExpireDate = licenseExpireDate;
    }
}
