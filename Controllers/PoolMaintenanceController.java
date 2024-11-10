package Controllers;


import Models.PoolMaintenance.PoolEquipmentInspection;
import Storage.StorageHelper;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class PoolMaintenanceController {

    private StorageHelper storageHelper;
    private final String POOL_EQUIPMENT_STORE_NAME = "poolEquipmentInspections"; // the store name
    private int nextId;

    public PoolMaintenanceController(String baseDirectory) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, POOL_EQUIPMENT_STORE_NAME);
        this.nextId = determineNextId();
    }

    private int determineNextId() throws IOException {
        List<Map<String, Object>> allReadings = storageHelper.getStore(POOL_EQUIPMENT_STORE_NAME).loadAll();
        int maxId = 0;
        for (Map<String, Object> readingMap : allReadings) {
            Number idNumber = (Number) readingMap.get("id");
            if (idNumber != null) {
                int id = idNumber.intValue();
                if (id > maxId) {
                    maxId = id;
                }
            }
        }
        return maxId + 1;
    }

    public PoolEquipmentInspection makePoolEquipmentInspectionFromInput(Scanner scanner) throws IOException {
        System.out.println("=== Create New Pool Equipment Inspection ===");

        int id = nextId++;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDate.now().format(formatter);

        printInspectionChecksPump();
        boolean pump = getBooleanInput(scanner, "Is the pump functioning properly? (yes/no): ");

        printInspectionChecksHeater();
        boolean heater = getBooleanInput(scanner, "Is the heater functioning properly? (yes/no): ");

        printInspectionChecksFilter();
        boolean filter = getBooleanInput(scanner, "Is the filter functioning properly? (yes/no): ");

        PoolEquipmentInspection inspection = new PoolEquipmentInspection(id, date.toString(), pump, filter, heater);

        return inspection;
    }

    private boolean getBooleanInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("yes") || input.equals("y")) {
                return true;
            } else if (input.equals("no") || input.equals("n")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }
    }

    private void printInspectionChecksPump() {
        System.out.println("\n=== Pump Inspection Checks ===");
        System.out.println("1. Check for good water flow:");
        System.out.println("   - Ensure there are no blockages in the pump basket.");
        System.out.println("   - Observe for consistent water flow without fluctuations.");
        System.out.println("2. Listen for unusual noises:");
        System.out.println("   - Unusual sounds may indicate motor or bearing issues.");
        System.out.println("3. Inspect for leaks:");
        System.out.println("   - Look for any signs of water leakage around the pump.");
        System.out.println("4. Verify pressure readings:");
        System.out.println("   - Ensure pressure gauges are within normal operating ranges.");
    }

    private void printInspectionChecksFilter() {
        System.out.println("\n=== Filter Inspection Checks ===");
        System.out.println("1. Check for good flow rate:");
        System.out.println("   - Ensure the filter is not clogged and water flows smoothly.");
        System.out.println("2. Inspect filter media:");
        System.out.println("   - Look for dirty or worn-out filter media and clean or replace if necessary.");
        System.out.println("3. Examine seals and connections:");
        System.out.println("   - Ensure all seals are intact and there are no leaks.");
    }

    private void printInspectionChecksHeater() {
        System.out.println("\n=== Heater Inspection Checks ===");
        System.out.println("1. Check water temperature:");
        System.out.println("   - Ensure the heater is effectively maintaining the desired water temperature.");
        System.out.println("2. Inspect for gas or electrical leaks:");
        System.out.println("   - Look for any signs of leaks or faulty wiring.");
        System.out.println("3. Listen for operational sounds:");
        System.out.println("   - Unusual noises may indicate internal issues.");
    }

    public void createPoolEquipmentInspection(PoolEquipmentInspection reading) throws IOException {
        Map<String, Object> readingMap = convertReadingToMap(reading);
        storageHelper.getStore(POOL_EQUIPMENT_STORE_NAME).save(String.valueOf(reading.getId()), readingMap);
    }

    public PoolEquipmentInspection getReading(int id) throws IOException {
        Map<String, Object> data = storageHelper.getStore(POOL_EQUIPMENT_STORE_NAME).load(String.valueOf(id));
        return data != null ? convertMapToReading(data) : null;
    }

    public void updateReading(PoolEquipmentInspection reading) throws IOException {
        Map<String, Object> readingMap = convertReadingToMap(reading);
        storageHelper.getStore(POOL_EQUIPMENT_STORE_NAME).save(String.valueOf(reading.getId()), readingMap);
    }

    public void deleteReading(int id) throws IOException {
        storageHelper.getStore(POOL_EQUIPMENT_STORE_NAME).delete(String.valueOf(id));
    }

    public int getNumOfReadings() throws IOException {
        List<Map<String, Object>> allReadings = storageHelper.getStore(POOL_EQUIPMENT_STORE_NAME).loadAll();
        return allReadings.size();
    }

    public void printAll() throws IOException {
        List<Map<String, Object>> allReadings = storageHelper.getStore(POOL_EQUIPMENT_STORE_NAME).loadAll();
        if (allReadings.isEmpty()) {
            System.out.println("\nNo pool equipment inspections found.");
            return;
        }

        System.out.println("\n=== All Pool Equipment Inspections ===");
        for (Map<String, Object> readingMap : allReadings) {
            PoolEquipmentInspection inspection = convertMapToReading(readingMap);
            System.out.println("------------------------------------");
            System.out.println("ID: " + inspection.getId());
            System.out.println("Date: " + inspection.getMaintainedDate());
            System.out.println("Pump: " + (inspection.getPump() ? "OK" : "Not OK"));
            System.out.println("Heater: " + (inspection.getHeater() ? "OK" : "Not OK"));
            System.out.println("Filter: " + (inspection.getFilter() ? "OK" : "Not OK"));
        }
        System.out.println("------------------------------------");
    }

    private Map<String, Object> convertReadingToMap(PoolEquipmentInspection reading) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", reading.getId());
        map.put("date", reading.getMaintainedDate());
        map.put("pump", reading.getPump());
        map.put("heater", reading.getHeater());
        map.put("filter", reading.getFilter());
        return map;
    }

    private PoolEquipmentInspection convertMapToReading(Map<String, Object> map) {
        Number idNumber = (Number) map.get("id");
        int id = idNumber != null ? idNumber.intValue() : 0;
        String date = (String) map.get("date");

        boolean pump = (boolean) map.get("pump");
        boolean filter = (boolean) map.get("filter");
        boolean heater = (boolean) map.get("heater");

        return new PoolEquipmentInspection(id, date, pump, filter, heater);
    }
}
