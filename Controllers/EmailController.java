// Controllers/EmailController.java
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
        List<Map<String, Object>> customers = customerStore.loadAll();

        for (Map<String, Object> customerData : customers) {
            Customer customer = convertMapToCustomer(customerData);
            String email = (String) customerData.get("email");
            if (email != null && !email.isEmpty()) {
                System.out.println("Sending email to: " + email);
                System.out.println("Subject: " + subject);
                System.out.println("Message: " + message);
            }
        }
        System.out.println("Mass email sent to all customers.");
    }

    private Customer convertMapToCustomer(Map<String, Object> customerData) {
        Customer customer = new Customer();
        customer.setId(((Number) customerData.get("id")).intValue());
        customer.setLoyaltyProgramLevel((String) customerData.get("loyaltyProgramLevel"));
        customer.setPaymentMethod((String) customerData.get("paymentMethod"));
        customer.setNumberOfStays(((Number) customerData.get("numberOfStays")).intValue());
        return customer;
    }
}