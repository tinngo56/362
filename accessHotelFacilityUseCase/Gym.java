package accessHotelFacilityUseCase;

import util.HotelUtils;
import util.Time;
import util.TimeRange;
import util.HotelUtils.CheckInStatus;
import util.HotelUtils.RestrictionLevel;

public class Gym extends Facility {

    public Gym(TimeRange availableHours) {
        super("Gym", availableHours);
    }

    @Override
    protected int getLockIndex() {
        return HotelUtils.GYM_ROOM_LOCK;
    }

    @Override
    public boolean isAccessible(Time time, CheckInStatus status, RestrictionLevel level) {
        if (level == RestrictionLevel.EMPLOYEE) {
            return true;
        }

        return !isAdminLocked()
                && isAvailableAt(time)
                && (level == RestrictionLevel.REGULAR_CUSTOMER
                || level == RestrictionLevel.EXECUTIVE_CUSTOMER)
                && status == CheckInStatus.CHECKED_IN;
    }
}
