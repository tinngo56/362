package Controllers;

import Models.Booking;
import Models.CEO;
import Models.Room;
import Storage.StorageHelper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BookingController {
    private StorageHelper storageHelper;
    private final String STORE_NAME = "bookings";
    private final String FRANCHISE_OWNER_PAY_KEY = "franchiseOwnerPay";
    private final String CEO_PAY_KEY = "ceoPay";
    private CEO ceo;

    public BookingController(String baseDirectory, CEO ceo) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, STORE_NAME);
        this.ceo = ceo;
    }

    public void createBooking(Booking booking) throws IOException {
        Map<String, Object> bookingMap = convertBookingToMap(booking);
        storageHelper.getStore(STORE_NAME).save(String.valueOf(booking.getId()), bookingMap);
        double bookingAmount = booking.getTotalPrice();
        addPayToFranchiseOwner(bookingAmount);
        double ceoFee = bookingAmount * 0.05;
        addPayToCEO(ceoFee);
    }

    public Booking getBooking(int id) throws IOException {
        Map<String, Object> data = storageHelper.getStore(STORE_NAME).load(String.valueOf(id));
        return data != null ? convertMapToBooking(data) : null;
    }

    public void updateBooking(Booking booking) throws IOException {
        Map<String, Object> bookingMap = convertBookingToMap(booking);
        storageHelper.getStore(STORE_NAME).save(String.valueOf(booking.getId()), bookingMap);
    }

    public void deleteBooking(int id) throws IOException {
        storageHelper.getStore(STORE_NAME).delete(String.valueOf(id));
    }

    public int getNumOfBookings() throws IOException {
        return storageHelper.getStore(STORE_NAME).loadAll().size();
    }

    public double getFranchiseOwnerPay() throws IOException {
        Map<String, Object> data = storageHelper.getStore(STORE_NAME).load(FRANCHISE_OWNER_PAY_KEY);
        return data != null ? (double) data.get("pay") : 0.0;
    }

    public double getCEOPay() throws IOException {
        Map<String, Object> data = storageHelper.getStore(STORE_NAME).load(CEO_PAY_KEY);
        return data != null ? (double) data.get("pay") : 0.0;
    }

    private void addPayToFranchiseOwner(double amount) throws IOException {
        double currentPay = getFranchiseOwnerPay();
        Map<String, Object> payMap = new HashMap<>();
        payMap.put("pay", currentPay + amount);
        storageHelper.getStore(STORE_NAME).save(FRANCHISE_OWNER_PAY_KEY, payMap);
    }

    public void addPayToCEO(double amount) throws IOException {
        double currentPay = getCEOPay();
        Map<String, Object> payMap = new HashMap<>();
        payMap.put("pay", currentPay + amount);
        storageHelper.getStore(STORE_NAME).save(CEO_PAY_KEY, payMap);
    }

    private Map<String, Object> convertBookingToMap(Booking booking) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", booking.getId());
        map.put("checkInDate", booking.getCheckInDate());
        map.put("checkOutDate", booking.getCheckOutDate());
        map.put("totalPrice", booking.getTotalPrice());
        map.put("paymentStatus", booking.getPaymentStatus());
        map.put("room", booking.getRoomNum());
        return map;
    }

    private Booking convertMapToBooking(Map<String, Object> map) {
        int id = (Integer) map.get("id");
        String checkInDate = (String) map.get("checkInDate");
        String checkOutDate = (String) map.get("checkOutDate");
        double totalPrice = (Double) map.get("totalPrice");
        String paymentStatus = (String) map.get("paymentStatus");
        int roomNumber = (Integer) map.get("room");
        Room room = new Room(roomNumber, "UNKNOWN", 0.0, "UNKNOWN", null, null); // Simplified for example
        return new Booking(id, checkInDate, checkOutDate, totalPrice, paymentStatus, roomNumber, false);
    }
}