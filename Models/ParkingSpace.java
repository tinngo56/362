package Models;

public class ParkingSpace extends Mappable<ParkingSpace> {
    private String spaceId;
    private String status;
    private String vehicleLicense;
    private boolean isOverflow;

    public ParkingSpace() {} // Required for Mappable

    // Getters and Setters
    public String getSpaceId() { return spaceId; }
    public void setSpaceId(String spaceId) { this.spaceId = spaceId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getVehicleLicense() { return vehicleLicense; }
    public void setVehicleLicense(String vehicleLicense) { this.vehicleLicense = vehicleLicense; }
    public boolean isOverflow() { return isOverflow; }
    public void setOverflow(boolean overflow) { isOverflow = overflow; }
}
