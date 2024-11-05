/**
 * Utility class for managing hotel-related constants and configurations.
 */
package util;

public class HotelUtils {

    /**
     * status of various hotel facilities
     * false indicates unlocked, true indicates locked
     */
    public static boolean[] adminLock = {false, false, false, false};

    public static int POOL_LOCK = 0;

    public static int GAME_ROOM_LOCK = 1;

    public static int CONFERENCE_ROOM_LOCK = 2;

    public static int GYM_ROOM_LOCK = 3;

    /**
     * Enum representing the check-in status of a hotel guest.
     */
    public enum CheckInStatus {
        /** Guest is currently checked in. */
        CHECKED_IN,
        /** Guest has checked out. */
        CHECKED_OUT,
        /** Room is reserved for a guest (NOT SURE IF IT SHOULD BE INCLUDED) */
        RESERVED,
        /** Reservation has been cancelled (NOT SURE IF IT SHOULD BE INCLUDED) */
        CANCELLED,
        /** Status for employees */
        EMPLOYEE,
        UNKNOWN
    }

    /**
     * Enum representing the access restriction level for hotel guests and employees
     */
    public enum RestrictionLevel {
        /** Access level for executive customers */
        EXECUTIVE_CUSTOMER,
        /** Access level for regular customers */
        REGULAR_CUSTOMER,
        /** Access level for employees */
        EMPLOYEE,
        UNKNOWN
    }
}