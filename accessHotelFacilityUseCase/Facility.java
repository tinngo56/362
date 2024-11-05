package accessHotelFacilityUseCase;

import util.HotelUtils;
import util.Time;
import util.TimeRange;
import util.HotelUtils.CheckInStatus;
import util.HotelUtils.RestrictionLevel;

public abstract class Facility {
    protected String name;
    protected TimeRange availableHours;

    public Facility(String name, TimeRange availableHours) {
        this.name = name;
        this.availableHours = availableHours;
    }

    public String getName() {
        return name;
    }

    /**
     * Checks if the facility is available at a given time
     * @param time The current time
     * @return true if the facility is within available hours
     */
    public boolean isAvailableAt(Time time) {
        return availableHours.isWithinRange(time);
    }

    public boolean isAdminLocked() {
        return HotelUtils.adminLock[getLockIndex()];
    }

    protected abstract int getLockIndex();

    /**
     * Determines if the facility can be accessed given the time, check-in status, and restriction level
     * @param time Current time
     * @param status Guest's check-in status
     * @param level Guest's access level
     * @return true if access is granted; false otherwise
     */
    public abstract boolean isAccessible(Time time, CheckInStatus status, RestrictionLevel level);
}