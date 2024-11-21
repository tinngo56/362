package Models;

import java.time.LocalDateTime;
import java.util.List;

public class RoomServiceOrder extends Mappable<RoomServiceOrder> {
    private String orderId;
    private int roomNumber;
    private String customerId;
    private List<MenuItem> items;
    private double totalAmount;
    private OrderStatus status;
    private LocalDateTime deliveryTime;
    private String notes;
    private PaymentMethod paymentMethod;
    private String staffId;
    
    public enum OrderStatus {
        RECEIVED, CONFIRMED, IN_PREPARATION, READY_FOR_DELIVERY, 
        DELIVERY_ATTEMPTED, DELIVERED, CANCELLED
    }
    
    public enum PaymentMethod {
        ROOM_CHARGE, CREDIT_CARD, CASH
    }
    
    
    public RoomServiceOrder() {
    }
    
    public RoomServiceOrder(String orderId, int roomNumber, String customerId, List<MenuItem> items, double totalAmount, OrderStatus status, LocalDateTime deliveryTime, String notes, PaymentMethod paymentMethod, String staffId) {
        this.orderId = orderId;
        this.roomNumber = roomNumber;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
        this.deliveryTime = deliveryTime;
        this.notes = notes;
        this.paymentMethod = paymentMethod;
        this.staffId = staffId;
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public int getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public List<MenuItem> getItems() {
        return items;
    }
    
    public void setItems(List<MenuItem> items) {
        this.items = items;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }
    
    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getStaffId() {
        return staffId;
    }
    
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
}
