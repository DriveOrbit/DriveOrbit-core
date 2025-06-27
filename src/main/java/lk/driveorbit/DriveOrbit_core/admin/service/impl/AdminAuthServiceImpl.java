package lk.driveorbit.DriveOrbit_core.admin.service.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import lk.driveorbit.DriveOrbit_core.admin.dto.AdminProfileResponse;
import lk.driveorbit.DriveOrbit_core.admin.dto.AdminSignupRequest;
import lk.driveorbit.DriveOrbit_core.admin.model.Admin;
import lk.driveorbit.DriveOrbit_core.admin.repository.AdminRepository;
import lk.driveorbit.DriveOrbit_core.admin.service.AdminAuthService;
import lk.driveorbit.DriveOrbit_core.admin.util.AdminAuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of AdminAuthService for Firebase-based admin authentication
 */
@Service
public class AdminAuthServiceImpl implements AdminAuthService {

    private static final Logger logger = LoggerFactory.getLogger(AdminAuthServiceImpl.class);

    private final AdminRepository adminRepository;

    @Autowired
    public AdminAuthServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Admin createAdmin(AdminSignupRequest request) throws Exception {
        logger.info("Creating admin user for email: {}", request.getEmail());

        // Validate admin role
        if (!AdminAuthUtils.isValidAdminRole(request.getRole())) {
            throw new IllegalArgumentException("Invalid role. Only 'admin' or 'super-admin' roles allowed.");
        }

        // Check if admin already exists in our repository
        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new Exception("Admin with this email already exists in the system");
        }

        try {
            // Create user in Firebase
            UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest()
                    .setEmail(request.getEmail())
                    .setPassword(request.getPassword())
                    .setDisplayName(request.getFullName())
                    .setEmailVerified(true); // Admin accounts are pre-verified

            UserRecord userRecord = FirebaseAuth.getInstance().createUser(createRequest);

            // Set custom claims for admin role
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", request.getRole());
            claims.put("isAdmin", true);
            if ("super-admin".equals(request.getRole())) {
                claims.put("isSuperAdmin", true);
            }

            FirebaseAuth.getInstance().setCustomUserClaims(userRecord.getUid(), claims);

            logger.info("Admin user created successfully with UID: {}", userRecord.getUid());

            // Create Admin model object and save to repository
            Admin admin = new Admin(
                    userRecord.getUid(),
                    userRecord.getEmail(),
                    request.getFullName(),
                    request.getRole()
            );

            // Save to repository
            admin = adminRepository.save(admin);
            logger.info("Admin saved to repository: {}", admin.getUid());

            return admin;

        } catch (FirebaseAuthException e) {
            logger.error("Firebase auth error during admin creation: {}", e.getMessage());
            
            String errorMessage;
            String errorCode = e.getErrorCode().name();
            if ("EMAIL_ALREADY_EXISTS".equals(errorCode)) {
                errorMessage = "Email already exists in Firebase";
            } else if ("INVALID_EMAIL".equals(errorCode)) {
                errorMessage = "Invalid email format";
            } else if ("WEAK_PASSWORD".equals(errorCode)) {
                errorMessage = "Password is too weak. Use at least 6 characters.";
            } else {
                errorMessage = "Authentication error: " + e.getMessage();
            }
            
            throw new Exception(errorMessage);
        }
    }

    @Override
    public AdminProfileResponse verifyAdminLogin(String idToken) throws Exception {
        logger.info("Verifying admin login token");

        try {
            // Verify Firebase token
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            
            // Check if user has admin privileges
            if (!isAdminUser(decodedToken)) {
                logger.warn("Non-admin user attempted to login: {}", decodedToken.getEmail());
                throw new Exception("Access denied. Admin privileges required.");
            }

            logger.info("Admin login successful for: {}", decodedToken.getEmail());
            return createAdminProfileResponse(decodedToken);

        } catch (FirebaseAuthException e) {
            logger.error("Firebase auth error during admin login: {}", e.getMessage());
            throw new Exception("Invalid or expired token");
        }
    }

    @Override
    public AdminProfileResponse getAdminProfile(FirebaseToken token) throws Exception {
        // Verify admin privileges
        if (!isAdminUser(token)) {
            throw new Exception("Admin access required");
        }

        return createAdminProfileResponse(token);
    }

    @Override
    public void updateAdminRole(String uid, String newRole, FirebaseToken requestingUserToken) throws Exception {
        // Only super-admin can update roles
        if (!isSuperAdmin(requestingUserToken)) {
            throw new Exception("Super admin access required");
        }

        if (!AdminAuthUtils.isValidAdminRole(newRole)) {
            throw new IllegalArgumentException("Invalid role specified");
        }

        try {
            // Update custom claims in Firebase
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", newRole);
            claims.put("isAdmin", true);
            claims.put("isSuperAdmin", "super-admin".equals(newRole));

            FirebaseAuth.getInstance().setCustomUserClaims(uid, claims);
            
            // Update in repository if exists
            adminRepository.findByUid(uid).ifPresent(admin -> {
                admin.setRole(newRole);
                adminRepository.save(admin);
                logger.info("Admin role updated in repository for UID: {}", uid);
            });
            
            logger.info("Admin role updated successfully for UID: {} to role: {}", uid, newRole);

        } catch (FirebaseAuthException e) {
            logger.error("Failed to update admin role: {}", e.getMessage());
            throw new Exception("Failed to update role: " + e.getMessage());
        }
    }

    @Override
    public boolean isAdminUser(FirebaseToken token) {
        return AdminAuthUtils.hasAdminPrivileges(token);
    }

    @Override
    public boolean isSuperAdmin(FirebaseToken token) {
        return AdminAuthUtils.hasSuperAdminPrivileges(token);
    }

    @Override
    public FirebaseToken verifyToken(String idToken) throws Exception {
        try {
            return FirebaseAuth.getInstance().verifyIdToken(idToken);
        } catch (FirebaseAuthException e) {
            logger.error("Token verification failed: {}", e.getMessage());
            throw new Exception("Invalid or expired token");
        }
    }

    // Helper methods
    private AdminProfileResponse createAdminProfileResponse(FirebaseToken token) {
        String role = AdminAuthUtils.getRoleFromToken(token);
        
        return new AdminProfileResponse(
                token.getUid(),
                token.getEmail(),
                token.getName(),
                role,
                true, // isAdmin is always true for admin users
                AdminAuthUtils.hasSuperAdminPrivileges(token)
        );
    }
}
