package Controllers;

import Models.Booking;
import Models.FranchiseOwner;
import Models.CEO;
import java.io.IOException;

public class BookingController {
    private StorageHelper storageHelper;
    private FranchiseOwner franchiseOwner;
    private CEO ceo;

    public BookingController(String baseDirectory, FranchiseOwner franchiseOwner, CEO ceo) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, "bookings");
        this.franchiseOwner = franchiseOwner;
        this.ceo = ceo;
    }

    public void createBooking(Booking booking) throws IOException {
        storageHelper.getStore("bookings").save(String.valueOf(booking.getId()), booking.toMap());
        double bookingAmount = booking.getTotalPrice();
        franchiseOwner.addPay(bookingAmount);
        double ceoFee = bookingAmount * 0.05;
        ceo.addPay(ceoFee);
    }

    public Booking getBooking(int id) throws IOException {
        Map<String, Object> data = storageHelper.getStore("bookings").load(String.valueOf(id));
        return data != null ? new Booking().fromMap(data) : null;
    }

    public void updateBooking(Booking booking) throws IOException {
        storageHelper.getStore("bookings").save(String.valueOf(booking.getId()), booking.toMap());
    }

    public void deleteBooking(int id) throws IOException {
        storageHelper.getStore("bookings").delete(String.valueOf(id));
    }
}