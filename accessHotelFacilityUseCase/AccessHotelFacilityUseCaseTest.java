package accessHotelFacilityUseCase;

import util.CardScan;
import util.HotelUtils;
import util.Time;
import util.TimeRange;

public class AccessHotelFacilityUseCaseTest {

    public static void main(String[] args) {
        FacilityAccess facilityAccess = new FacilityAccess();

        facilityAccess.simpleIntialize();

        // Main Success Scenario
        System.out.println("Main Success Scenario: Customer Accessing Gym");
        CardScan customerScan = new CardScan("Gym", new Time(10, 0), HotelUtils.CheckInStatus.CHECKED_IN, HotelUtils.RestrictionLevel.REGULAR_CUSTOMER);
        facilityAccess.requestAccess(customerScan);

        // Alternate Flow 3a: Key card invalid due to check-in status
        System.out.println("\nScenario 3a: Invalid Check-in Status");
        CardScan invalidStatusScan = new CardScan("Pool", new Time(10, 0), HotelUtils.CheckInStatus.CHECKED_OUT, HotelUtils.RestrictionLevel.REGULAR_CUSTOMER);
        facilityAccess.requestAccess(invalidStatusScan);

        // Alternate Flow 3b: Key card invalid due to time
        System.out.println("\nScenario 3b: Invalid Time of Access");
        CardScan invalidTimeScan = new CardScan("Gym", new Time(23, 0), HotelUtils.CheckInStatus.CHECKED_IN, HotelUtils.RestrictionLevel.REGULAR_CUSTOMER);
        facilityAccess.requestAccess(invalidTimeScan);

        // Alternate Flow 3c: Key card invalid due to admin lock
        System.out.println("\nScenario 3c: Admin Facility Lock");
        CardScan managerScan = new CardScan("Gym", new Time(10, 0), HotelUtils.CheckInStatus.CHECKED_IN, HotelUtils.RestrictionLevel.EMPLOYEE);
        CardScan regularCustomerScan = new CardScan("Gym", new Time(10, 0), HotelUtils.CheckInStatus.CHECKED_IN, HotelUtils.RestrictionLevel.REGULAR_CUSTOMER);

        facilityAccess.requestAccess(regularCustomerScan);
        facilityAccess.restrictAccess(managerScan);
        facilityAccess.requestAccess(regularCustomerScan);
        facilityAccess.unlockAccess(managerScan);
        facilityAccess.requestAccess(regularCustomerScan);


        // Alternate Flow 3d: Restricted access due to access level
        System.out.println("\nScenario 3d: Restricted Access");
        CardScan restrictedAccessScan = new CardScan("ConferenceRoom", new Time(10, 0), HotelUtils.CheckInStatus.CHECKED_IN, HotelUtils.RestrictionLevel.REGULAR_CUSTOMER);
        facilityAccess.requestAccess(restrictedAccessScan);
    }
}

