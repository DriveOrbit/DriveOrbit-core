package lk.driveorbit.DriveOrbit_core.assignment.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FirestoreWorkAssignment {
    private String assignId;
    private String date;
    private String arrivedTime;
    private Double distance;
    private Integer duration;
    private String startLocation;
    private String endLocation;
    private String status;
    private String urgency;
    private String customerName;
    private String customerContact;
    private String vehicleType;
    private Double estimatedFare;
    private String notes;
    private String driverId;
    private String vehicleId;
    private Boolean isComplete;

    // Default constructor required for Firestore
    public FirestoreWorkAssignment() {
    }

    // Convert from WorkAssignment to FirestoreWorkAssignment
    public static FirestoreWorkAssignment fromWorkAssignment(WorkAssignment assignment) {
        if (assignment == null) {
            return null;
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        
        FirestoreWorkAssignment firestoreAssignment = new FirestoreWorkAssignment();
        
        firestoreAssignment.setAssignId(assignment.getAssignId());
        
        // Convert LocalDate and LocalDateTime to String
        if (assignment.getDate() != null) {
            firestoreAssignment.setDate(assignment.getDate().format(dateFormatter));
        }
        if (assignment.getArrivedTime() != null) {
            firestoreAssignment.setArrivedTime(assignment.getArrivedTime().format(dateTimeFormatter));
        }
        
        firestoreAssignment.setDistance(assignment.getDistance());
        firestoreAssignment.setDuration(assignment.getDuration());
        firestoreAssignment.setStartLocation(assignment.getStartLocation());
        firestoreAssignment.setEndLocation(assignment.getEndLocation());
        firestoreAssignment.setStatus(assignment.getStatus());
        firestoreAssignment.setUrgency(assignment.getUrgency());
        firestoreAssignment.setCustomerName(assignment.getCustomerName());
        firestoreAssignment.setCustomerContact(assignment.getCustomerContact());
        firestoreAssignment.setVehicleType(assignment.getVehicleType());
        firestoreAssignment.setEstimatedFare(assignment.getEstimatedFare());
        firestoreAssignment.setNotes(assignment.getNotes());
        firestoreAssignment.setDriverId(assignment.getDriverId());
        firestoreAssignment.setVehicleId(assignment.getVehicleId());
        firestoreAssignment.setIsComplete(assignment.getIsComplete());

        return firestoreAssignment;
    }

    // Convert from FirestoreWorkAssignment to WorkAssignment
    public WorkAssignment toWorkAssignment() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        
        WorkAssignment assignment = new WorkAssignment();
        
        assignment.setAssignId(this.getAssignId());
        
        // Convert String to LocalDate and LocalDateTime
        if (this.getDate() != null) {
            assignment.setDate(LocalDate.parse(this.getDate(), dateFormatter));
        }
        if (this.getArrivedTime() != null) {
            assignment.setArrivedTime(LocalDateTime.parse(this.getArrivedTime(), dateTimeFormatter));
        }
        
        assignment.setDistance(this.getDistance());
        assignment.setDuration(this.getDuration());
        assignment.setStartLocation(this.getStartLocation());
        assignment.setEndLocation(this.getEndLocation());
        assignment.setStatus(this.getStatus());
        assignment.setUrgency(this.getUrgency());
        assignment.setCustomerName(this.getCustomerName());
        assignment.setCustomerContact(this.getCustomerContact());
        assignment.setVehicleType(this.getVehicleType());
        assignment.setEstimatedFare(this.getEstimatedFare());
        assignment.setNotes(this.getNotes());
        assignment.setDriverId(this.getDriverId());
        assignment.setVehicleId(this.getVehicleId());
        assignment.setIsComplete(this.getIsComplete());
        
        return assignment;
    }

    // Getters and setters
    public String getAssignId() {
        return assignId;
    }

    public void setAssignId(String assignId) {
        this.assignId = assignId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArrivedTime() {
        return arrivedTime;
    }

    public void setArrivedTime(String arrivedTime) {
        this.arrivedTime = arrivedTime;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Double getEstimatedFare() {
        return estimatedFare;
    }

    public void setEstimatedFare(Double estimatedFare) {
        this.estimatedFare = estimatedFare;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Boolean isComplete) {
        this.isComplete = isComplete;
    }
}
