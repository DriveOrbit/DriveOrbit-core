package lk.driveorbit.DriveOrbit_core.controller;

import lk.driveorbit.DriveOrbit_core.entity.Vehicle;
import lk.driveorbit.DriveOrbit_core.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

//    @PostMapping
//    public Vehicle registerVehicle(@RequestBody Vehicle vehicle) throws Exception {
//        return vehicleService.registerVehicle(vehicle);
//    }

    @PostMapping
    public Vehicle saveVehicle(@RequestBody Vehicle vehicle) throws Exception {
        // Save the vehicle and generate QR code
        Vehicle savedVehicle = vehicleService.saveVehicle(vehicle);

        // Send WebSocket message with vehicle details and QR code
        messagingTemplate.convertAndSend("/topic/vehicles", savedVehicle);

        return savedVehicle;
    }

    @GetMapping("/{id}")
    public Optional<Vehicle> getVehicleById(@PathVariable Integer id) {
        return vehicleService.getVehicleById(id);
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }
}