package lk.driveorbit.DriveOrbit_core.assignment.repository;

import lk.driveorbit.DriveOrbit_core.assignment.model.WorkAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkAssignmentRepository extends JpaRepository<WorkAssignment, String> {
    
    // Find assignments by driver ID
    List<WorkAssignment> findByDriverId(String driverId);
    
    // Find assignments by vehicle ID
    List<WorkAssignment> findByVehicleId(String vehicleId);
    
    // Find assignments by status
    List<WorkAssignment> findByStatus(String status);
    
    // Find assignments by date
    List<WorkAssignment> findByDate(LocalDate date);
    
    // Find assignments by date and driver ID
    List<WorkAssignment> findByDateAndDriverId(LocalDate date, String driverId);
    
    // Find assignments by urgency level
    List<WorkAssignment> findByUrgency(String urgency);
    
    // Find assignments by customer
    List<WorkAssignment> findByCustomerName(String customerName);
    
    // Find pending or in-progress assignments for a driver
    @Query("SELECT a FROM WorkAssignment a WHERE a.driverId = ?1 AND (a.status = 'pending' OR a.status = 'in-progress')")
    List<WorkAssignment> findActiveAssignmentsByDriver(String driverId);
    
    // Find assignments by date range
    @Query("SELECT a FROM WorkAssignment a WHERE a.date BETWEEN ?1 AND ?2")
    List<WorkAssignment> findByDateRange(LocalDate startDate, LocalDate endDate);
    
    // Find assignments by completion status
    List<WorkAssignment> findByIsComplete(Boolean isComplete);
    
    // Find completed assignments by driver ID
    List<WorkAssignment> findByDriverIdAndIsComplete(String driverId, Boolean isComplete);
}
