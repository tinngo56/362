package maintainPool;

import maintainPool.Maintenance;
import util.CardScan;
import util.HotelUtils.RestrictionLevel;

public abstract class FacilityMaintenance implements Maintenance {

    protected boolean accessGranted;

    public FacilityMaintenance() {
        this.accessGranted = false;
    }

    @Override
    public void exitFacility() {
        System.out.println("Maintenance staff exits the facility");
        logMaintenanceActivity();
    }

    @Override
    public void logMaintenanceActivity() {
        System.out.println("Logging completed maintenance activities for the facility");
    }

    public abstract void inspectEquipment();

    public abstract void checkConditionLevels();

    public abstract void cleanFacility();

    public abstract void checkSupplyLevels();
}
