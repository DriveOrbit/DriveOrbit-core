package lk.driveorbit.DriveOrbit_core.vehicle.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vehicleId;
    private String vehicleNumber;
    private String vehicleType;
    private String vehicleModel;
    private String vehicleImage;
    private String vehicleStatus;
    
    // New fields
    private String plateNumber;
    private String condition;
    private Double fuelConsumption;
    private Integer recommendedDistance;
    private String warnings;
    private String fuelType;
    private String gearSystem;
    private Boolean hasSpareTools;
    private Boolean hasEmergencyKit;

    //getters

    public Integer getVehicleId() {
        return vehicleId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public String getVehicleStatus() {
        return vehicleStatus;
    }
    
    // Getters for new fields
    public String getPlateNumber() {
        return plateNumber;
    }
    
    public String getCondition() {
        return condition;
    }
    
    public Double getFuelConsumption() {
        return fuelConsumption;
    }
    
    public Integer getRecommendedDistance() {
        return recommendedDistance;
    }
    
    public String getWarnings() {
        return warnings;
    }
    
    public String getFuelType() {
        return fuelType;
    }
    
    public String getGearSystem() {
        return gearSystem;
    }
    
    public Boolean getHasSpareTools() {
        return hasSpareTools;
    }
    
    public Boolean getHasEmergencyKit() {
        return hasEmergencyKit;
    }

    // Existing setters
    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }
    
    // Setters for new fields
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }
    
    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    public void setFuelConsumption(Double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }
    
    public void setRecommendedDistance(Integer recommendedDistance) {
        this.recommendedDistance = recommendedDistance;
    }
    
    public void setWarnings(String warnings) {
        this.warnings = warnings;
    }
    
    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
    
    public void setGearSystem(String gearSystem) {
        this.gearSystem = gearSystem;
    }
    
    public void setHasSpareTools(Boolean hasSpareTools) {
        this.hasSpareTools = hasSpareTools;
    }
    
    public void setHasEmergencyKit(Boolean hasEmergencyKit) {
        this.hasEmergencyKit = hasEmergencyKit;
    }
}