package Models;

import java.util.HashMap;
import java.util.Map;

public class Hotel {
    private int id;
    private String name;
    private String location;
    private int roomCount;
    private int numAvailableRooms;
    private double propertyTax;
    private double size;
    private double rating;

    public Hotel(int id, String name, String location, int roomCount, double propertyTax, double size, double rating, int numAvailableRooms) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.roomCount = roomCount;
        this.propertyTax = propertyTax;
        this.size = size;
        this.rating = rating;
        this.numAvailableRooms = numAvailableRooms;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }

    public double getPropertyTax() {
        return propertyTax;
    }

    public void setPropertyTax(double propertyTax) {
        this.propertyTax = propertyTax;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumAvailableRooms() {
        return numAvailableRooms;
    }

    public void setNumAvailableRooms(int numAvailableRooms) {
        this.numAvailableRooms = numAvailableRooms;
    }

    // Convert object to map
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("location", location);
        map.put("roomCount", roomCount);
        map.put("numAvailableRooms", numAvailableRooms);
        map.put("propertyTax", propertyTax);
        map.put("size", size);
        map.put("rating", rating);
        return map;
    }

    // Convert map to object
    public static Hotel fromMap(Map<String, Object> map) {
        int id = ((Number)(map.get("id"))).intValue();
        String name = (String) map.get("name");
        String location = (String) map.get("location");
        int roomCount = ((Number)(map.get("roomCount"))).intValue();
        int numAvailableRooms = ((Number)(map.get("numAvailableRooms"))).intValue();
        double propertyTax = (Double) map.get("propertyTax");
        double size = (Double) map.get("size");
        double rating = (Double) map.get("rating");
        return new Hotel(id, name, location, roomCount, propertyTax, size, rating, numAvailableRooms);
    }
}