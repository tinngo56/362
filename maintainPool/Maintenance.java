package maintainPool;

import util.CardScan;

public interface Maintenance {

    /**
     * Inspects equipment in the facility
     */
    void inspectEquipment();

    /**
     * Checks and records condition levels in the facility
     */
    void checkConditionLevels();

    /**
     * Cleans the facility according to maintenance protocols
     */
    void cleanFacility();

    /**
     * Checks supply levels to ensure they are above minimum thresholds
     */
    void checkSupplyLevels();

    /**
     * Logs maintenance activities after tasks are completed
     */
    void logMaintenanceActivity();

    /**
     * Exits the facility after completing maintenance tasks
     */
    void exitFacility();
}
