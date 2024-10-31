public class CEO {
    private int id;
    private String name;
    private String contactInfo;
    private int numberOfFranchisesManaged;
    private double grossProfit;
    private double feesCosts;

    public CEO(int id, String name, String contactInfo, int numberOfFranchisesManaged, double grossProfit, double feesCosts) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.numberOfFranchisesManaged = numberOfFranchisesManaged;
        this.grossProfit = grossProfit;
        this.feesCosts = feesCosts;
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

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

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