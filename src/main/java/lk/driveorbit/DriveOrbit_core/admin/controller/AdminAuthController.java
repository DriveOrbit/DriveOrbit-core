package lk.driveorbit.DriveOrbit_core.admin.controller;

import com.google.firebase.auth.FirebaseToken;
import lk.driveorbit.DriveOrbit_core.admin.dto.*;
import lk.driveorbit.DriveOrbit_core.admin.model.Admin;
import lk.driveorbit.DriveOrbit_core.admin.service.AdminAuthService;
import lk.driveorbit.DriveOrbit_core.admin.util.AdminAuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/auth")
@CrossOrigin(origins = "*")
public class AdminAuthController {

    private static final Logger logger = LoggerFactory.getLogger(AdminAuthController.class);

    private final AdminAuthService adminAuthService;

    @Autowired
    public AdminAuthController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    /**
     * Create admin user in Firebase
     * Only allows creation of admin and super-admin users
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createAdmin(@Valid @RequestBody AdminSignupRequest request) {
        try {
            logger.info("Received admin signup request for email: {}", request.getEmail());

            Admin admin = adminAuthService.createAdmin(request);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("uid", admin.getUid());
            responseData.put("email", admin.getEmail());
            responseData.put("role", admin.getRole());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Admin user created successfully", responseData));

        } catch (Exception e) {
            logger.error("Error during admin signup: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Verify admin login token
     * Validates Firebase token and checks admin privileges
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AdminProfileResponse>> verifyAdminLogin(@Valid @RequestBody AdminLoginRequest request) {
        try {
            logger.info("Received admin login verification request");

            AdminProfileResponse profileResponse = adminAuthService.verifyAdminLogin(request.getIdToken());
            
            return ResponseEntity.ok(ApiResponse.success("Admin login successful", profileResponse));

        } catch (Exception e) {
            logger.error("Error during admin login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get admin user profile
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<AdminProfileResponse>> getAdminProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = AdminAuthUtils.extractTokenFromHeader(authHeader);
            FirebaseToken decodedToken = adminAuthService.verifyToken(token);
            
            AdminProfileResponse profile = adminAuthService.getAdminProfile(decodedToken);
            
            return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", profile));

        } catch (Exception e) {
            logger.error("Error getting admin profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Update admin role (super-admin only)
     */
    @PatchMapping("/role/{uid}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateAdminRole(
            @PathVariable String uid,
            @Valid @RequestBody AdminRoleUpdateRequest request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = AdminAuthUtils.extractTokenFromHeader(authHeader);
            FirebaseToken decodedToken = adminAuthService.verifyToken(token);
            
            adminAuthService.updateAdminRole(uid, request.getRole(), decodedToken);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("uid", uid);
            responseData.put("newRole", request.getRole());

            return ResponseEntity.ok(ApiResponse.success("Admin role updated successfully", responseData));

        } catch (Exception e) {
            logger.error("Error updating admin role: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
