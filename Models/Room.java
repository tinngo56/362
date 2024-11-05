package Models;

import java.util.HashMap;
import java.util.Map;

public class Room {
    private int roomNumber;
    private String roomType;
    private double pricePerNight;
    private String status;
    private String currentGuest;
    private String lastCleaned;

    public Room(int roomNumber, String roomType, double pricePerNight, String status, String currentGuest, String lastCleaned) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.status = status;
        this.currentGuest = currentGuest;
        this.lastCleaned = lastCleaned;
    }

    // Getters and setters
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

    // Convert object to map
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("roomNumber", roomNumber);
        map.put("roomType", roomType);
        map.put("pricePerNight", pricePerNight);
        map.put("status", status);
        map.put("currentGuest", currentGuest);
        map.put("lastCleaned", lastCleaned);
        return map;
    }

    // Convert map to object
    public static Room fromMap(Map<String, Object> map) {
        int roomNumber = ((Number)(map.get("roomNumber"))).intValue();
        String roomType = (String) map.get("roomType");
        double pricePerNight = (Double) map.get("pricePerNight");
        String status = (String) map.get("status");
        String currentGuest = (String) map.get("currentGuest");
        String lastCleaned = (String) map.get("lastCleaned");
        return new Room(roomNumber, roomType, pricePerNight, status, currentGuest, lastCleaned);
    }
}