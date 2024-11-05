package Models;

import java.util.HashMap;
import java.util.Map;

public class Customer {
    private int id;
    private String name;
    private String contactInfo;
    private String loyaltyProgramLevel;
    private String paymentMethod;
    private int numberOfStays;

    public Customer(int id, String name, String contactInfo, String loyaltyProgramLevel, String paymentMethod, int numberOfStays) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.loyaltyProgramLevel = loyaltyProgramLevel;
        this.paymentMethod = paymentMethod;
        this.numberOfStays = numberOfStays;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getLoyaltyProgramLevel() {
        return loyaltyProgramLevel;
    }

    public void setLoyaltyProgramLevel(String loyaltyProgramLevel) {
        this.loyaltyProgramLevel = loyaltyProgramLevel;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getNumberOfStays() {
        return numberOfStays;
    }

    public void setNumberOfStays(int numberOfStays) {
        this.numberOfStays = numberOfStays;
    }

    // Convert object to map
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("contactInfo", contactInfo);
        map.put("loyaltyProgramLevel", loyaltyProgramLevel);
        map.put("paymentMethod", paymentMethod);
        map.put("numberOfStays", numberOfStays);
        return map;
    }

    // Convert map to object
    public static Customer fromMap(Map<String, Object> map) {
        int id = (Integer) map.get("id");
        String name = (String) map.get("name");
        String contactInfo = (String) map.get("contactInfo");
        String loyaltyProgramLevel = (String) map.get("loyaltyProgramLevel");
        String paymentMethod = (String) map.get("paymentMethod");
        int numberOfStays = (Integer) map.get("numberOfStays");
        return new Customer(id, name, contactInfo, loyaltyProgramLevel, paymentMethod, numberOfStays);
    }
}