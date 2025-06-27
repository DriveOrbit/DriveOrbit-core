package lk.driveorbit.DriveOrbit_core.admin.repository;

import lk.driveorbit.DriveOrbit_core.admin.model.Admin;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Admin entity persistence operations
 * Currently using Firebase for user management, but this interface
 * provides abstraction for future database integration
 */
public interface AdminRepository {
    
    /**
     * Save admin to persistent storage
     * @param admin Admin entity to save
     * @return Saved admin entity
     */
    Admin save(Admin admin);
    
    /**
     * Find admin by UID
     * @param uid Firebase UID
     * @return Optional containing admin if found
     */
    Optional<Admin> findByUid(String uid);
    
    /**
     * Find admin by email
     * @param email Admin email
     * @return Optional containing admin if found
     */
    Optional<Admin> findByEmail(String email);
    
    /**
     * Find all admins with specific role
     * @param role Admin role
     * @return List of admins with the specified role
     */
    List<Admin> findByRole(String role);
    
    /**
     * Find all admins
     * @return List of all admins
     */
    List<Admin> findAll();
    
    /**
     * Delete admin by UID
     * @param uid Firebase UID
     */
    void deleteByUid(String uid);
    
    /**
     * Check if admin exists by email
     * @param email Admin email
     * @return true if admin exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if admin exists by UID
     * @param uid Firebase UID
     * @return true if admin exists
     */
    boolean existsByUid(String uid);
}
