package Models;

public class Insurance extends Mappable<Insurance> {
    private String insuranceId;
    private String type;
    private double coverage;
    private double dailyRate;

    public Insurance(String insuranceId, String type, double coverage, double dailyRate) {
        this.insuranceId = insuranceId;
        this.type = type;
        this.coverage = coverage;
        this.dailyRate = dailyRate;
    }

    public Insurance() {
    }

    // Getters and setters
    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getCoverage() {
        return coverage;
    }

    public void setCoverage(double coverage) {
        this.coverage = coverage;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    // Business methods
    public double calculateCost(int days) {
        return dailyRate * days;
    }
}