package lk.driveorbit.DriveOrbit_core.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import lk.driveorbit.DriveOrbit_core.model.Driver;
import lk.driveorbit.DriveOrbit_core.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    @Autowired
    private DriverRepository driverRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerDriver(@RequestBody Driver driver) {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(driver.getEmail())
                    .setPassword("password"); // Replace with actual password from request

            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

            driverRepository.save(driver);

            return ResponseEntity.ok("Driver registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Registration failed: " + e.getMessage());
        }
    }
}
