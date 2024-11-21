package Models;

public class Charge extends Mappable<Charge> {
    private double amount;
    private String roomNumber;
    private String lastModified;
    private ChargeType type;
    private String guestId;
    private String timestamp;

    public enum ChargeType {
        ROOM_SERVICE, MINIBAR, SPA, RESTAURANT, PARKING
    }

    // Default constructor required for Mappable
    public Charge() {
    }

    public Charge(double amount, String roomNumber, ChargeType type, String guestId) {
        this.amount = amount;
        this.roomNumber = roomNumber;
        this.type = type;
        this.guestId = guestId;
    }

    // Getters and setters
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public ChargeType getType() {
        return type;
    }

    public void setType(ChargeType type) {
        this.type = type;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}