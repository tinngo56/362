package Controllers;

import java.io.IOException;
import java.util.Map;
import Models.Booking;
import Models.FranchiseOwner;
import Models.CEO;
import storage.StorageHelper;

public class BookingController {
    private StorageHelper storageHelper;
    private final String STORE_NAME = "bookings";
    private final String FRANCHISE_OWNER_PAY_KEY = "franchiseOwnerPay";
    private final String CEO_PAY_KEY = "ceoPay";
    private FranchiseOwner franchiseOwner;
    private CEO ceo;

    public BookingController(String baseDirectory, FranchiseOwner franchiseOwner, CEO ceo) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, STORE_NAME);
        this.franchiseOwner = franchiseOwner;
        this.ceo = ceo;
    }

    public void createBooking(Booking booking) throws IOException {
        storageHelper.getStore(STORE_NAME).save(String.valueOf(booking.getId()), booking.toMap());
        double bookingAmount = booking.getTotalPrice();
        addPayToFranchiseOwner(bookingAmount);
        double ceoFee = bookingAmount * 0.05;
        addPayToCEO(ceoFee);
    }

    public Booking getBooking(int id) throws IOException {
        Map<String, Object> data = storageHelper.getStore(STORE_NAME).load(String.valueOf(id));
        return data != null ? new Booking().fromMap(data) : null;
    }

    public void updateBooking(Booking booking) throws IOException {
        storageHelper.getStore(STORE_NAME).save(String.valueOf(booking.getId()), booking.toMap());
    }

    public void deleteBooking(int id) throws IOException {
        storageHelper.getStore(STORE_NAME).delete(String.valueOf(id));
    }

    public int getNumOfBookings() throws IOException {
        return storageHelper.getStore(STORE_NAME).loadAll().size();
    }

    private void addPayToFranchiseOwner(double amount) throws IOException {
        double currentPay = getFranchiseOwnerPay();
        storageHelper.getStore(STORE_NAME).save(FRANCHISE_OWNER_PAY_KEY, currentPay + amount);
    }

    private double getFranchiseOwnerPay() throws IOException {
        Object pay = storageHelper.getStore(STORE_NAME).load(FRANCHISE_OWNER_PAY_KEY);
        return pay != null ? (double) pay : 0.0;
    }

    private void addPayToCEO(double amount) throws IOException {
        double currentPay = getCEOPay();
        storageHelper.getStore(STORE_NAME).save(CEO_PAY_KEY, currentPay + amount);
    }

    private double getCEOPay() throws IOException {
        Object pay = storageHelper.getStore(STORE_NAME).load(CEO_PAY_KEY);
        return pay != null ? (double) pay : 0.0;
    }
}