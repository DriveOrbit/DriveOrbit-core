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

    // Add getter for email
    public String getEmail() {
        return email;
    }
}
