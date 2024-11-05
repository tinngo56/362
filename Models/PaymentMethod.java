package Models;

import java.util.HashMap;
import java.util.Map;

public class PaymentMethod {
    private int id;
    private String type;
    private String cardNumber;
    private String expiryDate;
    private String paymentStatus;

    public PaymentMethod(int id, String type, String cardNumber, String expiryDate, String paymentStatus) {
        this.id = id;
        this.type = type;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.paymentStatus = paymentStatus;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    // Convert object to map
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("type", type);
        map.put("cardNumber", cardNumber);
        map.put("expiryDate", expiryDate);
        map.put("paymentStatus", paymentStatus);
        return map;
    }

    // Convert map to object
    public static PaymentMethod fromMap(Map<String, Object> map) {
        int id = (Integer) map.get("id");
        String type = (String) map.get("type");
        String cardNumber = (String) map.get("cardNumber");
        String expiryDate = (String) map.get("expiryDate");
        String paymentStatus = (String) map.get("paymentStatus");
        return new PaymentMethod(id, type, cardNumber, expiryDate, paymentStatus);
    }
}