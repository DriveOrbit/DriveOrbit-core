package lk.driveorbit.DriveOrbit_core.controller;

import com.google.zxing.WriterException;
import lk.driveorbit.DriveOrbit_core.entity.Vehicle;
import lk.driveorbit.DriveOrbit_core.model.VehicleMessage;
import lk.driveorbit.DriveOrbit_core.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class VehicleWebSocketController {

    @Autowired
    private VehicleService vehicleService;

    @MessageMapping("/vehicles.get.all")
    @SendTo("/topic/vehicles")
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @MessageMapping("/vehicles.get")
    @SendTo("/topic/vehicles.single")
    public Vehicle getVehicleById(Integer id) {
        Optional<Vehicle> vehicle = vehicleService.getVehicleById(id);
        return vehicle.orElse(null);
    }

    @MessageMapping("/vehicles.add")
    @SendTo("/topic/vehicles")
    public Vehicle addVehicle(Vehicle vehicle) throws IOException, WriterException {
        return vehicleService.saveVehicle(vehicle);
    }
    
    @MessageMapping("/vehicles.update")
    @SendTo("/topic/vehicles")
    public Vehicle updateVehicle(Vehicle vehicle) throws IOException, WriterException {
        return vehicleService.saveVehicle(vehicle);
    }

    @MessageMapping("/vehicles.status.update")
    @SendTo("/topic/vehicles")
    public Vehicle updateVehicleStatus(VehicleMessage vehicleMessage) {
        return vehicleService.updateVehicleStatus(vehicleMessage.getVehicle().getVehicleId(), vehicleMessage.getVehicle().getVehicleStatus());
    }
}