package Models;

import java.time.LocalDateTime;

public class VehicleForRent extends Mappable<VehicleForRent> {
    private String vehicleId;
    private String make;
    private String model;
    private int year;
    private String status;
    private double dailyRate;
    private String currentLocation;
    private LocalDateTime lastMaintenance;

    public VehicleForRent(String vehicleId, String make, String model, int year, 
                         double dailyRate, String currentLocation) {
        this.vehicleId = vehicleId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.dailyRate = dailyRate;
        this.currentLocation = currentLocation;
        this.status = "AVAILABLE";
    }

    public VehicleForRent() {
    }

    // Getters and setters
    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public LocalDateTime getLastMaintenance() {
        return lastMaintenance;
    }

    public void setLastMaintenance(LocalDateTime lastMaintenance) {
        this.lastMaintenance = lastMaintenance;
    }

    public boolean isAvailable() {
        return status.equals("AVAILABLE");
    }
}