package accessHotelFacilityUseCase;

import util.CardScan;
import util.HotelUtils;
import util.Time;
import util.TimeRange;
import java.util.HashMap;
import java.util.Map;

public class FacilityAccess implements AccessControl {
    private Map<String, Facility> facilities;

    public FacilityAccess() {
        this.facilities = new HashMap<>();
    }

    public void simpleIntialize(){
        facilities.put("Pool", new Pool(new TimeRange(new Time(8, 0), new Time(20, 0))));
        facilities.put("GameRoom", new GameRoom(new TimeRange(new Time(8, 0), new Time(22, 0))));
        facilities.put("ConferenceRoom", new ConferenceRoom(new TimeRange(new Time(9, 0), new Time(17, 0))));
        facilities.put("Gym", new Gym(new TimeRange(new Time(5, 30), new Time(22, 0))));
    }
    public Map<String, Facility> getFacilities() {
        return facilities;
    }

    private Facility getFacility(String facilityName) {
        return facilities.get(facilityName);
    }

    @Override
    public boolean requestAccess(CardScan scan) {
        if (!scan.validScan()) {
            System.out.println("Access denied: Error scanning card.");
            return false;
        }

        Facility facility = getFacility(scan.getFacility());
        if (facility == null) {
            System.out.println("Access denied: Unknown facility.");
            return false;
        }

        // isAccessible check all (time, check-in status, restriction level)
        if (!facility.isAccessible(scan.getTime(), scan.getStatus(), scan.getLevel())) {
            System.out.println("Access denied: Facility is restricted based on access rules.");
            return false;
        }

        System.out.println("Access granted.");
        return true;
    }

    @Override
    public boolean restrictAccess(CardScan scan) {
        if (!scan.validScan()) {
            System.out.println("Access denied: Error scanning card.");
            return false;
        }

        if (scan.getLevel() != HotelUtils.RestrictionLevel.EMPLOYEE) {
            System.out.println("Access denied: Only employees can restrict access.");
            return false;
        }

        Facility facility = getFacility(scan.getFacility());
        if (facility == null) {
            System.out.println("Access denied: Unknown facility.");
            return false;
        }

        int lockIndex = facility.getLockIndex();
        HotelUtils.adminLock[lockIndex] = true;  // Set facility to locked

        System.out.println(facility.getName() + " is now restricted to employees only.");
        return true;
    }

    @Override
    public boolean unlockAccess(CardScan scan) {
        if (!scan.validScan()) {
            System.out.println("Access denied: Error scanning card.");
            return false;
        }

        if (scan.getLevel() != HotelUtils.RestrictionLevel.EMPLOYEE) {
            System.out.println("Access denied: Only employees can unlock access.");
            return false;
        }

        Facility facility = getFacility(scan.getFacility());
        if (facility == null) {
            System.out.println("Access denied: Unknown facility.");
            return false;
        }

        int lockIndex = facility.getLockIndex();
        HotelUtils.adminLock[lockIndex] = false;  // Unlock the facility

        System.out.println(facility.getName() + " is now accessible to all users.");
        return true;
    }
}
