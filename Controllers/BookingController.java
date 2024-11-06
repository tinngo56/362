package Controllers;

import java.io.IOException;

import Models.Booking;
import java.util.Map;
import storage.StorageHelper;

public class BookingController {
    private StorageHelper storageHelper;
    private final String STORE_NAME = "bookings";

    public BookingController(String baseDirectory) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, STORE_NAME);
    }

    public void createBooking(Booking booking) throws IOException {
        storageHelper.getStore(STORE_NAME).save(String.valueOf(booking.getId()), booking.toMap());
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