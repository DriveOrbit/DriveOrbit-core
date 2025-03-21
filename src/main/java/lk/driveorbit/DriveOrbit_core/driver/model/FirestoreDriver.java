package lk.driveorbit.DriveOrbit_core.driver.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FirestoreDriver {
    private Long id;
    private String companyId;
    private String email;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String password;
    private String nicNumber;
    private String phoneNumber;
    private String joinDate;
    private String dateOfBirth;
    private String address;
    private String emergencyContact;
    private String emergencyContactName;
    private String emergencyContactRelation;
    private String licenseNumber;
    private String licenseIssueDate;
    private String licenseType;
    private String licenseExpireDate;

    // Default constructor required for Firestore
    public FirestoreDriver() {
    }

    // Convert from Driver to FirestoreDriver
    public static FirestoreDriver fromDriver(Driver driver) {
        if (driver == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        FirestoreDriver firestoreDriver = new FirestoreDriver();

        firestoreDriver.setId(driver.getId());
        firestoreDriver.setCompanyId(driver.getCompanyId());
        firestoreDriver.setEmail(driver.getEmail());
        firestoreDriver.setFirstName(driver.getFirstName());
        firestoreDriver.setLastName(driver.getLastName());
        firestoreDriver.setProfilePicture(driver.getProfilePicture());
        firestoreDriver.setPassword(driver.getPassword());
        firestoreDriver.setNicNumber(driver.getNicNumber());
        firestoreDriver.setPhoneNumber(driver.getPhoneNumber());
        
        // Convert LocalDate to String
        if (driver.getJoinDate() != null) {
            firestoreDriver.setJoinDate(driver.getJoinDate().format(formatter));
        }
        if (driver.getDateOfBirth() != null) {
            firestoreDriver.setDateOfBirth(driver.getDateOfBirth().format(formatter));
        }
        if (driver.getLicenseIssueDate() != null) {
            firestoreDriver.setLicenseIssueDate(driver.getLicenseIssueDate().format(formatter));
        }
        if (driver.getLicenseExpireDate() != null) {
            firestoreDriver.setLicenseExpireDate(driver.getLicenseExpireDate().format(formatter));
        }
        
        firestoreDriver.setAddress(driver.getAddress());
        firestoreDriver.setEmergencyContact(driver.getEmergencyContact());
        firestoreDriver.setEmergencyContactName(driver.getEmergencyContactName());
        firestoreDriver.setEmergencyContactRelation(driver.getEmergencyContactRelation());
        firestoreDriver.setLicenseNumber(driver.getLicenseNumber());
        firestoreDriver.setLicenseType(driver.getLicenseType());

        return firestoreDriver;
    }

    // Convert from FirestoreDriver to Driver
    public Driver toDriver() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        Driver driver = new Driver();

        driver.setId(this.getId());
        driver.setCompanyId(this.getCompanyId());
        driver.setEmail(this.getEmail());
        driver.setFirstName(this.getFirstName());
        driver.setLastName(this.getLastName());
        driver.setProfilePicture(this.getProfilePicture());
        driver.setPassword(this.getPassword());
        driver.setNicNumber(this.getNicNumber());
        driver.setPhoneNumber(this.getPhoneNumber());
        
        // Convert String to LocalDate
        if (this.getJoinDate() != null) {
            driver.setJoinDate(LocalDate.parse(this.getJoinDate(), formatter));
        }
        if (this.getDateOfBirth() != null) {
            driver.setDateOfBirth(LocalDate.parse(this.getDateOfBirth(), formatter));
        }
        if (this.getLicenseIssueDate() != null) {
            driver.setLicenseIssueDate(LocalDate.parse(this.getLicenseIssueDate(), formatter));
        }
        if (this.getLicenseExpireDate() != null) {
            driver.setLicenseExpireDate(LocalDate.parse(this.getLicenseExpireDate(), formatter));
        }
        
        driver.setAddress(this.getAddress());
        driver.setEmergencyContact(this.getEmergencyContact());
        driver.setEmergencyContactName(this.getEmergencyContactName());
        driver.setEmergencyContactRelation(this.getEmergencyContactRelation());
        driver.setLicenseNumber(this.getLicenseNumber());
        driver.setLicenseType(this.getLicenseType());

        return driver;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNicNumber() {
        return nicNumber;
    }

    public void setNicNumber(String nicNumber) {
        this.nicNumber = nicNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactRelation() {
        return emergencyContactRelation;
    }

    public void setEmergencyContactRelation(String emergencyContactRelation) {
        this.emergencyContactRelation = emergencyContactRelation;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseIssueDate() {
        return licenseIssueDate;
    }

    public void setLicenseIssueDate(String licenseIssueDate) {
        this.licenseIssueDate = licenseIssueDate;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getLicenseExpireDate() {
        return licenseExpireDate;
    }

    public void setLicenseExpireDate(String licenseExpireDate) {
        this.licenseExpireDate = licenseExpireDate;
    }
}
