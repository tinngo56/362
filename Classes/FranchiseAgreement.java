public class FranchiseAgreement {
    private int id;
    private String startDate;
    private String endDate;
    private double fees;
    private String conditions;

    public FranchiseAgreement(int id, String startDate, String endDate, double fees, String conditions) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fees = fees;
        this.conditions = conditions;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }
}