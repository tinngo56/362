package Models;

import java.util.HashMap;
import java.util.Map;

public class PaymentMethod extends Mappable<PaymentMethod> {
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

    public PaymentMethod() {
        super();
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
}