package lk.driveorbit.DriveOrbit_core.admin.service;

import com.google.firebase.auth.FirebaseToken;
import lk.driveorbit.DriveOrbit_core.admin.dto.AdminProfileResponse;
import lk.driveorbit.DriveOrbit_core.admin.dto.AdminSignupRequest;
import lk.driveorbit.DriveOrbit_core.admin.model.Admin;

/**
 * Service interface for admin authentication operations
 */
public interface AdminAuthService {
    
    /**
     * Create a new admin user in Firebase
     * @param request Admin signup request containing user details
     * @return Created admin with UID and role information
     * @throws Exception if creation fails
     */
    Admin createAdmin(AdminSignupRequest request) throws Exception;
    
    /**
     * Verify admin login token and check privileges
     * @param idToken Firebase ID token from client
     * @return Admin profile if token is valid and user has admin privileges
     * @throws Exception if verification fails or user lacks admin privileges
     */
    AdminProfileResponse verifyAdminLogin(String idToken) throws Exception;
    
    /**
     * Get admin profile from Firebase token
     * @param token Decoded Firebase token
     * @return Admin profile response
     * @throws Exception if token is invalid or user is not admin
     */
    AdminProfileResponse getAdminProfile(FirebaseToken token) throws Exception;
    
    /**
     * Update admin role (super-admin only operation)
     * @param uid User ID to update
     * @param newRole New role to assign
     * @param requestingUserToken Token of the user making the request
     * @throws Exception if update fails or requesting user lacks permissions
     */
    void updateAdminRole(String uid, String newRole, FirebaseToken requestingUserToken) throws Exception;
    
    /**
     * Verify if Firebase token belongs to an admin user
     * @param token Firebase token to verify
     * @return true if user has admin privileges
     */
    boolean isAdminUser(FirebaseToken token);
    
    /**
     * Verify if Firebase token belongs to a super admin user
     * @param token Firebase token to verify
     * @return true if user has super admin privileges
     */
    boolean isSuperAdmin(FirebaseToken token);
    
    /**
     * Verify Firebase ID token
     * @param idToken Firebase ID token string
     * @return Decoded Firebase token
     * @throws Exception if token is invalid
     */
    FirebaseToken verifyToken(String idToken) throws Exception;
}
