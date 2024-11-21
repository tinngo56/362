package Models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Room extends Facility {
    // Basic room properties
    private int roomNumber;
    private String roomType;
    private double pricePerNight;
    private String status;
    private String currentGuest;
    private String lastCleaned;
    private int hotelId;
    
    // Inventory properties
    private Map<String, Integer> consumables;
    private Map<String, Integer> linens;
    private LocalDateTime inventoryLastUpdated;
    private String inventoryUpdatedBy;

    // Full constructor
    public Room(int roomNumber, String roomType, double pricePerNight, String status, 
                String currentGuest, String lastCleaned, int hotelId) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.status = status;
        this.currentGuest = currentGuest;
        this.lastCleaned = lastCleaned;
        this.hotelId = hotelId;
        super.setAccessLevel(AccessLevels.ROOM);
        
        // Initialize inventory maps
        this.consumables = new HashMap<>();
        this.linens = new HashMap<>();
    }

    // Default constructor
    public Room() {
        super();
        this.consumables = new HashMap<>();
        this.linens = new HashMap<>();
    }

    // Inventory management methods
    public void updateConsumable(String item, int quantity) {
        consumables.put(item, quantity);
        this.inventoryLastUpdated = LocalDateTime.now();
    }

    public void updateLinens(String item, int quantity) {
        linens.put(item, quantity);
        this.inventoryLastUpdated = LocalDateTime.now();
    }

    // Getters and setters for basic room properties
    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentGuest() {
        return currentGuest;
    }

    public void setCurrentGuest(String currentGuest) {
        this.currentGuest = currentGuest;
    }

    public String getLastCleaned() {
        return lastCleaned;
    }

    public void setLastCleaned(String lastCleaned) {
        this.lastCleaned = lastCleaned;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    // Getters and setters for inventory properties
    public Map<String, Integer> getConsumables() {
        return consumables;
    }

    public Map<String, Integer> getLinens() {
        return linens;
    }

    public LocalDateTime getInventoryLastUpdated() {
        return inventoryLastUpdated;
    }

    public String getInventoryUpdatedBy() {
        return inventoryUpdatedBy;
    }

    public void setInventoryUpdatedBy(String updatedBy) {
        this.inventoryUpdatedBy = updatedBy;
    }
}