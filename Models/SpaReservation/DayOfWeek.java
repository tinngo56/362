package Models.SpaReservation;

public enum DayOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    public static DayOfWeek fromInt(int dayIndex) {
        if (dayIndex < 0 || dayIndex >= values().length) {
            throw new IllegalArgumentException("Invalid day index: " + dayIndex);
        }
        return values()[dayIndex];
    }

    @Override
    public String toString() {
        String name = super.toString();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }
}