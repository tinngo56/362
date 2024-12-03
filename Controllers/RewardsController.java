// Controllers/RewardsController.java
package Controllers;

import Models.LoyaltyProgram;
import Models.Booking;
import Storage.StorageHelper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RewardsController {
    private final StorageHelper loyaltyStorageHelper;
    private final StorageHelper customerStorageHelper;

    public RewardsController(String baseDirectory) throws IOException {
        this.loyaltyStorageHelper = new StorageHelper(baseDirectory, "loyaltyPrograms");
        this.customerStorageHelper = new StorageHelper(baseDirectory, "customers");
    }

    public StorageHelper getLoyaltyStorageHelper() {
        return loyaltyStorageHelper;
    }

    public StorageHelper getCustomerStorageHelper() {
        return customerStorageHelper;
    }

    public void earnPoints(int customerId, double amountSpent) throws IOException {
        StorageHelper.DataStore<Map<String, Object>> customerStore = customerStorageHelper.getStore("customers");
        StorageHelper.DataStore<Map<String, Object>> loyaltyStore = loyaltyStorageHelper.getStore("loyaltyPrograms");
        Map<String, Object> customerData = customerStore.load(String.valueOf(customerId));
        if (customerData == null) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }

        Map<String, Object> loyaltyData = loyaltyStore.load(String.valueOf(customerId));
        if (loyaltyData == null) {
            loyaltyData = new HashMap<>();
            loyaltyData.put("id", customerId);
            loyaltyData.put("pointsAccumulated", 0);
            loyaltyData.put("rewardsAvailable", 0.0);
        }

        int currentPoints = (int) loyaltyData.get("pointsAccumulated");
        int newPoints = currentPoints + (int) amountSpent;
        double newRewardsAvailable = newPoints * 0.5; // Assuming 1 point = $0.5 rewards

        loyaltyData.put("pointsAccumulated", newPoints);
        loyaltyData.put("rewardsAvailable", newRewardsAvailable);

        LoyaltyProgram loyaltyProgram = new LoyaltyProgram();
        loyaltyProgram.setId(customerId);
        loyaltyProgram.setPointsAccumulated(newPoints);
        loyaltyProgram.setRewardsAvailable(newRewardsAvailable);

        loyaltyStore.save(String.valueOf(customerId), loyaltyData);
    }

    public void processBooking(Booking booking) throws IOException {
        int customerId = booking.getCustomerId();
        double amountSpent = booking.getTotalPrice();
        earnPoints(customerId, amountSpent);
    }
}