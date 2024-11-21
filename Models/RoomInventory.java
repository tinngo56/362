package Models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class RoomInventory extends Mappable<RoomInventory> {
    private String roomNumber;
    private Map<String, Integer> consumables;
    private Map<String, Integer> linens;
    private LocalDateTime lastUpdated;
    private String updatedBy;

    public RoomInventory() {
        this.consumables = new HashMap<>();
        this.linens = new HashMap<>();
    }

    public void updateConsumable(String item, int quantity) {
        consumables.put(item, quantity);
        this.lastUpdated = LocalDateTime.now();
    }

    public void updateLinens(String item, int quantity) {
        linens.put(item, quantity);
        this.lastUpdated = LocalDateTime.now();
    }

    public Map<String, Integer> getConsumables() {
        return consumables;
    }

    public Map<String, Integer> getLinens() {
        return linens;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}
