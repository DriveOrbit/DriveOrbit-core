package lk.driveorbit.DriveOrbit_core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vehicleId;
    private String vehicleNumber;
    private String vehicleType;
    private String vehicleModel;
    private String vehicleImage;
    private String vehicleStatus;
}