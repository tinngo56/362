package Controllers;

import Models.Customer;
import Storage.StorageHelper;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class EmailController {
    private final StorageHelper storageHelper;

    public EmailController(String baseDirectory) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, "customers");
    }

    public void sendMassEmail(String subject, String message) throws IOException {
        StorageHelper.DataStore<Map<String, Object>> customerStore = storageHelper.getStore("customers");

        List<Map<String, Object>> customersData = customerStore.loadAll();
        for (Map<String, Object> customerData : customersData) {
            Customer customer = convertMapToCustomer(customerData);
            if (customer.getLoyaltyProgramLevel() != null && !customer.getLoyaltyProgramLevel().isEmpty()) {
                sendEmail(customer.getContactInfo(), subject, message);
            }
        }
    }

    private void sendEmail(String email, String subject, String message) {
        // Simulate sending an email
        System.out.println("Sending email to: " + email);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
        System.out.println("Email sent successfully!\n");
    }

    private Customer convertMapToCustomer(Map<String, Object> map) {
        int id = (Integer) map.get("id");
        String name = (String) map.get("name");
        String contactInfo = (String) map.get("contactInfo");
        String loyaltyProgramLevel = (String) map.get("loyaltyProgramLevel");
        String paymentMethod = (String) map.get("paymentMethod");
        int numberOfStays = (Integer) map.get("numberOfStays");
        return new Customer(id, name, contactInfo, loyaltyProgramLevel, paymentMethod, numberOfStays);
    }
}