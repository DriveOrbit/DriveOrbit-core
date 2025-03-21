package lk.driveorbit.DriveOrbit_core.driver.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/verify-token")
    public ResponseEntity<String> verifyToken(@RequestParam String idToken) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid(); // Firebase UID of the authenticated user
            return ResponseEntity.ok("User verified: " + uid);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        try {
            String resetLink = FirebaseAuth.getInstance().generatePasswordResetLink(email);
            return ResponseEntity.ok("Password reset link sent: " + resetLink);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(500).body("Failed to send password reset link: " + e.getMessage());
        }
    }
}
