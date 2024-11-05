package Models;

public class FranchiseOwner {
    private int id;
    private String name;
    private double grossProfit;
    private double hotelSize;
    private int numberOfHotelsOwned;
    private String contactInfo;

    public FranchiseOwner(int id, String name, double grossProfit, double hotelSize, int numberOfHotelsOwned, String contactInfo) {
        this.id = id;
        this.name = name;
        this.grossProfit = grossProfit;
        this.hotelSize = hotelSize;
        this.numberOfHotelsOwned = numberOfHotelsOwned;
        this.contactInfo = contactInfo;
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

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}