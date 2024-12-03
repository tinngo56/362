package Models.Rent;

import Models.Mappable;
import java.util.Map;

public class RentableRoom extends Mappable<RentableRoom> {
    private int id;
    private String name;
    private int capacity;
    private double ratePerPerson;
    private String bookedDates;  // String to store booked dates, separated by spaces

    public RentableRoom(int id, String name, int capacity, double ratePerPerson) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.ratePerPerson = ratePerPerson;
        this.bookedDates = "";  // Initialize as an empty string
    }

    public RentableRoom() {
        this.bookedDates = "";  // Initialize as an empty string
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getRatePerPerson() {
        return ratePerPerson;
    }

    public void setRatePerPerson(double ratePerPerson) {
        this.ratePerPerson = ratePerPerson;
    }

    public String getBookedDates() {
        return bookedDates;
    }

    public void setBookedDates(String bookedDates) {
        this.bookedDates = bookedDates;
    }

    // Check if the room is booked for a specific date
    public boolean isBookedOnDate(String date) {
        return bookedDates.contains(date);
    }

    // Add a new booking date
    public void addBookingDate(String date) {
        if (!bookedDates.isEmpty()) {
            bookedDates += " ";  // Add space if it's not the first date
        }
        bookedDates += date;  // Append the new booking date
    }

    // Remove a booking date
    public void removeBookingDate(String date) {
        // Remove the date and any spaces around it
        bookedDates = bookedDates.replace(date, "").trim();

        // Ensure no extra spaces remain (remove leading/trailing spaces)
        bookedDates = bookedDates.replaceAll(" +", " ");
    }

}
