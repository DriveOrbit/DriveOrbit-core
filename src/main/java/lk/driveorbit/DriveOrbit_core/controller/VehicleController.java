package lk.driveorbit.DriveOrbit_core.controller;

import lk.driveorbit.DriveOrbit_core.entity.Vehicle;
import lk.driveorbit.DriveOrbit_core.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    public Vehicle saveVehicle(@RequestBody Vehicle vehicle) {
        return vehicleService.saveVehicle(vehicle);
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