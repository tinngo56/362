package accessHotelFacilityUseCase;

import util.HotelUtils;
import util.Time;
import util.TimeRange;
import util.HotelUtils.CheckInStatus;
import util.HotelUtils.RestrictionLevel;

public class ConferenceRoom extends Facility {
    private boolean isBooked;

    public ConferenceRoom(TimeRange availableHours) {
        super("ConferenceRoom", availableHours);
        this.isBooked = false;
    }

    public void setBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }

    @Override
    protected int getLockIndex() {
        return HotelUtils.CONFERENCE_ROOM_LOCK;
    }

    @Override
    public boolean isAccessible(Time time, CheckInStatus status, RestrictionLevel level) {
        if (level == RestrictionLevel.EMPLOYEE) {
            return true;
        }
        return (!isBooked
                && !isAdminLocked()
                && isAvailableAt(time)
                && level == RestrictionLevel.EXECUTIVE_CUSTOMER)
                && status == CheckInStatus.CHECKED_IN;
    }
}
