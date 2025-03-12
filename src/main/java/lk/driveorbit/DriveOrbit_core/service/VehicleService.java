package lk.driveorbit.DriveOrbit_core.service;

import lk.driveorbit.DriveOrbit_core.entity.Vehicle;
import lk.driveorbit.DriveOrbit_core.model.VehicleMessage;
import lk.driveorbit.DriveOrbit_core.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Vehicle saveVehicle(Vehicle vehicle) {
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        // Broadcast to all subscribers
        VehicleMessage message = new VehicleMessage("UPDATE", savedVehicle);
        messagingTemplate.convertAndSend("/topic/vehicles", message);
        return savedVehicle;
    }

    public Optional<Vehicle> getVehicleById(Integer id) {
        return vehicleRepository.findById(id);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
    
    public void deleteVehicle(Integer id) {
        vehicleRepository.deleteById(id);
        // Broadcast delete notification
        VehicleMessage message = new VehicleMessage("DELETE", new Vehicle());
        message.getVehicle().setVehicleId(id);
        messagingTemplate.convertAndSend("/topic/vehicles", message);
    }
}