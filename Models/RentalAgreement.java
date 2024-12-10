package Models;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RentalAgreement extends Mappable<RentalAgreement> {
    private String agreementId;
    private Customer customer;
    private VehicleForRent vehicle;
    private Insurance insurance;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean insuranceSelected;
    private double deposit;
    private double dailyRate;
    private String status;

    // Constructors
    public RentalAgreement(String agreementId, Customer customer, VehicleForRent vehicle,
                          Insurance insurance, LocalDateTime startTime, LocalDateTime endTime,
                          double deposit, double dailyRate) {
        this.agreementId = agreementId;
        this.customer = customer;
        this.vehicle = vehicle;
        this.insurance = insurance;
        this.startTime = startTime;
        this.endTime = endTime;
        this.insuranceSelected = (insurance != null);
        this.deposit = deposit;
        this.dailyRate = dailyRate;
        this.status = "ACTIVE";
    }

    public RentalAgreement() {
    }

    // Getters and setters
    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public VehicleForRent getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleForRent vehicle) {
        this.vehicle = vehicle;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
        this.insuranceSelected = (insurance != null);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Business methods
    public double calculateTotalCost() {
        long days = ChronoUnit.DAYS.between(startTime, endTime);
        double rentalCost = dailyRate * days;
        double insuranceCost = insuranceSelected ? insurance.calculateCost((int)days) : 0;
        return rentalCost + insuranceCost;
    }

    public void extendRental(LocalDateTime newEndTime) {
        if (newEndTime.isBefore(this.endTime)) {
            throw new IllegalArgumentException("New end time must be after current end time");
        }
        this.endTime = newEndTime;
    }

    public void completeRental() {
        this.status = "COMPLETED";
        this.vehicle.setStatus("AVAILABLE");
    }

    public double applyLoyaltyDiscount(double rate) {
        double discount = 0.0;
        switch (customer.getLoyaltyProgramLevel().toUpperCase()) {
            case "GOLD":
                discount = 0.15;
                break;
            case "SILVER":
                discount = 0.10;
                break;
            case "BRONZE":
                discount = 0.05;
                break;
        }
        return rate * (1 - discount);
    }

    public boolean isInsuranceSelected() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isInsuranceSelected'");
    }
}