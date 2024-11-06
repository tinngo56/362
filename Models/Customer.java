package Models;

import java.util.HashMap;
import java.util.Map;

public class Customer extends Person {
    private int id;
    private String loyaltyProgramLevel;
    private String paymentMethod;
    private int numberOfStays;

    public Customer(int id, String name, String contactInfo, String loyaltyProgramLevel, String paymentMethod, int numberOfStays) {
        super(id, name, contactInfo);
        this.loyaltyProgramLevel = loyaltyProgramLevel;
        this.paymentMethod = paymentMethod;
        this.numberOfStays = numberOfStays;
    }

    public Customer() {
        super();
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
}