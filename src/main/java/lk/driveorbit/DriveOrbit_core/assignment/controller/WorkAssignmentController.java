package lk.driveorbit.DriveOrbit_core.assignment.controller;

import lk.driveorbit.DriveOrbit_core.assignment.model.WorkAssignment;
import lk.driveorbit.DriveOrbit_core.assignment.service.WorkAssignmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/work-assignments")
public class WorkAssignmentController {

    private static final Logger logger = LoggerFactory.getLogger(WorkAssignmentController.class);

    @Autowired
    private WorkAssignmentService workAssignmentService;

    /**
     * Create a new work assignment
     */
    @PostMapping
    public ResponseEntity<?> createAssignment(@RequestBody WorkAssignment assignment) {
        try {
            logger.info("Received request to create work assignment with ID: {}", assignment.getAssignId());
            
            // Check if required fields are present
            if (assignment.getAssignId() == null || assignment.getAssignId().isEmpty()) {
                return ResponseEntity.badRequest().body("Assignment ID is required");
            }
            
            if (assignment.getStatus() == null || assignment.getStatus().isEmpty()) {
                // Set default status if not provided
                assignment.setStatus("pending");
            }
            
            if (assignment.getIsComplete() == null) {
                // Set default isComplete value if not provided
                assignment.setIsComplete(false);
            }
            
            WorkAssignment createdAssignment = workAssignmentService.createAssignment(assignment);
            return new ResponseEntity<>(createdAssignment, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating work assignment: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating work assignment: " + e.getMessage());
        }
    }

    /**
     * Get all work assignments
     */
    @GetMapping
    public ResponseEntity<List<WorkAssignment>> getAllAssignments() {
        logger.info("Received request to get all work assignments");
        return ResponseEntity.ok(workAssignmentService.getAllAssignments());
    }

    /**
     * Get work assignment by ID
     */
    @GetMapping("/{assignId}")
    public ResponseEntity<?> getAssignmentById(@PathVariable String assignId) {
        logger.info("Received request to get work assignment with ID: {}", assignId);
        
        WorkAssignment assignment = workAssignmentService.getAssignmentById(assignId);
        if (assignment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Work assignment not found with ID: " + assignId);
        }
        
        return ResponseEntity.ok(assignment);
    }

    /**
     * Update work assignment
     */
    @PutMapping("/{assignId}")
    public ResponseEntity<?> updateAssignment(@PathVariable String assignId, @RequestBody WorkAssignment assignment) {
        try {
            logger.info("Received request to update work assignment with ID: {}", assignId);
            
            WorkAssignment updatedAssignment = workAssignmentService.updateAssignment(assignId, assignment);
            if (updatedAssignment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Work assignment not found with ID: " + assignId);
            }
            
            return ResponseEntity.ok(updatedAssignment);
        } catch (Exception e) {
            logger.error("Error updating work assignment: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating work assignment: " + e.getMessage());
        }
    }

    /**
     * Delete work assignment
     */
    @DeleteMapping("/{assignId}")
    public ResponseEntity<?> deleteAssignment(@PathVariable String assignId) {
        try {
            logger.info("Received request to delete work assignment with ID: {}", assignId);
            
            boolean deleted = workAssignmentService.deleteAssignment(assignId);
            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Work assignment not found with ID: " + assignId);
            }
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Work assignment deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error deleting work assignment: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting work assignment: " + e.getMessage());
        }
    }

    /**
     * Get assignments by driver ID
     */
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<WorkAssignment>> getAssignmentsByDriver(@PathVariable String driverId) {
        logger.info("Received request to get assignments for driver with ID: {}", driverId);
        return ResponseEntity.ok(workAssignmentService.getAssignmentsByDriver(driverId));
    }

    /**
     * Get assignments by vehicle ID
     */
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<WorkAssignment>> getAssignmentsByVehicle(@PathVariable String vehicleId) {
        logger.info("Received request to get assignments for vehicle with ID: {}", vehicleId);
        return ResponseEntity.ok(workAssignmentService.getAssignmentsByVehicle(vehicleId));
    }

    /**
     * Get assignments by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<WorkAssignment>> getAssignmentsByStatus(@PathVariable String status) {
        logger.info("Received request to get assignments with status: {}", status);
        return ResponseEntity.ok(workAssignmentService.getAssignmentsByStatus(status));
    }

    /**
     * Get assignments by date
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<List<WorkAssignment>> getAssignmentsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        logger.info("Received request to get assignments for date: {}", date);
        return ResponseEntity.ok(workAssignmentService.getAssignmentsByDate(date));
    }

    /**
     * Get active assignments by driver
     */
    @GetMapping("/driver/{driverId}/active")
    public ResponseEntity<List<WorkAssignment>> getActiveAssignmentsByDriver(@PathVariable String driverId) {
        logger.info("Received request to get active assignments for driver with ID: {}", driverId);
        return ResponseEntity.ok(workAssignmentService.getActiveAssignmentsByDriver(driverId));
    }

    /**
     * Update assignment status
     */
    @PatchMapping("/{assignId}/status")
    public ResponseEntity<?> updateAssignmentStatus(
            @PathVariable String assignId,
            @RequestParam String status) {
        try {
            logger.info("Received request to update status of assignment {} to {}", assignId, status);
            
            WorkAssignment updatedAssignment = workAssignmentService.updateAssignmentStatus(assignId, status);
            if (updatedAssignment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Work assignment not found with ID: " + assignId);
            }
            
            return ResponseEntity.ok(updatedAssignment);
        } catch (Exception e) {
            logger.error("Error updating assignment status: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating assignment status: " + e.getMessage());
        }
    }

    /**
     * Assign driver to assignment
     */
    @PatchMapping("/{assignId}/driver")
    public ResponseEntity<?> assignDriver(
            @PathVariable String assignId,
            @RequestParam String driverId) {
        try {
            logger.info("Received request to assign driver {} to assignment {}", driverId, assignId);
            
            WorkAssignment updatedAssignment = workAssignmentService.assignDriver(assignId, driverId);
            if (updatedAssignment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Work assignment not found with ID: " + assignId);
            }
            
            return ResponseEntity.ok(updatedAssignment);
        } catch (Exception e) {
            logger.error("Error assigning driver to assignment: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error assigning driver to assignment: " + e.getMessage());
        }
    }

    /**
     * Update assignment completion status
     */
    @PatchMapping("/{assignId}/complete")
    public ResponseEntity<?> updateAssignmentCompletionStatus(
            @PathVariable String assignId,
            @RequestParam Boolean isComplete) {
        try {
            logger.info("Received request to update completion status of assignment {} to {}", assignId, isComplete);
            
            WorkAssignment updatedAssignment = workAssignmentService.updateAssignmentCompletionStatus(assignId, isComplete);
            if (updatedAssignment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Work assignment not found with ID: " + assignId);
            }
            
            return ResponseEntity.ok(updatedAssignment);
        } catch (Exception e) {
            logger.error("Error updating assignment completion status: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating assignment completion status: " + e.getMessage());
        }
    }
}
