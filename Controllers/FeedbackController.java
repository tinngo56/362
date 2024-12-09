// Controllers/FeedbackController.java
package Controllers;

import Storage.StorageHelper;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedbackController {
    private final StorageHelper storageHelper;

    public FeedbackController(String baseDirectory) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, "feedback");
    }

    public void submitFeedback(int customerId, String feedback) throws IOException {
        Map<String, Object> feedbackData = new HashMap<>();
        feedbackData.put("customerId", customerId);
        feedbackData.put("feedback", feedback);
        feedbackData.put("timestamp", System.currentTimeMillis());
        storageHelper.getStore("feedback").save(String.valueOf(customerId), feedbackData);
    }

    public List<Map<String, Object>> viewAllFeedback() throws IOException {
        return storageHelper.getStore("feedback").loadAll();
    }
}