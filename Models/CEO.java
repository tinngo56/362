package Models;

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

    public CEO() {
        super();
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
}