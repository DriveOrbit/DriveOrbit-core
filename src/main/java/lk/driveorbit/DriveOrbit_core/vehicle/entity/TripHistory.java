package lk.driveorbit.DriveOrbit_core.vehicle.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
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
    
    // Explicit getter and setter for historyId
    public String getHistoryId() {
        return historyId;
    }
    
    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }
}
