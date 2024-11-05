package util;

public class CardScan {
    private String facility;
    Time time;
    HotelUtils.CheckInStatus status;
    HotelUtils.RestrictionLevel level;


    public CardScan(String facility, Time time, HotelUtils.CheckInStatus status, HotelUtils.RestrictionLevel level) {
        this.facility = facility;
        this.time = time;
        this.status = status;
        this. level = level;
    }

    public String getFacility(){
        return facility;
    }

    public Time getTime() {
        return time;
    }

    public HotelUtils.CheckInStatus getStatus() {
        return status;
    }

    public HotelUtils.RestrictionLevel getLevel() {
        return level;
    }

    /**
     * checks if all information from the scan is there
     * @return true if all need information is not null
     */
    public boolean validScan(){
        if(facility != null && time != null && status != null && level != null){
            return true;
        } else {
            return false;
        }
    }
}