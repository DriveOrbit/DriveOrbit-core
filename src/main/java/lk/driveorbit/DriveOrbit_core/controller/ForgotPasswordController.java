package lk.driveorbit.DriveOrbit_core.controller;

import lk.driveorbit.DriveOrbit_core.model.Driver;
import lk.driveorbit.DriveOrbit_core.service.EmailService;
import lk.driveorbit.DriveOrbit_core.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/auth")
public class ForgotPasswordController {
    private final EmailService emailService;
    private final UserService userService;

    public ForgotPasswordController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String userID = request.get("userID");
        if (userID == null) {
            return ResponseEntity.badRequest().body("Missing userID");
        }

        Optional<Driver> driverOptional = userService.findByUserID(userID);
        if (driverOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Please enter a valid company ID");
        }

        Driver driver = driverOptional.get();
        String otp = generateOTP();
        // Assuming you have a method to save OTP in the userService
        userService.saveOTP(driver.getEmail(), otp);

        String emailMessage = "Your OTP Code is: " + otp;
        emailService.sendEmail(driver.getEmail(), "Password Reset OTP", emailMessage);

        return ResponseEntity.ok("OTP sent to email: " + driver.getEmail());
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}