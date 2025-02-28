package lk.driveorbit.DriveOrbit_core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
//@Table(name = "driver")
public class Driver {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userID;
    private String fullName;
    private String password;
    private String email;
    private String role;

}