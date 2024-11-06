package Models;

import java.util.Map;

public class FranchiseOwner extends Staff {
    private double grossProfit;
    private double hotelSize;
    private int numberOfHotelsOwned;
    private double pay;

    public FranchiseOwner(int id, String name, String contactInfo, String role, String status, double grossProfit, double hotelSize, int numberOfHotelsOwned) {
        super(id, name, contactInfo, role, status);
        this.grossProfit = grossProfit;
        this.hotelSize = hotelSize;
        this.numberOfHotelsOwned = numberOfHotelsOwned;
        this.pay = 0.0;
    }

    public FranchiseOwner() {
        super();
        this.pay = 0.0;
    }

    // Getters and setters
    public double getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(double grossProfit) {
        this.grossProfit = grossProfit;
    }

    public double getHotelSize() {
        return hotelSize;
    }

    public void setHotelSize(double hotelSize) {
        this.hotelSize = hotelSize;
    }

    public int getNumberOfHotelsOwned() {
        return numberOfHotelsOwned;
    }

    public void setNumberOfHotelsOwned(int numberOfHotelsOwned) {
        this.numberOfHotelsOwned = numberOfHotelsOwned;
    }

    public double getPay() {
        return pay;
    }

    public void addPay(double amount) {
        this.pay += amount;
    }
}