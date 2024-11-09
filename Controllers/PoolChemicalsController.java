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
*/
public class PoolChemicalsController {

    private StorageHelper storageHelper;
    private final String CHEMICAL_READING_STORE_NAME = "poolChemicalReadings"; // the store name
    private final String CHEMICAL_STORAGE_KEY = "chemicalStorage"; // the key within the store for inventory
    private int nextId;

    public PoolChemicalsController(String baseDirectory) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, CHEMICAL_READING_STORE_NAME);
        this.nextId = determineNextId();
        initializeChemicalInventory();
    }

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
        DataStore<?> store = storageHelper.getStore(CHEMICAL_READING_STORE_NAME);
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
        Map<String, Object> inventoryData = storageHelper.getStore(CHEMICAL_READING_STORE_NAME).load(CHEMICAL_STORAGE_KEY);
        if (inventoryData instanceof Map) {
            return (Map<String, Object>) inventoryData;
        } else {
            System.err.println("Expected inventory data to be a Map but found: " +
                    (inventoryData != null ? inventoryData.getClass().getName() : "null"));
            return null;
        }
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
            storageHelper.getStore(CHEMICAL_READING_STORE_NAME).save(CHEMICAL_STORAGE_KEY, inventoryMap);
        }

        System.out.println("Soda Ash successfully added.");
        System.out.printf("New pH Level: %.2f%n", newPhLevel);
        System.out.printf("Remaining Soda Ash Stock: %.2f units%n", newSodaAshStock);
    }

    /**
     * Adds Pool Chlorine to increase chlorine level.
     *
     * @param reading        the ChemicalReading object to update.
     * @param gallonsOfWater the volume of water in the pool.
     * @throws IOException if an I/O error occurs.
     */
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
            storageHelper.getStore(CHEMICAL_READING_STORE_NAME).save(CHEMICAL_STORAGE_KEY, inventoryMap);
        }

        System.out.println("Pool Chlorine successfully added.");
        System.out.printf("New Chlorine Level: %.2f%n", newChlorineLevel);
        System.out.printf("Remaining Pool Chlorine Stock: %.2f units%n", newPoolChlorineStock);
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

    private ChemicalReading convertMapToReading(Map<String, Object> map) {
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
}