package lk.driveorbit.DriveOrbit_core.admin.util;

import com.google.firebase.auth.FirebaseToken;

/**
 * Utility class for admin authentication operations
 */
public class AdminAuthUtils {

    /**
     * Extract token from Authorization header
     * @param authHeader Authorization header value
     * @return Extracted token
     * @throws IllegalArgumentException if header format is invalid
     */
    public static String extractTokenFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header format. Expected 'Bearer <token>'");
        }
        return authHeader.substring(7);
    }

    /**
     * Validate admin role string
     * @param role Role to validate
     * @return true if role is valid admin role
     */
    public static boolean isValidAdminRole(String role) {
        return "admin".equals(role) || "super-admin".equals(role);
    }

    /**
     * Check if Firebase token has admin privileges
     * @param token Firebase token to check
     * @return true if user has admin privileges
     */
    public static boolean hasAdminPrivileges(FirebaseToken token) {
        if (token == null) {
            return false;
        }
        
        Object isAdminClaim = token.getClaims().get("isAdmin");
        Object roleClaim = token.getClaims().get("role");
        
        return Boolean.TRUE.equals(isAdminClaim) && 
               roleClaim instanceof String && 
               isValidAdminRole((String) roleClaim);
    }

    /**
     * Check if Firebase token has super admin privileges
     * @param token Firebase token to check
     * @return true if user has super admin privileges
     */
    public static boolean hasSuperAdminPrivileges(FirebaseToken token) {
        if (token == null) {
            return false;
        }
        
        Object isSuperAdminClaim = token.getClaims().get("isSuperAdmin");
        return Boolean.TRUE.equals(isSuperAdminClaim) && hasAdminPrivileges(token);
    }

    /**
     * Get role from Firebase token
     * @param token Firebase token
     * @return Role string or null if not found
     */
    public static String getRoleFromToken(FirebaseToken token) {
        if (token == null) {
            return null;
        }
        
        Object roleClaim = token.getClaims().get("role");
        return roleClaim instanceof String ? (String) roleClaim : null;
    }
}
