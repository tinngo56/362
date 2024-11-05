package util;

/**
 * Represents a range of time with a start and end time.
 */
public class TimeRange {
    private Time startTime;
    private Time endTime;

    /**
     * makes a TimeRange
     * @param startTime start time of range
     * @param endTime end time of range
     */
    public TimeRange(Time startTime, Time endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    /**
     * Checks if a given time is within the range (inclusive)
     * @param time time to check
     * @return True if the time is within the range, false otherwise
     */
    public boolean isWithinRange(Time time) {
        return time.isAfterOrEqual(startTime) && time.isBeforeOrEqual(endTime);
    }
}