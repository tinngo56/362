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
}