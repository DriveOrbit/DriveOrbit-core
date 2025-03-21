package lk.driveorbit.DriveOrbit_core.model;

import javax.persistence.*;
import lombok.Data;

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
    
    // Adding explicit getters and setters since there might be issues with Lombok
    
    // Add getter and setter for password
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    // Add getter and setter for id
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    // Existing email getter
    public String getEmail() {
        return email;
    }
}
