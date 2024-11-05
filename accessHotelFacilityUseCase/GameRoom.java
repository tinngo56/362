package accessHotelFacilityUseCase;

import util.HotelUtils;
import util.Time;
import util.TimeRange;
import util.HotelUtils.CheckInStatus;
import util.HotelUtils.RestrictionLevel;

public class GameRoom extends Facility {

    public GameRoom(TimeRange availableHours) {
        super("GameRoom", availableHours);
    }

    @Override
    protected int getLockIndex() {
        return HotelUtils.GAME_ROOM_LOCK;
    }

    @Override
    public boolean isAccessible(Time time, CheckInStatus status, RestrictionLevel level) {
        if (level == RestrictionLevel.EMPLOYEE) {
            return true;
        }
        return (!isAdminLocked()
                && isAvailableAt(time)
                && (level == RestrictionLevel.REGULAR_CUSTOMER
                || level == RestrictionLevel.EXECUTIVE_CUSTOMER))
                && status == CheckInStatus.CHECKED_IN;
    }
}