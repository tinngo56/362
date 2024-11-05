package Controllers;

import java.io.IOException;

import Models.Booking;
import java.util.Map;
import storage.StorageHelper;

public class BookingController {
    private StorageHelper storageHelper;

    public BookingController(String baseDirectory) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, "bookings");
    }

    public void createBooking(Booking booking) throws IOException {
        storageHelper.getStore("bookings").save(String.valueOf(booking.getId()), booking.toMap());
    }

    public Booking getBooking(int id) throws IOException {
        Map<String, Object> data = storageHelper.getStore("bookings").load(String.valueOf(id));
        return data != null ? Booking.fromMap(data) : null;
    }

    public void updateBooking(Booking booking) throws IOException {
        storageHelper.getStore("bookings").save(String.valueOf(booking.getId()), booking.toMap());
    }

    public void deleteBooking(int id) throws IOException {
        storageHelper.getStore("bookings").delete(String.valueOf(id));
    }
}