package Models;

import java.time.LocalDateTime;
import java.util.List;

public class VehicleInspection extends Mappable<VehicleInspection> {
    private String inspectionId;
    private String licensePlate;
    private String inspectedBy;
    private LocalDateTime inspectionTime;
    private boolean hasDamage;
    private String damageDetails;
    private List<String> personalItems;
    private double fuelLevel;

    public VehicleInspection() {} // Required for Mappable

    // Getters and Setters
    public String getInspectionId() { return inspectionId; }
    public void setInspectionId(String inspectionId) { this.inspectionId = inspectionId; }
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public String getInspectedBy() { return inspectedBy; }
    public void setInspectedBy(String inspectedBy) { this.inspectedBy = inspectedBy; }
    public LocalDateTime getInspectionTime() { return inspectionTime; }
    public void setInspectionTime(LocalDateTime inspectionTime) { this.inspectionTime = inspectionTime; }
    public boolean isHasDamage() { return hasDamage; }
    public void setHasDamage(boolean hasDamage) { this.hasDamage = hasDamage; }
    public String getDamageDetails() { return damageDetails; }
    public void setDamageDetails(String damageDetails) { this.damageDetails = damageDetails; }
    public List<String> getPersonalItems() { return personalItems; }
    public void setPersonalItems(List<String> personalItems) { this.personalItems = personalItems; }
    public double getFuelLevel() { return fuelLevel; }
    public void setFuelLevel(double fuelLevel) { this.fuelLevel = fuelLevel; }
}