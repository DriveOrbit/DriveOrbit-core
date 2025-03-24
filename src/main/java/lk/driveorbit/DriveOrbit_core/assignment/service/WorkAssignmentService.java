package lk.driveorbit.DriveOrbit_core.assignment.service;

import com.google.cloud.firestore.Firestore;
import lk.driveorbit.DriveOrbit_core.assignment.model.FirestoreWorkAssignment;
import lk.driveorbit.DriveOrbit_core.assignment.model.WorkAssignment;
import lk.driveorbit.DriveOrbit_core.assignment.repository.WorkAssignmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class WorkAssignmentService {
    
    private static final Logger logger = LoggerFactory.getLogger(WorkAssignmentService.class);
    private static final String COLLECTION_NAME = "work_assignments";
    
    @Autowired
    private WorkAssignmentRepository workAssignmentRepository;
    
    @Autowired(required = false)
    private Firestore firestore;
    
    /**
     * Create a new work assignment
     */
    public WorkAssignment createAssignment(WorkAssignment assignment) {
        logger.info("Creating new work assignment with ID: {}", assignment.getAssignId());
        
        // Save to PostgreSQL
        WorkAssignment savedAssignment = workAssignmentRepository.save(assignment);
        
        // Save to Firestore if available
        if (firestore != null) {
            try {
                FirestoreWorkAssignment firestoreAssignment = FirestoreWorkAssignment.fromWorkAssignment(savedAssignment);
                firestore.collection(COLLECTION_NAME).document(assignment.getAssignId()).set(firestoreAssignment).get();
                logger.info("Assignment {} saved to Firestore", assignment.getAssignId());
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error saving assignment to Firestore: {}", e.getMessage(), e);
            }
        }
        
        return savedAssignment;
    }
    
    /**
     * Update an existing work assignment
     */
    public WorkAssignment updateAssignment(String assignId, WorkAssignment assignmentDetails) {
        logger.info("Updating work assignment with ID: {}", assignId);
        
        Optional<WorkAssignment> assignmentOpt = workAssignmentRepository.findById(assignId);
        if (!assignmentOpt.isPresent()) {
            logger.warn("Assignment with ID {} not found", assignId);
            return null;
        }
        
        // Update the assignment
        WorkAssignment existingAssignment = assignmentOpt.get();
        
        // Set new values from the details object, preserving the ID
        existingAssignment.setDate(assignmentDetails.getDate());
        existingAssignment.setArrivedTime(assignmentDetails.getArrivedTime());
        existingAssignment.setDistance(assignmentDetails.getDistance());
        existingAssignment.setDuration(assignmentDetails.getDuration());
        existingAssignment.setStartLocation(assignmentDetails.getStartLocation());
        existingAssignment.setEndLocation(assignmentDetails.getEndLocation());
        existingAssignment.setStatus(assignmentDetails.getStatus());
        existingAssignment.setUrgency(assignmentDetails.getUrgency());
        existingAssignment.setCustomerName(assignmentDetails.getCustomerName());
        existingAssignment.setCustomerContact(assignmentDetails.getCustomerContact());
        existingAssignment.setVehicleType(assignmentDetails.getVehicleType());
        existingAssignment.setEstimatedFare(assignmentDetails.getEstimatedFare());
        existingAssignment.setNotes(assignmentDetails.getNotes());
        existingAssignment.setDriverId(assignmentDetails.getDriverId());
        existingAssignment.setVehicleId(assignmentDetails.getVehicleId());
        existingAssignment.setIsComplete(assignmentDetails.getIsComplete());
        
        // Save to PostgreSQL
        WorkAssignment updatedAssignment = workAssignmentRepository.save(existingAssignment);
        
        // Update in Firestore if available
        if (firestore != null) {
            try {
                FirestoreWorkAssignment firestoreAssignment = FirestoreWorkAssignment.fromWorkAssignment(updatedAssignment);
                firestore.collection(COLLECTION_NAME).document(assignId).set(firestoreAssignment).get();
                logger.info("Assignment {} updated in Firestore", assignId);
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error updating assignment in Firestore: {}", e.getMessage(), e);
            }
        }
        
        return updatedAssignment;
    }
    
    /**
     * Update assignment completion status
     */
    public WorkAssignment updateAssignmentCompletionStatus(String assignId, Boolean isComplete) {
        logger.info("Updating completion status of assignment {} to {}", assignId, isComplete);
        
        Optional<WorkAssignment> assignmentOpt = workAssignmentRepository.findById(assignId);
        if (!assignmentOpt.isPresent()) {
            logger.warn("Assignment with ID {} not found", assignId);
            return null;
        }
        
        WorkAssignment assignment = assignmentOpt.get();
        assignment.setIsComplete(isComplete);
        
        // If marked as complete, also update the status to "completed"
        if (isComplete) {
            assignment.setStatus("completed");
        }
        
        // Save to PostgreSQL
        WorkAssignment updatedAssignment = workAssignmentRepository.save(assignment);
        
        // Update in Firestore if available
        if (firestore != null) {
            try {
                FirestoreWorkAssignment firestoreAssignment = FirestoreWorkAssignment.fromWorkAssignment(updatedAssignment);
                firestore.collection(COLLECTION_NAME).document(assignId).set(firestoreAssignment).get();
                logger.info("Assignment {} completion status updated in Firestore", assignId);
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error updating assignment completion status in Firestore: {}", e.getMessage(), e);
            }
        }
        
        return updatedAssignment;
    }
    
    /**
     * Get a work assignment by ID
     */
    public WorkAssignment getAssignmentById(String assignId) {
        logger.info("Fetching work assignment with ID: {}", assignId);
        
        Optional<WorkAssignment> assignment = workAssignmentRepository.findById(assignId);
        return assignment.orElse(null);
    }
    
    /**
     * Delete a work assignment
     */
    public boolean deleteAssignment(String assignId) {
        logger.info("Deleting work assignment with ID: {}", assignId);
        
        if (!workAssignmentRepository.existsById(assignId)) {
            logger.warn("Assignment with ID {} not found", assignId);
            return false;
        }
        
        // Delete from PostgreSQL
        workAssignmentRepository.deleteById(assignId);
        
        // Delete from Firestore if available
        if (firestore != null) {
            try {
                firestore.collection(COLLECTION_NAME).document(assignId).delete().get();
                logger.info("Assignment {} deleted from Firestore", assignId);
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error deleting assignment from Firestore: {}", e.getMessage(), e);
            }
        }
        
        return true;
    }
    
    /**
     * Get all work assignments
     */
    public List<WorkAssignment> getAllAssignments() {
        logger.info("Fetching all work assignments");
        return workAssignmentRepository.findAll();
    }
    
    /**
     * Get assignments by driver ID
     */
    public List<WorkAssignment> getAssignmentsByDriver(String driverId) {
        logger.info("Fetching assignments for driver: {}", driverId);
        return workAssignmentRepository.findByDriverId(driverId);
    }
    
    /**
     * Get assignments by vehicle ID
     */
    public List<WorkAssignment> getAssignmentsByVehicle(String vehicleId) {
        logger.info("Fetching assignments for vehicle: {}", vehicleId);
        return workAssignmentRepository.findByVehicleId(vehicleId);
    }
    
    /**
     * Get assignments by status
     */
    public List<WorkAssignment> getAssignmentsByStatus(String status) {
        logger.info("Fetching assignments with status: {}", status);
        return workAssignmentRepository.findByStatus(status);
    }
    
    /**
     * Get assignments by date
     */
    public List<WorkAssignment> getAssignmentsByDate(LocalDate date) {
        logger.info("Fetching assignments for date: {}", date);
        return workAssignmentRepository.findByDate(date);
    }
    
    /**
     * Get active assignments (pending or in-progress) by driver
     */
    public List<WorkAssignment> getActiveAssignmentsByDriver(String driverId) {
        logger.info("Fetching active assignments for driver: {}", driverId);
        return workAssignmentRepository.findActiveAssignmentsByDriver(driverId);
    }
    
    /**
     * Update assignment status
     */
    public WorkAssignment updateAssignmentStatus(String assignId, String status) {
        logger.info("Updating status of assignment {} to {}", assignId, status);
        
        Optional<WorkAssignment> assignmentOpt = workAssignmentRepository.findById(assignId);
        if (!assignmentOpt.isPresent()) {
            logger.warn("Assignment with ID {} not found", assignId);
            return null;
        }
        
        WorkAssignment assignment = assignmentOpt.get();
        assignment.setStatus(status);
        
        // Save to PostgreSQL
        WorkAssignment updatedAssignment = workAssignmentRepository.save(assignment);
        
        // Update in Firestore if available
        if (firestore != null) {
            try {
                FirestoreWorkAssignment firestoreAssignment = FirestoreWorkAssignment.fromWorkAssignment(updatedAssignment);
                firestore.collection(COLLECTION_NAME).document(assignId).set(firestoreAssignment).get();
                logger.info("Assignment {} status updated in Firestore", assignId);
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error updating assignment status in Firestore: {}", e.getMessage(), e);
            }
        }
        
        return updatedAssignment;
    }
    
    /**
     * Assign a driver to an assignment
     */
    public WorkAssignment assignDriver(String assignId, String driverId) {
        logger.info("Assigning driver {} to assignment {}", driverId, assignId);
        
        Optional<WorkAssignment> assignmentOpt = workAssignmentRepository.findById(assignId);
        if (!assignmentOpt.isPresent()) {
            logger.warn("Assignment with ID {} not found", assignId);
            return null;
        }
        
        WorkAssignment assignment = assignmentOpt.get();
        assignment.setDriverId(driverId);
        
        // Save to PostgreSQL
        WorkAssignment updatedAssignment = workAssignmentRepository.save(assignment);
        
        // Update in Firestore if available
        if (firestore != null) {
            try {
                FirestoreWorkAssignment firestoreAssignment = FirestoreWorkAssignment.fromWorkAssignment(updatedAssignment);
                firestore.collection(COLLECTION_NAME).document(assignId).set(firestoreAssignment).get();
                logger.info("Assignment {} driver updated in Firestore", assignId);
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error updating assignment driver in Firestore: {}", e.getMessage(), e);
            }
        }
        
        return updatedAssignment;
    }
}
