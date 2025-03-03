package lk.driveorbit.DriveOrbit_core.services;


import lk.driveorbit.DriveOrbit_core.Entity.Vehicle;
import lk.driveorbit.DriveOrbit_core.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

    @Service
    public class VehicleService {

        @Autowired
        private VehicleRepository vehicleRepository;

        public Vehicle registerVehicle(Vehicle vehicle) {
            return vehicleRepository.save(vehicle);
        }

        public List<Vehicle> getAllVehicles() {
            return vehicleRepository.findAll();
        }

        public Optional<Vehicle> getVehicleById(Long id) {
            return vehicleRepository.findById(id);
        }

        public void deleteVehicle(Long id) {
            vehicleRepository.deleteById(id);
        }
    }


