package Models.Excursions;

import Models.Mappable;

public class ExcursionBooking extends Mappable<ExcursionBooking> {

    private int id;
    private String date;
    private String excursionName;
    private int bookingSize;

    public ExcursionBooking() {}

    public ExcursionBooking(int id, String date, String excursionName, int bookingSize) {
        this.id = id;
        this.date = date;
        this.excursionName = excursionName;
        this.bookingSize = bookingSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExcursionName() {
        return excursionName;
    }

    public int getBookingSize() {
        return bookingSize;
    }

    public void setBookingSize(int bookingSize) {
        this.bookingSize = bookingSize;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Date: " + date + ", Excursion: " + excursionName + ", Size: " + bookingSize;
    }
}
