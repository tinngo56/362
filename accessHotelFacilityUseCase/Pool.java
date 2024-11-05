package accessHotelFacilityUseCase;

import util.HotelUtils;
import util.Time;
import util.TimeRange;
import util.HotelUtils.CheckInStatus;
import util.HotelUtils.RestrictionLevel;

public class Pool extends Facility {
    private boolean isUnderMaintenance;

    public Pool(TimeRange availableHours) {
        super("Pool", availableHours);
        this.isUnderMaintenance = false;
    }

    public void setUnderMaintenance(boolean isUnderMaintenance) {
        this.isUnderMaintenance = isUnderMaintenance;
    }

    @Override
    protected int getLockIndex() {
        return HotelUtils.POOL_LOCK;
    }

    @Override
    public boolean isAccessible(Time time, CheckInStatus status, RestrictionLevel level) {
        if (level == RestrictionLevel.EMPLOYEE) {
            return true;
        }
        return !isUnderMaintenance
                && !isAdminLocked()
                && isAvailableAt(time)
                && (level == RestrictionLevel.REGULAR_CUSTOMER || level == RestrictionLevel.EXECUTIVE_CUSTOMER)
                && status == CheckInStatus.CHECKED_IN;
    }
}