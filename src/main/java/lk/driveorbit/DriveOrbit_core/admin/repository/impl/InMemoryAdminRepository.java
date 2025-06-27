package lk.driveorbit.DriveOrbit_core.admin.repository.impl;

import lk.driveorbit.DriveOrbit_core.admin.model.Admin;
import lk.driveorbit.DriveOrbit_core.admin.repository.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of AdminRepository
 * This is a temporary implementation for development/testing.
 * In production, this should be replaced with a proper database implementation.
 */
@Repository
public class InMemoryAdminRepository implements AdminRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(InMemoryAdminRepository.class);
    
    // In-memory storage maps
    private final Map<String, Admin> adminsByUid = new ConcurrentHashMap<>();
    private final Map<String, Admin> adminsByEmail = new ConcurrentHashMap<>();

    @Override
    public Admin save(Admin admin) {
        logger.info("Saving admin with UID: {} and email: {}", admin.getUid(), admin.getEmail());
        
        adminsByUid.put(admin.getUid(), admin);
        adminsByEmail.put(admin.getEmail(), admin);
        
        logger.debug("Admin saved successfully. Total admins: {}", adminsByUid.size());
        return admin;
    }

    @Override
    public Optional<Admin> findByUid(String uid) {
        logger.debug("Finding admin by UID: {}", uid);
        Admin admin = adminsByUid.get(uid);
        return Optional.ofNullable(admin);
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        logger.debug("Finding admin by email: {}", email);
        Admin admin = adminsByEmail.get(email);
        return Optional.ofNullable(admin);
    }

    @Override
    public List<Admin> findByRole(String role) {
        logger.debug("Finding admins by role: {}", role);
        return adminsByUid.values().stream()
                .filter(admin -> role.equals(admin.getRole()))
                .toList();
    }

    @Override
    public List<Admin> findAll() {
        logger.debug("Finding all admins. Total count: {}", adminsByUid.size());
        return new ArrayList<>(adminsByUid.values());
    }

    @Override
    public void deleteByUid(String uid) {
        logger.info("Deleting admin with UID: {}", uid);
        
        Admin admin = adminsByUid.remove(uid);
        if (admin != null) {
            adminsByEmail.remove(admin.getEmail());
            logger.info("Admin deleted successfully. Remaining admins: {}", adminsByUid.size());
        } else {
            logger.warn("Admin with UID {} not found for deletion", uid);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        boolean exists = adminsByEmail.containsKey(email);
        logger.debug("Checking if admin exists by email: {} - Result: {}", email, exists);
        return exists;
    }

    @Override
    public boolean existsByUid(String uid) {
        boolean exists = adminsByUid.containsKey(uid);
        logger.debug("Checking if admin exists by UID: {} - Result: {}", uid, exists);
        return exists;
    }
}
