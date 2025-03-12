package lk.driveorbit.DriveOrbit_core.service;

import lk.driveorbit.DriveOrbit_core.entity.Vehicle;
import lk.driveorbit.DriveOrbit_core.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Optional<Vehicle> getVehicleById(Integer id) {
        return vehicleRepository.findById(id);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
}