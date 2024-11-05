package Models;

import java.util.Map;

public class CEO extends Staff {
    private int numberOfFranchisesManaged;
    private double grossProfit;
    private double feesCosts;

    public CEO(int id, String name, String contactInfo, String role, String status, int numberOfFranchisesManaged, double grossProfit, double feesCosts) {
        super(id, name, contactInfo, role, status);
        this.numberOfFranchisesManaged = numberOfFranchisesManaged;
        this.grossProfit = grossProfit;
        this.feesCosts = feesCosts;
    }

    // Getters and setters
    public int getNumberOfFranchisesManaged() {
        return numberOfFranchisesManaged;
    }

    public void setNumberOfFranchisesManaged(int numberOfFranchisesManaged) {
        this.numberOfFranchisesManaged = numberOfFranchisesManaged;
    }

    public double getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(double grossProfit) {
        this.grossProfit = grossProfit;
    }

    public double getFeesCosts() {
        return feesCosts;
    }

    public void setFeesCosts(double feesCosts) {
        this.feesCosts = feesCosts;
    }

    // Convert object to map
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("numberOfFranchisesManaged", numberOfFranchisesManaged);
        map.put("grossProfit", grossProfit);
        map.put("feesCosts", feesCosts);
        return map;
    }

    // Convert map to object
    public static CEO fromMap(Map<String, Object> map) {
        int id = (Integer) map.get("id");
        String name = (String) map.get("name");
        String contactInfo = (String) map.get("contactInfo");
        String role = (String) map.get("role");
        String status = (String) map.get("status");
        int numberOfFranchisesManaged = (Integer) map.get("numberOfFranchisesManaged");
        double grossProfit = (Double) map.get("grossProfit");
        double feesCosts = (Double) map.get("feesCosts");
        return new CEO(id, name, contactInfo, role, status, numberOfFranchisesManaged, grossProfit, feesCosts);
    }
}