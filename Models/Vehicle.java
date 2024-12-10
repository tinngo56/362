package Models;

import java.time.LocalDateTime;

public class Vehicle extends Mappable<Vehicle> {
    private String licensePlate;
    private String make;
    private String model;
    private double fuelLevel;
    private String condition;
    private String parkingSpace;
    private LocalDateTime parkTime;
    private String status;
    private String claimTicket;
    private String parkedBy;

    public Vehicle() {
    } // Required for Mappable

    // Getters and Setters
    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
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

    public double getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(String parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public LocalDateTime getParkTime() {
        return parkTime;
    }

    public void setParkTime(LocalDateTime parkTime) {
        this.parkTime = parkTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClaimTicket() {
        return claimTicket;
    }

    public void setClaimTicket(String claimTicket) {
        this.claimTicket = claimTicket;
    }

    public String getParkedBy() {
        return parkedBy;
    }

    public void setParkedBy(String parkedBy) {
        this.parkedBy = parkedBy;
    }
}
