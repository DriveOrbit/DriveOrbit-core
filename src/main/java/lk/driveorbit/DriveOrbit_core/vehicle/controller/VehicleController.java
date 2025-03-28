package lk.driveorbit.DriveOrbit_core.vehicle.controller;

import lk.driveorbit.DriveOrbit_core.vehicle.entity.Vehicle;
import lk.driveorbit.DriveOrbit_core.vehicle.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<Vehicle> saveVehicle(@RequestBody Vehicle vehicle) {
        return new ResponseEntity<>(vehicleService.saveVehicle(vehicle), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Integer id) {
        Optional<Vehicle> vehicle = vehicleService.getVehicleById(id);
        return vehicle.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Integer id, @RequestBody Vehicle vehicle) {
        if (!id.equals(vehicle.getVehicleId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(vehicleService.saveVehicle(vehicle));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Integer id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<Vehicle> updateVehicleStatus(@PathVariable Integer id, @RequestBody String status) {
        try {
            Vehicle updatedVehicle = vehicleService.updateVehicleStatus(id, status);
            return ResponseEntity.ok(updatedVehicle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/qrcode")
    public ResponseEntity<Map<String, String>> getVehicleQRCode(@PathVariable Integer id) {
        Optional<Vehicle> vehicleOpt = vehicleService.getVehicleById(id);
        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            if (vehicle.getQrCodeURL() != null) {
                Map<String, String> response = new HashMap<>();
                response.put("qrCode", vehicle.getQrCodeURL());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "QR code not found for this vehicle"));
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}