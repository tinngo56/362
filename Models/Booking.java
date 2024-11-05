package Models;

import java.util.HashMap;
import java.util.Map;

public class Booking {
    private int id;
    private String checkInDate;
    private String checkOutDate;
    private double totalPrice;
    private String paymentStatus;
    private Room room;

    public Booking(int id, String checkInDate, String checkOutDate, double totalPrice, String paymentStatus, Room room) {
        this.id = id;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = totalPrice;
        this.paymentStatus = paymentStatus;
        this.room = room;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    // Convert object to map
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("checkInDate", checkInDate);
        map.put("checkOutDate", checkOutDate);
        map.put("totalPrice", totalPrice);
        map.put("paymentStatus", paymentStatus);
        map.put("room", room.toMap()); // Assuming Room class has a toMap() method
        return map;
    }

    // Convert map to object
    public static Booking fromMap(Map<String, Object> map) {
        int id = ((Number)(map.get("id"))).intValue();
        String checkInDate = (String) map.get("checkInDate");
        String checkOutDate = (String) map.get("checkOutDate");
        double totalPrice = (Double) map.get("totalPrice");
        String paymentStatus = (String) map.get("paymentStatus");
        @SuppressWarnings("unchecked")
        Map<String, Object> roomMap = (Map<String, Object>) map.get("room");
        Room room = Room.fromMap(roomMap); // Assuming Room class has a fromMap() method
        return new Booking(id, checkInDate, checkOutDate, totalPrice, paymentStatus, room);
    }
}