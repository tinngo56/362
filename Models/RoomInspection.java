package Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomInspection extends Mappable<RoomInspection> {
    private String roomNumber;
    private boolean hasDamage;
    private boolean needsMaintenance;
    private boolean hasMissingItems;
    private String damageDetails;
    private String maintenanceIssues;
    private List<String> missingItems;
    private Map<String, Integer> consumablesCount;
    private LocalDateTime inspectionTime;
    private String inspectedBy;

    public RoomInspection() {
    }

    // Getters and setters
    // ... standard getters and setters for all fields

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public boolean isHasDamage() {
        return hasDamage;
    }

    public void setHasDamage(boolean hasDamage) {
        this.hasDamage = hasDamage;
    }

    public String getDamageDetails() {
        return damageDetails;
    }

    public void setDamageDetails(String damageDetails) {
        this.damageDetails = damageDetails;
    }

    public boolean isNeedsMaintenance() {
        return needsMaintenance;
    }

    public void setNeedsMaintenance(boolean needsMaintenance) {
        this.needsMaintenance = needsMaintenance;
    }

    public String getMaintenanceIssues() {
        return maintenanceIssues;
    }

    public void setMaintenanceIssues(String maintenanceIssues) {
        this.maintenanceIssues = maintenanceIssues;
    }

    public List<String> getMissingItems() {
        return missingItems;
    }

    public void setInspectedBy(String name) {
        this.inspectedBy = name;
    }

    
    public String getInspectedBy() {
        return inspectedBy;
    }

    public LocalDateTime getInspectionTime() {
        return inspectionTime;
    }

    public void setInspectionTime(LocalDateTime inspectionTime) {
        this.inspectionTime = inspectionTime;
    }

    public void setLostAndFoundItems(List<String> missingItems) {
        this.missingItems = missingItems;
    }
    
    public boolean isHasMissingItems() {
        return hasMissingItems;
    }
}