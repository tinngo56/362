package Controllers;

import java.io.IOException;

import Models.Booking;
import java.util.Map;
import Storage.BookingStorageHelper;

public class BookingController {
    private BookingStorageHelper bookingStorageHelper;

    public BookingController(String baseDirectory) throws IOException {
        this.bookingStorageHelper = new BookingStorageHelper(baseDirectory);
    }

    public void createBooking(Booking booking) throws IOException {
        bookingStorageHelper.getStore("bookings").save(String.valueOf(booking.getId()), booking.toMap());
    }

    public Booking getBooking(int id) throws IOException {
        Map<String, Object> data = bookingStorageHelper.getStore("bookings").load(String.valueOf(id));
        return data != null ? Booking.fromMap(data) : null;
    }

    public void updateBooking(Booking booking) throws IOException {
        bookingStorageHelper.getStore("bookings").save(String.valueOf(booking.getId()), booking.toMap());
    }

    public void deleteBooking(int id) throws IOException {
        bookingStorageHelper.getStore("bookings").delete(String.valueOf(id));
    }
}