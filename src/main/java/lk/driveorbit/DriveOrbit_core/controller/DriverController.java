package lk.driveorbit.DriveOrbit_core.controller;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import lk.driveorbit.DriveOrbit_core.model.Driver;
import lk.driveorbit.DriveOrbit_core.model.FirestoreDriver;
import lk.driveorbit.DriveOrbit_core.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private static final Logger logger = Logger.getLogger(DriverController.class.getName());

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private Firestore firestore;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerDriver(@RequestBody Driver driver) {
        try {
            // Check if a driver with the given email already exists
            if (driverRepository.findByEmail(driver.getEmail()) != null) {
                return ResponseEntity.status(400).body("Email already exists");
            }
            
            // Check if a driver with the given NIC number already exists
            if (driverRepository.findByNicNumber(driver.getNicNumber()) != null) {
                return ResponseEntity.status(400).body("NIC number already exists");
            }

            // Store the original password for Firebase
            String originalPassword = driver.getPassword();

            // Encode the password before saving to the database
            driver.setPassword(passwordEncoder.encode(driver.getPassword()));

            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(driver.getEmail())
                    .setPassword(originalPassword); // Use the original password for Firebase

            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

            driverRepository.save(driver);
            
            // Convert Driver to FirestoreDriver before saving to Firestore
            FirestoreDriver firestoreDriver = FirestoreDriver.fromDriver(driver);
            firestore.collection("drivers").document(userRecord.getUid()).set(firestoreDriver).get();

            return ResponseEntity.ok("Driver registered successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Registration failed", e);
            return ResponseEntity.status(500).body("Registration failed: " + e.getMessage());
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<Driver> getDriver(@PathVariable String email) {
        try {
            Driver driver = driverRepository.findByEmail(email);
            if (driver == null) {
                // Get from Firestore and convert back to Driver
                FirestoreDriver firestoreDriver = firestore.collection("drivers").document(email).get().get()
                        .toObject(FirestoreDriver.class);
                if (firestoreDriver != null) {
                    driver = firestoreDriver.toDriver();
                }
            }
            return ResponseEntity.ok(driver);
        } catch (InterruptedException | ExecutionException e) {
            logger.log(Level.SEVERE, "Error retrieving driver", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{email}")
    public ResponseEntity<String> updateDriver(@PathVariable String email, @RequestBody Driver driver) {
        try {
            Driver existingDriver = driverRepository.findByEmail(email);
            if (existingDriver != null) {
                driver.setId(existingDriver.getId());
                driverRepository.save(driver);
            }
            
            // Convert Driver to FirestoreDriver before saving to Firestore
            FirestoreDriver firestoreDriver = FirestoreDriver.fromDriver(driver);
            firestore.collection("drivers").document(email).set(firestoreDriver).get();
            
            return ResponseEntity.ok("Driver updated successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Update failed", e);
            return ResponseEntity.status(500).body("Update failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteDriver(@PathVariable String email) {
        try {
            driverRepository.deleteByEmail(email);
            firestore.collection("drivers").document(email).delete().get();
            return ResponseEntity.ok("Driver deleted successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Deletion failed", e);
            return ResponseEntity.status(500).body("Deletion failed: " + e.getMessage());
        }
    }
}
