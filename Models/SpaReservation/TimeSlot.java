package Models.SpaReservation;

public class TimeSlot {
    private DayOfWeek day;
    private int slot; // 0-6 corresponding to the time slots

    public TimeSlot(DayOfWeek day, int slot) {
        this.day = day;
        this.slot = slot;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}