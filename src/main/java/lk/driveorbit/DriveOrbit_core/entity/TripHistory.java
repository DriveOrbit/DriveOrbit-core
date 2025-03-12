package lk.driveorbit.DriveOrbit_core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class TripHistory {
    @Id
    private String historyId;
    private LocalDate date;
    private String vehicleModel;
    private String vehicleNumber;
    private Double distance;
    private Integer duration;
    private String startLocation;
    private String endLocation;
    private String vehicleType;
}
