package Controllers;

import Models.CleanFacility.*;
import Storage.StorageHelper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CleanFacilityController {

//general
    private StorageHelper storageHelper;
    private StorageHelper inventoryStorageHelper;
    private final String FACILITY_CLEANING_REPORT_STORE_NAME = "facilityCleaningReports";
    private final String INVENTORY_STORE_NAME = "inventory";
    private final String FACILITY_CLEANING_SUPPLIES_KEY = "facilityCleaningSupplies";
    private final String DAYS_BETWEEN_CLEANING_KEY = "dysBetweenCleaning";
    private final String POOL = "pool";
    private final String GAME_ROOM = "gameRoom";
    private final String CONFERENCE_ROOM = "conferenceRoom";
    private final String GYM = "gym";
    /**
     * use on serfaces
     */
    private final String SANITIZER = "sanitiser liters";
//pool
    /**
     * use when mopping deck
     */
    private final String CLEANSER = "chlorineCleaner liters";
    /**
     * use when clean towls
     */
    private final String DETERGENT = "detergent grams";
//GameRoom
//    private final String CLEANSER = "chlorineCleaner";
//    private final String DETERGENT = "detergent";
    private int nextId;

    public CleanFacilityController(String baseDirectory) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, FACILITY_CLEANING_REPORT_STORE_NAME);
        this.inventoryStorageHelper = new StorageHelper(baseDirectory, INVENTORY_STORE_NAME);
        this.nextId = determineNextId();
        initializeFacilityCleaningInventory();
        initializeDaysBetweenCleaning();
    }

//---------------------------store managing stuff--------------------------- TODO only to find section

    private int determineNextId() throws IOException {
        List<Map<String, Object>> allReadings = storageHelper.getStore(FACILITY_CLEANING_REPORT_STORE_NAME).loadAll();
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

    private String getUnits(String item){
        //TODO
        return null;
    }

    private void initializeDaysBetweenCleaning() throws IOException {
        StorageHelper.DataStore<?> store = storageHelper.getStore(FACILITY_CLEANING_REPORT_STORE_NAME);
        Map<String, Object> inventoryData = store.load(DAYS_BETWEEN_CLEANING_KEY);
        if (inventoryData == null || !(inventoryData instanceof Map)) {
            Map<String, Object> initialIntervals = new HashMap<>();
            initialIntervals.put(POOL, 5);
            initialIntervals.put(GAME_ROOM, 7);
            initialIntervals.put(CONFERENCE_ROOM, 4);
            initialIntervals.put(GYM, 7);

            store.save(DAYS_BETWEEN_CLEANING_KEY, initialIntervals);
            System.out.println("Initialized initial default intervals for facility cleaning.");
        }
    }

    /**
     *
     * @param facility wanted POOL GAME_ROOM CONFERENCE_ROOM or GYM
     * @return days between cleaning
     * @throws IOException
     */
    private int getDaysBetweenCleaning(String facility) throws IOException {
        return ((Number) storageHelper.getStore(FACILITY_CLEANING_REPORT_STORE_NAME).load(DAYS_BETWEEN_CLEANING_KEY).get(facility)).intValue();
    }

    private void setDaysBetweenCleaning(String facility, int days) throws IOException {
        Map<String, Object> map = storageHelper.getStore(FACILITY_CLEANING_REPORT_STORE_NAME).load(DAYS_BETWEEN_CLEANING_KEY);
        map.replace(facility, days);
        storageHelper.getStore(FACILITY_CLEANING_REPORT_STORE_NAME).save(DAYS_BETWEEN_CLEANING_KEY, map);
    }

    private void initializeFacilityCleaningInventory() throws IOException {
        StorageHelper.DataStore<?> store = inventoryStorageHelper.getStore(INVENTORY_STORE_NAME);
        Map<String, Object> inventoryData = store.load(FACILITY_CLEANING_SUPPLIES_KEY);
        if (inventoryData == null || !(inventoryData instanceof Map)) {
            Map<String, Object> initialInventory = new HashMap<>();
            initialInventory.put(SANITIZER, 5.0);
            initialInventory.put(CLEANSER, 5.0);
            initialInventory.put(DETERGENT, 50.0);
            store.save(FACILITY_CLEANING_SUPPLIES_KEY, initialInventory);
            System.out.println("Initialized facility cleaning inventory with default stock.");
        }
    }

    public void updateCleaningInventory(Scanner scanner) throws IOException {
        System.out.println("Chose what you want to update:");
        System.out.println("1: Sanitzer liters");
        System.out.println("2: Cleanser liters");
        System.out.println("3: Detergent powder grams");
        int update = scanner.nextInt();
        System.out.println("How much?");
        double amt = scanner.nextDouble();
        Map map = getCleaningInventory();
        if(update == 1) {
            map.replace(SANITIZER, amt);
        } else if (update == 2) {
            map.replace(CLEANSER, amt);
        } else if (update == 3) {
            map.replace(DETERGENT, amt);
        } else {
            System.out.println("Invalid entry\n");
        }
    }

    private void saveCleaningInventory(Map<String, Object> map) throws IOException {
        inventoryStorageHelper.getStore(INVENTORY_STORE_NAME).save(FACILITY_CLEANING_SUPPLIES_KEY, map);
    }

    public void saveFacilityReport(HotelFacility facility) throws IOException {
        Map<String, Object> readingMap = convertHotelFacilityToMap(facility);
        storageHelper.getStore(FACILITY_CLEANING_REPORT_STORE_NAME).save(String.valueOf(facility.getId()), readingMap);
    }

    public HotelFacility getReading(int id) throws IOException {
        Map<String, Object> data = storageHelper.getStore(FACILITY_CLEANING_REPORT_STORE_NAME).load(String.valueOf(id));
        return data != null ? convertMapToHotelFacility(data) : null;
    }

    public void updateReading(HotelFacility reading) throws IOException {
        Map<String, Object> readingMap = convertHotelFacilityToMap(reading);
        storageHelper.getStore(FACILITY_CLEANING_REPORT_STORE_NAME).save(String.valueOf(reading.getId()), readingMap);
    }

    public int getNumOfReadings() throws IOException {
        List<Map<String, Object>> allReadings = storageHelper.getStore(FACILITY_CLEANING_REPORT_STORE_NAME).loadAll();
        return allReadings.size();
    }

    public Map<String, Object> getCleaningInventory() throws IOException {
        Map<String, Object> inventoryData = inventoryStorageHelper.getStore(INVENTORY_STORE_NAME).load(FACILITY_CLEANING_SUPPLIES_KEY);
        if (inventoryData instanceof Map) {
            return (Map<String, Object>) inventoryData;
        } else {
            System.out.println("Expected inventory data to be a Map but found: " +
                    (inventoryData != null ? inventoryData.getClass().getName() : "null"));
            return null;
        }
    }

    private Map<String, Object> getDaysBetweenCleaning() throws IOException {
        Map<String, Object> inventoryData = storageHelper.getStore(FACILITY_CLEANING_REPORT_STORE_NAME).load(DAYS_BETWEEN_CLEANING_KEY);
        if (inventoryData instanceof Map) {
            return (Map<String, Object>) inventoryData;
        } else {
            System.out.println("Expected days between cleaning data to be a Map but found: " +
                    (inventoryData != null ? inventoryData.getClass().getName() : "null"));
            return null;
        }
    }

    private Map<String, Object> convertHotelFacilityToMap(HotelFacility reading) {
        return reading.toMap();
//        Map<String, Object> map = new HashMap<>();
//        map.put("id", reading.getId());
//        map.put("date", reading.getMaintainedDate());
//        map.put("phLevel", reading.getphLevel());
//        map.put("alkalinity", reading.getAlkalinity());
//        map.put("chlorine", reading.getChlorine());
//        return map;
    }

    public HotelFacility convertMapToHotelFacility(Map<String, Object> map) {
        HotelFacility facility = null;
        facility = (HotelFacility) facility.fromMap(map);
        return facility;
//        Number idNumber = (Number) map.get("id");
//        int id = idNumber != null ? idNumber.intValue() : 0;
//
//        String date = (String) map.get("date");
//
//        Number phNumber = (Number) map.get("phLevel");
//        double ph = phNumber != null ? phNumber.doubleValue() : 0.0;
//        Number alkalinityNumber = (Number) map.get("alkalinity");
//        double alkalinity = alkalinityNumber != null ? alkalinityNumber.doubleValue() : 0.0;
//        Number chlorineNumber = (Number) map.get("chlorine");
//        double chlorine = chlorineNumber != null ? chlorineNumber.doubleValue() : 0.0;
//
//        return new ChemicalReading(id, date, ph, alkalinity, chlorine);
    }

    public List<Map<String, Object>> getAllFacilityCleaningReports() throws IOException {
        return storageHelper.getStore(FACILITY_CLEANING_REPORT_STORE_NAME).loadAll();
    }

//---------------------------interactive print stuff--------------------------- TODO only to find section

    public void completeFacilityCleaning(Scanner scanner) throws IOException {
        System.out.println("=== Complete New Cleaning Save ===");
        System.out.println("Enter the following details for a new facility cleaning reading:");

        int id = nextId++;
        String facilityType = null;
        HotelFacility facility = null;
        while(facilityType == null) {
            System.out.println("Select a facility to report cleaning (enter the number):");
            System.out.println("1. Pool");
            System.out.println("2. Game Room");
            System.out.println("3. Conference Room");
            System.out.println("4. Gym");

            int facilityChoice = scanner.nextInt();
            scanner.nextLine();

            switch (facilityChoice) {
                case 1:
                    facility = new Pool(id);
                    facilityType = POOL;
                    break;
                case 2:
                    facility = new GameRoom(id);
                    facilityType = GAME_ROOM;
                    break;

                case 3:
                    facility = new ConferenceRoom(id);
                    facilityType = CONFERENCE_ROOM;
                    break;

                case 4:
                    facility = new Gym(id);
                    facilityType = GYM;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        int days = getDaysBetweenCleaning(facilityType);
        facility.createCleaningReport(scanner, days);

        saveFacilityReport(facility);
        System.out.println("Facility cleaning complete. New cleaning report saved.\n");
    }

    public void changeDaysBetweenCleaning(Scanner scanner) throws IOException {
        String facilityType = null;
        while(facilityType == null) {
            System.out.println("Select a facility to report cleaning (enter the number):");
            System.out.println("1. Pool");
            System.out.println("2. Game Room");
            System.out.println("3. Conference Room");
            System.out.println("4. Gym");

            int facilityChoice = scanner.nextInt();
            scanner.nextLine();

            switch (facilityChoice) {
                case 1:
                    facilityType = POOL;
                    break;
                case 2:
                    facilityType = GAME_ROOM;
                    break;
                case 3:
                    facilityType = CONFERENCE_ROOM;
                    break;

                case 4:
                    facilityType = GYM;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        System.out.println("New number of days between cleanings?");
        int days = scanner.nextInt();
        setDaysBetweenCleaning(facilityType,days);
    }
    //add remove/check for cleaning things

    public void printChemicalInventory() throws IOException { //TODO
        Map<String, Object> inventory = getCleaningInventory();

        if (inventory == null || inventory.isEmpty()) {
            System.out.println("No chemical inventory available.");
            return;
        }

        System.out.println("Current Chemical Inventory:");
        System.out.println("----------------------------");

        for (Map.Entry<String, Object> entry : inventory.entrySet()) {
            String chemical = entry.getKey();
            if (entry.getKey().equals("lastModified")) {
                continue;
            }
            String unit = "kg";
            if (entry.getKey().equals("poolChlorine")) {
                unit = "liters";
            }
            double amount = ((Number) entry.getValue()).doubleValue();
            System.out.printf("%s: %.2f %s%n", capitalize(chemical), amount, unit);
        }
        System.out.println("----------------------------");
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public void printAll() throws IOException {
        List<Map<String, Object>> allReadings = getAllFacilityCleaningReports();

        if (allReadings.isEmpty()) {
            System.out.println("No cleaning reports available.");
            return;
        }

        System.out.println("All Chemical Readings:");
        System.out.println("----------------------");

        for (Map<String, Object> readingMap : allReadings) {
            for (Map.Entry<String, Object> entry : readingMap.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("----------------------");
        }
    }

    public void addToCleaningInventory(Scanner scnr) throws IOException {
        System.out.println("=== Add to Cleaning Inventory ===");
        Map<String, Object> inventory =  getCleaningInventory();

        if (inventory == null || inventory.isEmpty()) {
            System.out.println("Cleaning inventory is empty or not initialized.");
            return;
        }

        String chemical = selectCleaningSupply(scnr, inventory.keySet());
        if (chemical == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        System.out.print("Enter amount to add: ");
        double amountToAdd = scnr.nextDouble();
        scnr.nextLine();

        if (amountToAdd <= 0) {
            System.out.println("Amount to add must be positive.");
            return;
        }

        double currentAmount = ((Number) inventory.get(chemical)).doubleValue();
        double newAmount = currentAmount + amountToAdd;
        inventory.put(chemical, newAmount);

        // Update the inventory in storage
        inventoryStorageHelper.getStore(INVENTORY_STORE_NAME).save(FACILITY_CLEANING_SUPPLIES_KEY, inventory);


        System.out.printf("Successfully added %.2f %s to %s.%n", amountToAdd, getChemicalUnit(chemical), capitalize(chemical));
        System.out.printf("New %s Stock: %.2f %s%n", capitalize(chemical), newAmount, getChemicalUnit(chemical));
    }

    public void subtractFromCleaningInventory(Scanner scnr) throws IOException {
        System.out.println("=== Subtract from Cleaning Inventory ===");
        Map<String, Object> inventory = getCleaningInventory();

        if (inventory == null || inventory.isEmpty()) {
            System.out.println("Cleaning inventory is empty or not initialized.");
            return;
        }

        String chemical = selectCleaningSupply(scnr, inventory.keySet());
        if (chemical == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        System.out.print("Enter amount to subtract: ");
        double amountToSubtract = scnr.nextDouble();
        scnr.nextLine();

        if (amountToSubtract <= 0) {
            System.out.println("Amount to subtract must be positive.");
            return;
        }

        double currentAmount = ((Number) inventory.get(chemical)).doubleValue();
        if (amountToSubtract > currentAmount) {
            System.out.println("Insufficient stock. Operation aborted.");
            return;
        }

        double newAmount = currentAmount - amountToSubtract;
        inventory.put(chemical, newAmount);

        // Update the inventory in storage
        inventoryStorageHelper.getStore(INVENTORY_STORE_NAME).save(FACILITY_CLEANING_SUPPLIES_KEY, inventory);

        System.out.printf("Successfully subtracted %.2f %s from %s.%n", amountToSubtract, getChemicalUnit(chemical), capitalize(chemical));
        System.out.printf("New %s Stock: %.2f %s%n", capitalize(chemical), newAmount, getChemicalUnit(chemical));
    }

    public void setCleaningInventory(Scanner scnr) throws IOException {
        System.out.println("=== Set Cleaning Inventory ===");
        Map<String, Object> inventory = getCleaningInventory();

        if (inventory == null || inventory.isEmpty()) {
            System.out.println("Cleaning inventory is empty or not initialized.");
            return;
        }

        String chemical = selectCleaningSupply(scnr, inventory.keySet());
        if (chemical == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        System.out.printf("Current amount of %s: %.2f %s%n", capitalize(chemical), ((Number) inventory.get(chemical)).doubleValue(), getChemicalUnit(chemical));
        System.out.print("Enter new amount: ");
        double newAmount = scnr.nextDouble();
        scnr.nextLine(); // Consume newline

        if (newAmount < 0) {
            System.out.println("Amount cannot be negative.");
            return;
        }

        inventory.put(chemical, newAmount);

        // Update the inventory in storage
        inventoryStorageHelper.getStore(INVENTORY_STORE_NAME).save(FACILITY_CLEANING_SUPPLIES_KEY, inventory);

        System.out.printf("Successfully set %s to %.2f %s.%n", capitalize(chemical), newAmount, getChemicalUnit(chemical));
    }

    private String selectCleaningSupply(Scanner scnr, Set<String> chemicals) {
        List<String> chemicalList = new ArrayList<>(chemicals);
        System.out.println("Available Cleaning Supplies:");
        for (int i = 0; i < chemicalList.size(); i++) {
            if (chemicalList.get(i).equals("lastModified")) {
                continue;
            }
            System.out.printf("%d. %s%n", i + 1, capitalize(chemicalList.get(i)));
        }
        System.out.println("0. Cancel");
        System.out.print("Select a cleaning supply by number: ");

        int selection;
        try {
            selection = scnr.nextInt();
            scnr.nextLine();
        } catch (InputMismatchException e) {
            scnr.nextLine();
            System.out.println("Invalid input. Operation cancelled.");
            return null;
        }

        if (selection == 0) {
            return null;
        }

        if (selection < 1 || selection > chemicalList.size()) {
            System.out.println("Invalid selection. Operation cancelled.");
            return null;
        }

        return chemicalList.get(selection - 1);
    }

//    public void setDaysBetweenCleaning(Scanner scnr) throws IOException {
//        System.out.println("=== Set Days Between Cleaning ===");
//        Map<String, Object> daysBetweenCleaningMap = getDaysBetweenCleaning();
//
//        if (daysBetweenCleaningMap == null || daysBetweenCleaningMap.isEmpty()) {
//            System.out.println("Days Between Cleaning is empty or not initialized.");
//            return;
//        }
//
//        String facility = selectFacility(scnr, daysBetweenCleaningMap.keySet());
//        if (facility == null) {
//            System.out.println("Operation cancelled.");
//            return;
//        }
//
//        System.out.printf("Current days between cleaning of %s: %.2f %s%n", capitalize(facility), ((Number) daysBetweenCleaningMap.get(facility)).doubleValue(), getChemicalUnit(facility));
//        System.out.print("Enter new amount: ");
//        int newAmount = scnr.nextInt();
//        scnr.nextLine();
//
//        if (newAmount < 0) {
//            System.out.println("Amount cannot be negative.");
//            return;
//        }
//
//        daysBetweenCleaningMap.put(facility, newAmount);
//
//        // Update in storage
//        inventoryStorageHelper.getStore(FACILITY_CLEANING_REPORT_STORE_NAME).save(DAYS_BETWEEN_CLEANING_KEY, daysBetweenCleaningMap);
//
//        System.out.printf("Successfully set " + capitalize(facility) + " to " + newAmount);
//    }

    private String selectFacility(Scanner scnr, Set<String> chemicals) {
        List<String> chemicalList = new ArrayList<>(chemicals);
        System.out.println("Available Facilities:");
        for (int i = 0; i < chemicalList.size(); i++) {
            if (chemicalList.get(i).equals("lastModified")) {
                continue;
            }
            System.out.printf("%d. %s%n", i + 1, capitalize(chemicalList.get(i)));
        }
        System.out.println("0. Cancel");
        System.out.print("Select a chemical by number: ");

        int selection;
        try {
            selection = scnr.nextInt();
            scnr.nextLine();
        } catch (InputMismatchException e) {
            scnr.nextLine();
            System.out.println("Invalid input. Operation cancelled.");
            return null;
        }

        if (selection == 0) {
            return null;
        }

        if (selection < 1 || selection > chemicalList.size()) {
            System.out.println("Invalid selection. Operation cancelled.");
            return null;
        }

        return chemicalList.get(selection - 1);
    }

    private String getChemicalUnit(String chemical) {
        switch (chemical.toLowerCase()) {
            case "bakingsoda":
                return "kg";
            case "sodaash":
                return "kg";
            case "poolchlorine":
                return "liters";
            default:
                return "units";
        }
    }

    private boolean nextBoolean(Scanner scanner) {
        while (true) {
            System.out.print("Enter 'y' for yes or 'n' for no: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'y' or 'n'.");
            }
        }
    }
}
