package Controllers;

import Models.*;
import Storage.StorageHelper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class BookingController {
    private StorageHelper bookingStorageHelper;
    private StorageHelper salaryStorageHelper;
    private final String STORE_NAME = "bookings";
    private final String SALARY_NAME = "salaries";
    private final String FRANCHISE_OWNER_PAY_KEY = "franchiseOwnerPay";
    private final String CEO_PAY_KEY = "ceoPay";
    private CEO ceo;

    public BookingController(String baseDirectory, CEO ceo) throws IOException {
        this.bookingStorageHelper = new StorageHelper(baseDirectory, STORE_NAME);
        this.salaryStorageHelper = new StorageHelper(baseDirectory, SALARY_NAME);
        this.ceo = ceo;
    }

    public void createBooking(Booking booking) throws IOException {
        bookingStorageHelper.getStore(STORE_NAME).save(String.valueOf(booking.getId()), booking.toMap());
        double bookingAmount = booking.getTotalPrice();
        addPayToFranchiseOwner(bookingAmount);
        double ceoFee = bookingAmount * 0.05;
        addPayToCEO(ceoFee);
    }

    public Booking getBooking(int id) throws IOException {
        Map<String, Object> data = bookingStorageHelper.getStore(STORE_NAME).load(String.valueOf(id));
        return data != null ? new Booking().fromMap(data) : null;
    }

    public void updateBooking(Booking booking) throws IOException {
        bookingStorageHelper.getStore(STORE_NAME).save(String.valueOf(booking.getId()), booking.toMap());
    }

    public void deleteBooking(int id) throws IOException {
        bookingStorageHelper.getStore(STORE_NAME).delete(String.valueOf(id));
    }

    public int getNumOfBookings() throws IOException {
        return bookingStorageHelper.getStore(STORE_NAME).loadAll().size();
    }

    public double getFranchiseOwnerPay() throws IOException {
        Map<String, Object> data = salaryStorageHelper.getStore(SALARY_NAME).load(FRANCHISE_OWNER_PAY_KEY);
        return data != null ? (double) data.get("pay") : 0.0;
    }

    public double getCEOPay() throws IOException {
        Map<String, Object> data = salaryStorageHelper.getStore(SALARY_NAME).load(CEO_PAY_KEY);
        return data != null ? (double) data.get("pay") : 0.0;
    }

    private void addPayToFranchiseOwner(double amount) throws IOException {
        double currentPay = getFranchiseOwnerPay();
        Map<String, Object> payMap = new HashMap<>();
        payMap.put("pay", currentPay + amount);
        salaryStorageHelper.getStore(SALARY_NAME).save(FRANCHISE_OWNER_PAY_KEY, payMap);
    }

    public void addPayToCEO(double amount) throws IOException {
        double currentPay = getCEOPay();
        Map<String, Object> payMap = new HashMap<>();
        payMap.put("pay", currentPay + amount);
        salaryStorageHelper.getStore(SALARY_NAME).save(CEO_PAY_KEY, payMap);
    }

    public Booking bookRoom(Room room, int numNights, Customer customer) throws IOException {
        if(numNights <= 0 || room == null) return null;
        LocalDate checkoutDate = LocalDate.now().plusDays(numNights);
        int bookingId = getNumOfBookings() + 1;

        Booking booking = new Booking(bookingId, LocalDate.now().toString(),
                checkoutDate.toString(), room.getPricePerNight() * numNights, "Complete",
                room.getRoomNumber());

        createBooking(booking);
        return booking;
    }

    public Booking extendStay(Booking booking, Room room, int numNights, Customer customer) throws IOException {
        if(numNights <= 0 || booking == null) return null;
        LocalDate checkoutDate = LocalDate.parse(booking.getCheckOutDate()).plusDays(numNights);
        booking.setCheckOutDate(checkoutDate.toString());
        booking.setTotalPrice(booking.getTotalPrice() + (room.getPricePerNight() * numNights));
        updateBooking(booking);
        return booking;
    }

    public boolean isEarlyCheckout(Booking booking) {
        return LocalDate.parse(booking.getCheckOutDate()).isAfter(LocalDate.now());
    }

    public void checkOut(Booking booking) throws IOException {
        deleteBooking(booking.getId());
    }
}