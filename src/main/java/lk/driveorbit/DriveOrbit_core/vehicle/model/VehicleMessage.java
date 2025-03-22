package lk.driveorbit.DriveOrbit_core.vehicle.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lk.driveorbit.DriveOrbit_core.vehicle.entity.Vehicle;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleMessage {
    private String type; // "ADD", "UPDATE", "DELETE", "GET_ALL"
    private Vehicle vehicle;
}