package Controllers;

import Models.CleanFacility.HotelFacility;
import Models.CleanFacility.Pool;
import Models.PoolMaintenance.ChemicalAdition;
import Models.PoolMaintenance.ChemicalReading;
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
    private final String SANITIZER = "sanitiser";
//pool
    /**
     * use when mopping deck
     */
    private final String CLEANSER = "chlorineCleaner";
    /**
     * use when clean towls
     */
    private final String DETERGENT = "detergent";
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

            store.save(FACILITY_CLEANING_REPORT_STORE_NAME, initialIntervals);
            System.out.println("Initialized initial default intervals for facility cleaning.");
        }
    }

    private void initializeFacilityCleaningInventory() throws IOException {
        StorageHelper.DataStore<?> store = inventoryStorageHelper.getStore(INVENTORY_STORE_NAME);
        Map<String, Object> inventoryData = store.load(FACILITY_CLEANING_SUPPLIES_KEY);
        if (inventoryData == null || !(inventoryData instanceof Map)) {
            Map<String, Object> initialInventory = new HashMap<>();
            initialInventory.put(SANITIZER, 5.0);
            store.save(FACILITY_CLEANING_SUPPLIES_KEY, initialInventory);
            System.out.println("Initialized facility cleaning inventory with default stock.");
        }
    }

    public void createChemicalReading(ChemicalReading reading) throws IOException {
        Map<String, Object> readingMap = convertHotelFacilityToMap(reading);
        storageHelper.getStore(FACILITY_CLEANING_REPORT_STORE_NAME).save(String.valueOf(reading.getId()), readingMap);
    }

    public HotelFacility getReading(int id) throws IOException {
        Map<String, Object> data = storageHelper.getStore(FACILITY_CLEANING_REPORT_STORE_NAME).load(String.valueOf(id));
        return data != null ? convertMapToHotelFacility(data) : null;
    }

    public void updateReading(ChemicalReading reading) throws IOException {
        Map<String, Object> readingMap = convertHotelFacilityToMap(reading);
        storageHelper.getStore(FACILITY_CLEANING_REPORT_STORE_NAME).save(String.valueOf(reading.getId()), readingMap);
    }

    public void deleteReading(int id) throws IOException {
        storageHelper.getStore(FACILITY_CLEANING_REPORT_STORE_NAME).delete(String.valueOf(id));
    }

    public int getNumOfReadings() throws IOException {
        List<Map<String, Object>> allReadings = storageHelper.getStore(FACILITY_CLEANING_REPORT_STORE_NAME).loadAll();
        return allReadings.size();
    }

    public double getSanatizerAmount() throws IOException {
        Map<String, Object> data = getChemicalInventory();
        return data != null ? ((Number) data.getOrDefault(SANITIZER, 0.0)).doubleValue() : 0.0;
    }

    private Map<String, Object> getChemicalInventory() throws IOException {
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

    private Map<String, Object> convertHotelFacilityToMap(ChemicalReading reading) {
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

    private HotelFacility makeHotelFacilityCleaningReportFromInput(Scanner scanner) throws IOException {
        System.out.println("Enter the following details for a new facility cleaning reading:");

        int id = nextId++;

        System.out.println("Select a facility to report cleaning (enter the number):");
        System.out.println("1. Pool");
        System.out.println("2. Game Room");
        System.out.println("3. Conference Room");
        System.out.println("4. Gym");

        int facilityChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (facilityChoice) {
            case 1:
                return new Pool(id);
            case 2:
                return new GameRoom(id);
            case 3:
                return new ConferenceRoom(id);
            case 4:
                return new Gym(id);
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    public ChemicalAdition[] completePoolChemicalReview(Scanner scanner) throws IOException {
        ChemicalReading newReading = makeChemicalReadingFromInput(scanner);
        System.out.println("=== Complete New Chemical Reading ===");

        double minPhLevel = 7.2;
        double minAlkalinity = 80.0;
        double minChlorine = 1.0;

        List<ChemicalAdition> additions = new ArrayList<>();

        // pH
        if (newReading.getphLevel() < minPhLevel) {
            System.out.println("pH level is low. Current pH: " + newReading.getphLevel());
            System.out.print("Would you like to add Soda Ash to increase pH? (y/n): ");
            String response = scanner.next();
            if (response.equalsIgnoreCase("y")) {
                printChemicalEffects();
                System.out.print("Enter gallons of water in pool: ");
                double gallonsOfWater = scanner.nextDouble();
                addSodaAsh(newReading, gallonsOfWater);
                additions.add(new ChemicalAdition(gallonsOfWater * 0.05, "Soda Ash"));
            }
        }

        // Alkalinity
        if (newReading.getAlkalinity() < minAlkalinity) {
            System.out.println("Alkalinity is low. Current Alkalinity: " + newReading.getAlkalinity());
            System.out.print("Would you like to add Baking Soda to increase Alkalinity? (y/n): ");
            String response = scanner.next();
            if (response.equalsIgnoreCase("y")) {
                printChemicalEffects();
                System.out.print("Enter gallons of water in pool: ");
                double gallonsOfWater = scanner.nextDouble();
                addBakingSoda(newReading, gallonsOfWater);
                additions.add(new ChemicalAdition(gallonsOfWater * 0.1, "Baking Soda"));
            }
        }

        // Chlorine
        if (newReading.getChlorine() < minChlorine) {
            System.out.println("Chlorine level is low. Current Chlorine: " + newReading.getChlorine());
            System.out.print("Would you like to add Pool Chlorine? (y/n): ");
            String response = scanner.next();
            if (response.equalsIgnoreCase("y")) {
                printChemicalEffects();
                System.out.print("Enter gallons of water in pool: ");
                double gallonsOfWater = scanner.nextDouble();
                addPoolChlorine(newReading, gallonsOfWater);
                additions.add(new ChemicalAdition(gallonsOfWater * 0.08, "Pool Chlorine"));
            }
        }

        createChemicalReading(newReading);
        System.out.println("Pool chemical review complete. All chemicals are above minimum. New chemical reading saved.\n");

        return additions.toArray(new ChemicalAdition[0]);
    }

    public void addBakingSoda(ChemicalReading reading, double gallonsOfWater) throws IOException {
        double currentAlkalinity = reading.getAlkalinity();
        double currentBakingSodaStock = getBakingSodaAmount();
        double requiredBakingSoda = gallonsOfWater * 0.1;

        if (currentBakingSodaStock <= 0) {
            System.out.println("No stock of baking soda available.");
            return;
        }

        double usedBakingSoda = Math.min(currentBakingSodaStock, requiredBakingSoda);
        double newAlkalinity = currentAlkalinity + (usedBakingSoda * 0.5 / gallonsOfWater);
        double newBakingSodaStock = currentBakingSodaStock - usedBakingSoda;

        if (usedBakingSoda < requiredBakingSoda) {
            System.out.println("Not enough baking soda. Partially added, but still insufficient.");
        }

        reading.setAlkalinity(newAlkalinity);
        updateReading(reading);

        Map<String, Object> inventoryMap = getChemicalInventory();
        if (inventoryMap != null) {
            inventoryMap.put("bakingSoda", newBakingSodaStock);
            storageHelper.getStore(FACILITY_CLEANING_REPORT_STORE_NAME).save(FACILITY_CLEANING_SUPPLIES_KEY, inventoryMap);
        }

        System.out.println("Baking soda successfully added.");
        System.out.printf("New Alkalinity Level: %.2f%n", newAlkalinity);
        System.out.printf("Remaining Baking Soda Stock: %.2f units%n", newBakingSodaStock);
    }

    public void addSodaAsh(ChemicalReading reading, double gallonsOfWater) throws IOException {
        double currentPh = reading.getphLevel();
        double currentSodaAshStock = getSodaAshAmount();
        double requiredSodaAsh = gallonsOfWater * 0.05;

        if (currentSodaAshStock <= 0) {
            System.out.println("No stock of soda ash available.");
            return;
        }

        double usedSodaAsh = Math.min(currentSodaAshStock, requiredSodaAsh);
        double newPhLevel = currentPh + (usedSodaAsh * 0.3 / gallonsOfWater);
        double newSodaAshStock = currentSodaAshStock - usedSodaAsh;

        if (usedSodaAsh < requiredSodaAsh) {
            System.out.println("Not enough soda ash. Partially added, but still insufficient.");
        }

        reading.setphLevel(newPhLevel);
        updateReading(reading);

        Map<String, Object> inventoryMap = getChemicalInventory();
        if (inventoryMap != null) {
            inventoryMap.put("sodaAsh", newSodaAshStock);
            inventoryStorageHelper.getStore(INVENTORY_STORE_NAME).save(FACILITY_CLEANING_SUPPLIES_KEY, inventoryMap);
        }

        System.out.println("Soda Ash successfully added.");
        System.out.printf("New pH Level: %.2f%n", newPhLevel);
        System.out.printf("Remaining Soda Ash Stock: %.2f units%n", newSodaAshStock);
    }

    public void addPoolChlorine(ChemicalReading reading, double gallonsOfWater) throws IOException {
        double currentChlorineLevel = reading.getChlorine();
        double currentPoolChlorineStock = getPoolChlorineAmount();
        double requiredChlorine = gallonsOfWater * 0.08;

        if (currentPoolChlorineStock <= 0) {
            System.out.println("No stock of pool chlorine available.");
            return;
        }

        double usedChlorine = Math.min(currentPoolChlorineStock, requiredChlorine);
        double newChlorineLevel = currentChlorineLevel + (usedChlorine * 0.4 / gallonsOfWater);
        double newPoolChlorineStock = currentPoolChlorineStock - usedChlorine;

        if (usedChlorine < requiredChlorine) {
            System.out.println("Not enough pool chlorine. Partially added, but still insufficient.");
        }

        reading.setChlorine(newChlorineLevel);
        updateReading(reading);

        Map<String, Object> inventoryMap = getChemicalInventory();
        if (inventoryMap != null) {
            inventoryMap.put("poolChlorine", newPoolChlorineStock);
            inventoryStorageHelper.getStore(INVENTORY_STORE_NAME).save(FACILITY_CLEANING_SUPPLIES_KEY, inventoryMap);
        }

        System.out.println("Pool Chlorine successfully added.");
        System.out.printf("New Chlorine Level: %.2f%n", newChlorineLevel);
        System.out.printf("Remaining Pool Chlorine Stock: %.2f units%n", newPoolChlorineStock);
    }

    public void printChemicalInventory() throws IOException {
        Map<String, Object> inventory = getChemicalInventory();

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
        List<Map<String, Object>> allReadings = getAllChemicalReadings();

        if (allReadings.isEmpty()) {
            System.out.println("No chemical readings available.");
            return;
        }

        System.out.println("All Chemical Readings:");
        System.out.println("----------------------");

        for (Map<String, Object> readingMap : allReadings) {
            ChemicalReading reading = convertMapToReading(readingMap);
            System.out.println("ID: " + reading.getId());
            System.out.println("Date: " + reading.getMaintainedDate());
            System.out.println("pH Level: " + reading.getphLevel());
            System.out.println("Alkalinity: " + reading.getAlkalinity());
            System.out.println("Chlorine Level: " + reading.getChlorine());
            System.out.println("----------------------");
        }

        printChemicalInventory();
    }

    public void addToChemicalInventory(Scanner scnr) throws IOException {
        System.out.println("=== Add to Chemical Inventory ===");
        Map<String, Object> inventory = getChemicalInventory();

        if (inventory == null || inventory.isEmpty()) {
            System.out.println("Chemical inventory is empty or not initialized.");
            return;
        }

        String chemical = selectChemical(scnr, inventory.keySet());
        if (chemical == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        System.out.print("Enter amount to add: ");
        double amountToAdd = scnr.nextDouble();
        scnr.nextLine(); // Consume newline

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

    public void subtractFromChemicalInventory(Scanner scnr) throws IOException {
        System.out.println("=== Subtract from Chemical Inventory ===");
        Map<String, Object> inventory = getChemicalInventory();

        if (inventory == null || inventory.isEmpty()) {
            System.out.println("Chemical inventory is empty or not initialized.");
            return;
        }

        String chemical = selectChemical(scnr, inventory.keySet());
        if (chemical == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        System.out.print("Enter amount to subtract: ");
        double amountToSubtract = scnr.nextDouble();
        scnr.nextLine(); // Consume newline

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

    public void setChemicalInventory(Scanner scnr) throws IOException {
        System.out.println("=== Set Chemical Inventory ===");
        Map<String, Object> inventory = getChemicalInventory();

        if (inventory == null || inventory.isEmpty()) {
            System.out.println("Chemical inventory is empty or not initialized.");
            return;
        }

        String chemical = selectChemical(scnr, inventory.keySet());
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

    private String selectChemical(Scanner scnr, Set<String> chemicals) {
        List<String> chemicalList = new ArrayList<>(chemicals);
        System.out.println("Available Chemicals:");
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
            scnr.nextLine(); // Consume newline
        } catch (InputMismatchException e) {
            scnr.nextLine(); // Clear invalid input
            System.out.println("Invalid input. Operation cancelled.");
            return null;
        }

        if (selection == 0) {
            return null; // Operation cancelled
        }

        if (selection < 1 || selection > chemicalList.size()) {
            System.out.println("Invalid selection. Operation cancelled.");
            return null;
        }

        return chemicalList.get(selection - 1);
    }

    public void setDaysBetweenCleaning(Scanner scnr) throws IOException {
        System.out.println("=== Set Days Between Cleaning ===");
        Map<String, Object> daysBetweenCleaningMap = getDaysBetweenCleaning();

        if (daysBetweenCleaningMap == null || daysBetweenCleaningMap.isEmpty()) {
            System.out.println("Days Between Cleaning is empty or not initialized.");
            return;
        }

        String facility = selectFacility(scnr, daysBetweenCleaningMap.keySet());
        if (facility == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        System.out.printf("Current days between cleaning of %s: %.2f %s%n", capitalize(facility), ((Number) daysBetweenCleaningMap.get(facility)).doubleValue(), getChemicalUnit(facility));
        System.out.print("Enter new amount: ");
        int newAmount = scnr.nextInt();
        scnr.nextLine(); // Consume newline

        if (newAmount < 0) {
            System.out.println("Amount cannot be negative.");
            return;
        }

        daysBetweenCleaningMap.put(facility, newAmount);

        // Update in storage
        inventoryStorageHelper.getStore(FACILITY_CLEANING_REPORT_STORE_NAME).save(DAYS_BETWEEN_CLEANING_KEY, daysBetweenCleaningMap);

        System.out.printf("Successfully set " + capitalize(facility) + " to " + newAmount);
    }

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
}
