public class LoyaltyProgram {
    private int id;
    private String tier;
    private int pointsAccumulated;
    private String rewardsAvailable;

    public LoyaltyProgram(int id, String tier, int pointsAccumulated, String rewardsAvailable) {
        this.id = id;
        this.tier = tier;
        this.pointsAccumulated = pointsAccumulated;
        this.rewardsAvailable = rewardsAvailable;
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
    }

    public String getRewardsAvailable() {
        return rewardsAvailable;
    }

    public void setRewardsAvailable(String rewardsAvailable) {
        this.rewardsAvailable = rewardsAvailable;
    }
}