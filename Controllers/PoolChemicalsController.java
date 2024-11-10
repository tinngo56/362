package Controllers;

import Models.PoolMaintenance.ChemicalAdition;
import Models.PoolMaintenance.ChemicalReading;
import Storage.StorageHelper;
import Storage.StorageHelper.DataStore;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/*
bakingSoda increases alkalinity
sodaAsh increases pH
poolChlorine increases chlorine

store = folder
key = specific save similar to id
*/
public class PoolChemicalsController {

    private StorageHelper storageHelper;
    private StorageHelper inventoryStorageHelper;
    private final String CHEMICAL_READING_STORE_NAME = "poolChemicalReadings";
    private final String INVENTORY_STORE_NAME = "inventory";
    private final String CHEMICAL_STORAGE_KEY = "chemicalStorage";
    private int nextId;

    public PoolChemicalsController(String baseDirectory) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, CHEMICAL_READING_STORE_NAME);
        this.inventoryStorageHelper = new StorageHelper(baseDirectory, INVENTORY_STORE_NAME);
        this.nextId = determineNextId();
        initializeChemicalInventory();
    }

//---------------------------store manging stuff--------------------------- TODO only to find section

    private int determineNextId() throws IOException {
        List<Map<String, Object>> allReadings = storageHelper.getStore(CHEMICAL_READING_STORE_NAME).loadAll();
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

    private void initializeChemicalInventory() throws IOException {
        DataStore<?> store = inventoryStorageHelper.getStore(INVENTORY_STORE_NAME);
        Map<String, Object> inventoryData = store.load(CHEMICAL_STORAGE_KEY);
        if (inventoryData == null || !(inventoryData instanceof Map)) {
            Map<String, Object> initialInventory = new HashMap<>();
            initialInventory.put("bakingSoda", 100.0);
            initialInventory.put("sodaAsh", 100.0);
            initialInventory.put("poolChlorine", 100.0);
            store.save(CHEMICAL_STORAGE_KEY, initialInventory);
            System.out.println("Initialized chemical inventory with default stock.");
        }
    }

    public void createChemicalReading(ChemicalReading reading) throws IOException {
        Map<String, Object> readingMap = convertReadingToMap(reading);
        storageHelper.getStore(CHEMICAL_READING_STORE_NAME).save(String.valueOf(reading.getId()), readingMap);
    }

    public ChemicalReading getReading(int id) throws IOException {
        Map<String, Object> data = storageHelper.getStore(CHEMICAL_READING_STORE_NAME).load(String.valueOf(id));
        return data != null ? convertMapToReading(data) : null;
    }

    public void updateReading(ChemicalReading reading) throws IOException {
        Map<String, Object> readingMap = convertReadingToMap(reading);
        storageHelper.getStore(CHEMICAL_READING_STORE_NAME).save(String.valueOf(reading.getId()), readingMap);
    }

    public void deleteReading(int id) throws IOException {
        storageHelper.getStore(CHEMICAL_READING_STORE_NAME).delete(String.valueOf(id));
    }

    public int getNumOfReadings() throws IOException {
        List<Map<String, Object>> allReadings = storageHelper.getStore(CHEMICAL_READING_STORE_NAME).loadAll();
        return allReadings.size();
    }

    public double getBakingSodaAmount() throws IOException {
        Map<String, Object> data = getChemicalInventory();
        return data != null ? ((Number) data.getOrDefault("bakingSoda", 0.0)).doubleValue() : 0.0;
    }

    public double getSodaAshAmount() throws IOException {
        Map<String, Object> data = getChemicalInventory();
        return data != null ? ((Number) data.getOrDefault("sodaAsh", 0.0)).doubleValue() : 0.0;
    }

    public double getPoolChlorineAmount() throws IOException {
        Map<String, Object> data = getChemicalInventory();
        return data != null ? ((Number) data.getOrDefault("poolChlorine", 0.0)).doubleValue() : 0.0;
    }

    private Map<String, Object> getChemicalInventory() throws IOException {
        Map<String, Object> inventoryData = inventoryStorageHelper.getStore(INVENTORY_STORE_NAME).load(CHEMICAL_STORAGE_KEY);
        if (inventoryData instanceof Map) {
            return (Map<String, Object>) inventoryData;
        } else {
            System.err.println("Expected inventory data to be a Map but found: " +
                    (inventoryData != null ? inventoryData.getClass().getName() : "null"));
            return null;
        }
    }

    private Map<String, Object> convertReadingToMap(ChemicalReading reading) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", reading.getId());
        map.put("date", reading.getMaintainedDate());
        map.put("phLevel", reading.getphLevel());
        map.put("alkalinity", reading.getAlkalinity());
        map.put("chlorine", reading.getChlorine());
        return map;
    }

    public ChemicalReading convertMapToReading(Map<String, Object> map) {
        Number idNumber = (Number) map.get("id");
        int id = idNumber != null ? idNumber.intValue() : 0;

        String date = (String) map.get("date");

        Number phNumber = (Number) map.get("phLevel");
        double ph = phNumber != null ? phNumber.doubleValue() : 0.0;
        Number alkalinityNumber = (Number) map.get("alkalinity");
        double alkalinity = alkalinityNumber != null ? alkalinityNumber.doubleValue() : 0.0;
        Number chlorineNumber = (Number) map.get("chlorine");
        double chlorine = chlorineNumber != null ? chlorineNumber.doubleValue() : 0.0;

        return new ChemicalReading(id, date, ph, alkalinity, chlorine);
    }

    public List<Map<String, Object>> getAllChemicalReadings() throws IOException {
        return storageHelper.getStore(CHEMICAL_READING_STORE_NAME).loadAll();
    }

//---------------------------interactive print stuff--------------------------- TODO only to find section

    private ChemicalReading makeChemicalReadingFromInput(Scanner scanner) throws IOException {
        System.out.println("Enter the following details for a new chemical reading:");

        int id = nextId++;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDate.now().format(formatter);

        System.out.print("pH Level: ");
        double phLevel = scanner.nextDouble();

        System.out.print("Alkalinity: ");
        double alkalinity = scanner.nextDouble();

        System.out.print("Chlorine Level: ");
        double chlorine = scanner.nextDouble();
        scanner.nextLine(); // Consume the remaining newline

        ChemicalReading newReading = new ChemicalReading(id, date, phLevel, alkalinity, chlorine);

        System.out.println("New Chemical Reading Created: " + newReading);

        return newReading;
    }

    public void printChemicalEffects() {
        System.out.println("Chemical Effects on Pool:");

        System.out.println("1. Baking Soda:");
        System.out.println("   - Effect on Pool: Increases alkalinity.");
        System.out.println("   - Dosage: 0.1 units per gallon of water.");
        System.out.println("   - Impact: Each unit used increases alkalinity by 0.5 per gallon of water.");

        System.out.println("2. Soda Ash:");
        System.out.println("   - Effect on Pool: Raises pH level.");
        System.out.println("   - Dosage: 0.05 units per gallon of water.");
        System.out.println("   - Impact: Each unit used increases pH by 0.3 per gallon of water.");

        System.out.println("3. Pool Chlorine:");
        System.out.println("   - Effect on Pool: Increases chlorine level.");
        System.out.println("   - Dosage: 0.08 units per gallon of water.");
        System.out.println("   - Impact: Each unit used increases chlorine level by 0.4 per gallon of water.");
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
            storageHelper.getStore(CHEMICAL_READING_STORE_NAME).save(CHEMICAL_STORAGE_KEY, inventoryMap);
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
            inventoryStorageHelper.getStore(INVENTORY_STORE_NAME).save(CHEMICAL_STORAGE_KEY, inventoryMap);
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
            inventoryStorageHelper.getStore(INVENTORY_STORE_NAME).save(CHEMICAL_STORAGE_KEY, inventoryMap);
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
            if(entry.getKey().equals("lastModified")) {
                continue;
            }
            String unit = "kg";
            if(entry.getKey().equals("poolChlorine")) {
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
        inventoryStorageHelper.getStore(INVENTORY_STORE_NAME).save(CHEMICAL_STORAGE_KEY, inventory);


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
        inventoryStorageHelper.getStore(INVENTORY_STORE_NAME).save(CHEMICAL_STORAGE_KEY, inventory);

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
        inventoryStorageHelper.getStore(INVENTORY_STORE_NAME).save(CHEMICAL_STORAGE_KEY, inventory);

        System.out.printf("Successfully set %s to %.2f %s.%n", capitalize(chemical), newAmount, getChemicalUnit(chemical));
    }

    private String selectChemical(Scanner scnr, Set<String> chemicals) {
        List<String> chemicalList = new ArrayList<>(chemicals);
        System.out.println("Available Chemicals:");
        for (int i = 0; i < chemicalList.size(); i++) {
            if(chemicalList.get(i).equals("lastModified")){
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