package Models.SpaReservation;

public class TimeSlot {

    private int day;
    private int slot;

    public TimeSlot(int day, int slot) {
        this.day = day;
        this.slot = slot;
    }

    public int getDay() {
        return day;
    }

    public int getSlot() {
        return slot;
    }

    @Override
    public String toString() {
        return "Day: " + (day + 1) + ", Slot: " + (slot + 1);
    }
}
