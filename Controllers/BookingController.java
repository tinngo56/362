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
        franchiseOwner.addPay(bookingAmount);
        double ceoFee = bookingAmount * 0.05;
        ceo.addPay(ceoFee);
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
}