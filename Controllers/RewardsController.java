// Controllers/RewardsController.java
package Controllers;

import Models.LoyaltyProgram;
import Models.Booking;
import Storage.StorageHelper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RewardsController {
    private final StorageHelper storageHelper;

    public RewardsController(String baseDirectory) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, "loyaltyPrograms");
    }

    public StorageHelper getStorageHelper() {
        return storageHelper;
    }

    public void earnPoints(int customerId, double amountSpent) throws IOException {
        StorageHelper.DataStore<Map<String, Object>> customerStore = storageHelper.getStore("customers");
        StorageHelper.DataStore<Map<String, Object>> loyaltyStore = storageHelper.getStore("loyaltyPrograms");

        Map<String, Object> customerData = customerStore.load(String.valueOf(customerId));
        if (customerData == null) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }

        Map<String, Object> loyaltyData = loyaltyStore.load(String.valueOf(customerId));
        if (loyaltyData == null) {
            loyaltyData = new HashMap<>();
            loyaltyData.put("id", customerId);
            loyaltyData.put("pointsAccumulated", 0);
        }

        int currentPoints = (int) loyaltyData.get("pointsAccumulated");
        int newPoints = currentPoints + (int) amountSpent;
        loyaltyData.put("pointsAccumulated", newPoints);

        LoyaltyProgram loyaltyProgram = new LoyaltyProgram();
        loyaltyProgram.setId(customerId);
        loyaltyProgram.setPointsAccumulated(newPoints);

        loyaltyStore.save(String.valueOf(customerId), loyaltyData);
    }

    public void processBooking(Booking booking) throws IOException {
        int customerId = booking.getId();
        double amountSpent = booking.getTotalPrice();
        earnPoints(customerId, amountSpent);
    }
}