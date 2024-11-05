package Models;

import java.util.Map;

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

    // Convert object to map
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("grossProfit", grossProfit);
        map.put("hotelSize", hotelSize);
        map.put("numberOfHotelsOwned", numberOfHotelsOwned);
        return map;
    }

    // Convert map to object
    public static FranchiseOwner fromMap(Map<String, Object> map) {
        int id = (Integer) map.get("id");
        String name = (String) map.get("name");
        String contactInfo = (String) map.get("contactInfo");
        String role = (String) map.get("role");
        String status = (String) map.get("status");
        double grossProfit = (Double) map.get("grossProfit");
        double hotelSize = (Double) map.get("hotelSize");
        int numberOfHotelsOwned = (Integer) map.get("numberOfHotelsOwned");
        return new FranchiseOwner(id, name, contactInfo, role, status, grossProfit, hotelSize, numberOfHotelsOwned);
    }
}