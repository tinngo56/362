// Models/LoyaltyProgram.java
package Models;

public class LoyaltyProgram {
    private int id;
    private String tier;
    private int pointsAccumulated;
    private double rewardsAvailable;

    public LoyaltyProgram(int id, String tier, int pointsAccumulated) {
        this.id = id;
        this.tier = tier;
        this.pointsAccumulated = pointsAccumulated;
        this.rewardsAvailable = calculateRewards(pointsAccumulated);
    }

    public LoyaltyProgram() {
        super();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
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