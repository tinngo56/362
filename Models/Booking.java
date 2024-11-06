package Models;

import java.util.HashMap;
import java.util.Map;

public class Booking extends Mappable<Booking> {
    private int id;
    private String checkInDate;
    private String checkOutDate;
    private double totalPrice;
    private String paymentStatus;
    private int roomNum;

    private boolean canCheckOutEarly;

    public Booking(int id, String checkInDate, String checkOutDate, double totalPrice, String paymentStatus, int roomNum,
                   boolean canCheckOutEarly) {
        this.id = id;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = totalPrice;
        this.paymentStatus = paymentStatus;
        this.roomNum = roomNum;
        this.canCheckOutEarly = canCheckOutEarly;
    }

    public Booking() {
        super();
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

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public boolean isCanCheckOutEarly() {
        return canCheckOutEarly;
    }

    public void setCanCheckOutEarly(boolean canCheckOutEarly) {
        this.canCheckOutEarly = canCheckOutEarly;
    }
}