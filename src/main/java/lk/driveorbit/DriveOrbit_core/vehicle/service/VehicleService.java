package lk.driveorbit.DriveOrbit_core.vehicle.service;

import com.google.cloud.firestore.Firestore;
import lk.driveorbit.DriveOrbit_core.vehicle.entity.Vehicle;
import lk.driveorbit.DriveOrbit_core.vehicle.repository.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class VehicleService {

    private static final Logger logger = LoggerFactory.getLogger(VehicleService.class);
    private static final String COLLECTION_NAME = "vehicles";

    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired(required = false)
    private Firestore firestore;
    
    @Autowired
    private QRCodeService qrCodeService;
    
    @PostConstruct
    public void init() {
        if (firestore != null) {
            logger.info("Firestore bean successfully injected in VehicleService");
        } else {
            logger.warn("Firestore bean is null in VehicleService - Firebase operations will be skipped");
        }
    }

    public Vehicle saveVehicle(Vehicle vehicle) {
        // First save to get the vehicle ID
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        
        // Generate QR code with vehicle info (base URL + vehicle ID)
        String qrCodeData = "vehicle:" + savedVehicle.getVehicleId();
        
        // For better user experience, include more data to display when scanned
        qrCodeData += "|number:" + savedVehicle.getVehicleNumber() + 
                      "|model:" + savedVehicle.getVehicleModel() + 
                      "|type:" + savedVehicle.getVehicleType();
                      
        // Generate QR code as Base64 image
        String qrCodeBase64 = qrCodeService.generateQRCodeBase64(
                qrCodeData, 250, 250);
        
        // Set the QR code URL in the vehicle
        savedVehicle.setQrCodeURL(qrCodeBase64);
        
        // Save the updated vehicle with QR code
        savedVehicle = vehicleRepository.save(savedVehicle);
        logger.info("Vehicle saved to PostgreSQL with ID: {}", savedVehicle.getVehicleId());
        
        // Save to Firebase if configured
        if (firestore != null) {
            try {
                logger.info("Attempting to save vehicle to Firebase with ID: {}", savedVehicle.getVehicleId());
                firestore.collection(COLLECTION_NAME)
                        .document(savedVehicle.getVehicleId().toString())
                        .set(savedVehicle)
                        .get();
                logger.info("Vehicle successfully saved to Firebase with ID: {}", savedVehicle.getVehicleId());
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Failed to save vehicle to Firebase: {}", e.getMessage(), e);
            }
        } else {
            logger.warn("Skipping Firebase save - Firestore bean is null");
        }
        
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
        logger.info("Vehicle deleted from PostgreSQL with ID: {}", id);
        
        // Delete from Firebase if configured
        if (firestore != null) {
            try {
                logger.info("Attempting to delete vehicle from Firebase with ID: {}", id);
                firestore.collection(COLLECTION_NAME)
                        .document(id.toString())
                        .delete()
                        .get();
                logger.info("Vehicle successfully deleted from Firebase with ID: {}", id);
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Failed to delete vehicle from Firebase: {}", e.getMessage(), e);
            }
        } else {
            logger.warn("Skipping Firebase delete - Firestore bean is null");
        }
    }

    public Vehicle updateVehicleStatus(Integer vehicleId, String status) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            vehicle.setVehicleStatus(status);
            
            // Update in PostgreSQL
            Vehicle updatedVehicle = vehicleRepository.save(vehicle);
            
            // Update in Firebase if configured
            if (firestore != null) {
                try {
                    firestore.collection(COLLECTION_NAME)
                            .document(vehicleId.toString())
                            .update("vehicleStatus", status)
                            .get();
                    logger.info("Vehicle status updated in Firebase with ID: {}", vehicleId);
                } catch (InterruptedException | ExecutionException e) {
                    logger.error("Failed to update vehicle status in Firebase: {}", e.getMessage());
                }
            }
            
            return updatedVehicle;
        } else {
            throw new RuntimeException("Vehicle not found with id: " + vehicleId);
        }
    }
}