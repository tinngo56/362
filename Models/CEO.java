package Models;

import java.util.Map;

public class CEO extends Staff {
    private int numberOfFranchisesManaged;
    private double grossProfit;
    private double feesCosts;
    private double pay;

    public CEO(int id, String name, String contactInfo, String role, String status, int numberOfFranchisesManaged, double grossProfit, double feesCosts) {
        super(id, name, contactInfo, role, status);
        this.numberOfFranchisesManaged = numberOfFranchisesManaged;
        this.grossProfit = grossProfit;
        this.feesCosts = feesCosts;
        this.pay = 0.0;
    }

    public CEO() {
        super();
        this.pay = 0.0;
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

    public double getPay() {
        return pay;
    }

    public void addPay(double amount) {
        this.pay += amount;
    }
}