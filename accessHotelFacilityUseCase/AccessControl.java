package accessHotelFacilityUseCase;

import util.CardScan;

public interface AccessControl {
    public boolean requestAccess(CardScan scan);
    public boolean restrictAccess(CardScan scan);
    boolean unlockAccess(CardScan scan);
}