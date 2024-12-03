package Models;

public class LoyaltyProgram {
    private int id;
    private int pointsAccumulated;
    private double rewardsAvailable;
    private String tier;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPointsAccumulated() {
        return pointsAccumulated;
    }

    public void setPointsAccumulated(int pointsAccumulated) {
        this.pointsAccumulated = pointsAccumulated;
        this.rewardsAvailable = calculateRewards(pointsAccumulated);
        updateTier();
    }

    public double getRewardsAvailable() {
        return rewardsAvailable;
    }

    public void setRewardsAvailable(double rewardsAvailable) {
        this.rewardsAvailable = rewardsAvailable;
    }

    public String getTier() {
        return tier;
    }

    private double calculateRewards(int points) {
        return points * 0.5;
    }

    private void updateTier() {
        if (pointsAccumulated >= 2000) {
            tier = "Diamond";
        } else if (pointsAccumulated >= 1500) {
            tier = "Platinum";
        } else if (pointsAccumulated >= 1000) {
            tier = "Gold";
        } else if (pointsAccumulated >= 500) {
            tier = "Silver";
        } else {
            tier = "Bronze";
        }
    }
}