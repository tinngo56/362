package Controllers;

import Models.*;

public class FacilityController {

    public boolean checkAccessLevel(Facility facility, KeyCard keyCard, Booking booking) {
        if(facility == null) return false;

        if(facility.getAccessLevel() == AccessLevels.ROOM && ((Room) facility).getRoomNumber() == booking.getRoomNum()) {
            return true;
        } else {
            return facility.getAccessLevel() == keyCard.getAccessLevel();
        }
    }

}
