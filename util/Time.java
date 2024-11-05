package util;

/**
 * Represents time in military format
 * @author bridgerb
 */
public class Time {
    private int hour;
    private int minute;

    /**
     * Constructs a Time object
     * @param hour The hour 24-hour (0-23)
     * @param minute The minute (0-59)
     */
    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Gets hours (0-23)
     * @return hour part of this time
     */
    public int getHour() {
        return hour;
    }

    /**
     * Gets minutes (0-59)
     * @return minute part of this time
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Compares this time to another time
     * @param other other Time to compare
     * @return True if this time is before or equal to the other time, false otherwise
     */
    public boolean isBeforeOrEqual(Time other) {
        return (hour < other.hour) || (hour == other.hour && minute <= other.minute);
    }

    /**
     * Compares this time to another time
     * @param other other Time to compare
     * @return True if this time is after or equal to the other time, false otherwise
     */
    public boolean isAfterOrEqual(Time other) {
        return (hour > other.hour) || (hour == other.hour && minute >= other.minute);
    }
}