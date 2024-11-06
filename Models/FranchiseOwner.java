package Models;

public class FranchiseOwner extends Staff {
    private double grossProfit;
    private double hotelSize;
    private int numberOfHotelsOwned;

    public FranchiseOwner(int id, String name, String contactInfo, String role, String status, double grossProfit, double hotelSize, int numberOfHotelsOwned) {
        super(id, name, contactInfo, role, status);
        this.grossProfit = grossProfit;
        this.hotelSize = hotelSize;
        this.numberOfHotelsOwned = numberOfHotelsOwned;
    }

    public FranchiseOwner() {
        super();
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
}