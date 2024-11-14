package Controllers;

import Models.CookFood.Menu;
import Models.CookFood.Units;
import Storage.StorageHelper;
import Storage.StorageHelper.DataStore;

import java.io.IOException;
import java.util.*;

public class CookBreakfastController {

    private StorageHelper storageHelper;
    private StorageHelper inventoryStorageHelper;
    private final String COOK_BREAKFAST_STORE_NAME = "cookBreakfastReports";
    private final String INVENTORY_STORE_NAME = "inventory";
    private final String INGREDIENTS_STORAGE_KEY = "breakfastIngredientsStorage";
    private int nextId;
    private final Units units = new Units();

    public CookBreakfastController(String baseDirectory) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, COOK_BREAKFAST_STORE_NAME);
        this.inventoryStorageHelper = new StorageHelper(baseDirectory, INVENTORY_STORE_NAME);
        this.nextId = determineNextId();
        initializeIngredientInventory();
    }

    //---------------------------Store Managing Stuff--------------------------- TODO

    private int determineNextId() throws IOException {
        List<Map<String, Object>> allReadings = storageHelper.getStore(COOK_BREAKFAST_STORE_NAME).loadAll();
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

    private void initializeIngredientInventory() throws IOException {
        DataStore<?> store = inventoryStorageHelper.getStore(INVENTORY_STORE_NAME);
        Map<String, Object> inventoryData = store.load(INGREDIENTS_STORAGE_KEY);
        if (inventoryData == null || !(inventoryData instanceof Map)) {
            Map<String, Object> initialInventory = new HashMap<>();
            initialInventory.put("flour", 1000.0);        // grams
            initialInventory.put("sugar", 500.0);         // grams
            initialInventory.put("salt", 200.0);          // grams
            initialInventory.put("bakingPowder", 100.0);  // grams
            initialInventory.put("milk", 5.0);            // liters
            initialInventory.put("water", 10.0);          // liters
            initialInventory.put("butter", 300.0);        // grams
            initialInventory.put("bacon", 100.0);         // pieces
            initialInventory.put("eggs", 200.0);          // pieces
            initialInventory.put("sausage", 100.0);       // pieces
            initialInventory.put("syrup", 2.0);           // liters
            initialInventory.put("yogurt", 5.0);          // liters
            // Add more ingredients as needed

            store.save(INGREDIENTS_STORAGE_KEY, initialInventory);
            System.out.println("Initialized ingredient inventory with default stock.");
        }
    }

    //---------------------------Ingredient Inventory Management Methods---------------------------TODO

    private Map<String, Object> getIngredientInventory() throws IOException {
        Map<String, Object> inventoryData = inventoryStorageHelper.getStore(INVENTORY_STORE_NAME).load(INGREDIENTS_STORAGE_KEY);
        if (inventoryData instanceof Map) {
            return (Map<String, Object>) inventoryData;
        } else {
            System.out.println("Expected inventory data to be a Map but found: " +
                    (inventoryData != null ? inventoryData.getClass().getName() : "null"));
            return null;
        }
    }

    public void addIngredient(Scanner scnr) throws IOException {
        System.out.println("=== Add to Ingredient Inventory ===");
        Map<String, Object> inventory = getIngredientInventory();

        if (inventory == null || inventory.isEmpty()) {
            System.out.println("Ingredient inventory is empty or not initialized.");
            return;
        }

        String ingredient = selectIngredient(scnr, inventory.keySet());
        if (ingredient == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        System.out.print("Enter amount to add: ");
        double amountToAdd;
        try {
            amountToAdd = scnr.nextDouble();
            scnr.nextLine();
        } catch (InputMismatchException e) {
            scnr.nextLine();
            System.out.println("Invalid input. Operation cancelled.");
            return;
        }

        if (amountToAdd <= 0) {
            System.out.println("Amount to add must be positive.");
            return;
        }

        double currentAmount = ((Number) inventory.get(ingredient)).doubleValue();
        double newAmount = currentAmount + amountToAdd;
        inventory.put(ingredient, newAmount);

        inventoryStorageHelper.getStore(INVENTORY_STORE_NAME).save(INGREDIENTS_STORAGE_KEY, inventory);

        System.out.printf("Successfully added %.2f %s to %s.%n",
                amountToAdd, getUnit(ingredient), capitalize(ingredient));
        System.out.printf("New %s Stock: %.2f %s%n",
                capitalize(ingredient), newAmount, getUnit(ingredient));
    }

    public void subtractIngredient(Scanner scnr) throws IOException {
        System.out.println("=== Subtract from Ingredient Inventory ===");
        Map<String, Object> inventory = getIngredientInventory();

        if (inventory == null || inventory.isEmpty()) {
            System.out.println("Ingredient inventory is empty or not initialized.");
            return;
        }

        String ingredient = selectIngredient(scnr, inventory.keySet());
        if (ingredient == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        System.out.print("Enter amount to subtract: ");
        double amountToSubtract;
        try {
            amountToSubtract = scnr.nextDouble();
            scnr.nextLine();
        } catch (InputMismatchException e) {
            scnr.nextLine();
            System.out.println("Invalid input. Operation cancelled.");
            return;
        }

        if (amountToSubtract <= 0) {
            System.out.println("Amount to subtract must be positive.");
            return;
        }

        double currentAmount = ((Number) inventory.get(ingredient)).doubleValue();
        if (amountToSubtract > currentAmount) {
            System.out.println("Insufficient stock. Operation aborted.");
            return;
        }

        double newAmount = currentAmount - amountToSubtract;
        inventory.put(ingredient, newAmount);

        inventoryStorageHelper.getStore(INVENTORY_STORE_NAME).save(INGREDIENTS_STORAGE_KEY, inventory);

        System.out.printf("Successfully subtracted %.2f %s from %s.%n",
                amountToSubtract, getUnit(ingredient), capitalize(ingredient));
        System.out.printf("New %s Stock: %.2f %s%n",
                capitalize(ingredient), newAmount, getUnit(ingredient));
    }

    public void setIngredientAmount(Scanner scnr) throws IOException {
        System.out.println("=== Set Ingredient Inventory ===");
        Map<String, Object> inventory = getIngredientInventory();

        if (inventory == null || inventory.isEmpty()) {
            System.out.println("Ingredient inventory is empty or not initialized.");
            return;
        }

        String ingredient = selectIngredient(scnr, inventory.keySet());
        if (ingredient == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        double currentAmount = ((Number) inventory.get(ingredient)).doubleValue();
        System.out.printf("Current amount of %s: %.2f %s%n",
                capitalize(ingredient), currentAmount, getUnit(ingredient));

        System.out.print("Enter new amount: ");
        double newAmount;
        try {
            newAmount = scnr.nextDouble();
            scnr.nextLine();
        } catch (InputMismatchException e) {
            scnr.nextLine();
            System.out.println("Invalid input. Operation cancelled.");
            return;
        }

        if (newAmount < 0) {
            System.out.println("Amount cannot be negative.");
            return;
        }

        inventory.put(ingredient, newAmount);

        inventoryStorageHelper.getStore(INVENTORY_STORE_NAME).save(INGREDIENTS_STORAGE_KEY, inventory);

        System.out.printf("Successfully set %s to %.2f %s.%n",
                capitalize(ingredient), newAmount, getUnit(ingredient));
    }

    private String selectIngredient(Scanner scnr, Set<String> ingredients) {
        List<String> ingredientList = new ArrayList<>(ingredients);
        System.out.println("Available Ingredients:");
        for (int i = 0; i < ingredientList.size(); i++) {
            if (ingredientList.get(i).equals("lastModified")) {
                continue;
            }
            System.out.printf("%d. %s %s%n", i + 1, capitalize(ingredientList.get(i)), getUnit(ingredientList.get(i)));
        }
        System.out.println("0. Cancel");
        System.out.print("Select an ingredient by number: ");

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

        if (selection < 1 || selection > ingredientList.size()) {
            System.out.println("Invalid selection. Operation cancelled.");
            return null;
        }

        return ingredientList.get(selection - 1);
    }

    private String getUnit(String ingredient) {
        switch (ingredient) {
            case "flour":
                return units.FLOUR;
            case "sugar":
                return units.SUGAR;
            case "salt":
                return units.SALT;
            case "bakingPowder":
                return units.BAKING_POWDER;
            case "milk":
                return units.MILK;
            case "water":
                return units.WATER;
            case "butter":
                return units.BUTTER;
            case "bacon":
                return units.BACON;
            case "eggs":
                return units.EGGS;
            case "sausage":
                return units.SAUSAGE;
            case "syrup":
                return units.SYRUP;
            case "yogurt":
                return units.YOGURT;
            default:
                return "units";
        }
    }

    public boolean checkIngredients(int guests) throws IOException {
        Map<String, Map<String, Double>> recipes = getRecipes();

        Map<String, Double> totalIngredientsNeeded = calculateTotalIngredients(recipes, guests);

        Map<String, Double> availableIngredients = getAvailableIngredients();

        List<String> insufficientIngredients = new ArrayList<>();

        for (Map.Entry<String, Double> entry : totalIngredientsNeeded.entrySet()) {
            String ingredient = entry.getKey();
            double requiredAmount = entry.getValue();
            double availableAmount = availableIngredients.getOrDefault(ingredient, 0.0);

            if (availableAmount < requiredAmount) {
                double shortage = requiredAmount - availableAmount;
                insufficientIngredients.add(String.format("- %s: Need %.2f %s more.",
                        capitalize(ingredient), shortage, getUnit(ingredient)));
            }
        }

        if (!insufficientIngredients.isEmpty()) {
            System.out.println("Insufficient ingredients for the requested number of guests:");
            for (String deficiency : insufficientIngredients) {
                System.out.println(deficiency);
            }
            return false;
        }

        // All ingredients are good
        System.out.println("Sufficient ingredients for requested " + guests + " of guests!");
        return true;
    }

    //---------------------------Interactive Print Stuff---------------------------TODO
    private Menu makeCookBreakfastReportFromInput(Scanner scanner) throws IOException {
        System.out.println("Enter the following details for a new Breakfast Report:");

        System.out.print("Number of guests: ");
        int guests;
        try {
            guests = scanner.nextInt();
            scanner.nextLine();
            if (guests <= 0) {
                System.out.println("Number of guests must be positive.");
                return null;
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Invalid input. Operation cancelled.");
            return null;
        }

        Map<String, Map<String, Double>> recipes = getRecipes();
        Map<String, Double> availableInventory = getAvailableIngredients();

        // Determine the maximum number of guests based on available inventory
        int possibleGuests = determineMaxGuests(recipes, availableInventory);



        if (guests == 0) {
            System.out.println("Cannot create a breakfast report with 0 guests.");
            return null;
        }

        // Now calculate the total ingredients needed based on the adjusted number of guests
        Map<String, Double> totalIngredientsNeeded = calculateTotalIngredients(recipes, guests);

        // Deduct the ingredients or just print what is missing
        deductIngredients(totalIngredientsNeeded);

        if (possibleGuests < guests) {
            System.out.printf("Not enough ingredients for %d guests. The max amount of guest that can be served is: %d guests.%n", guests, possibleGuests);
            //guests = possibleGuests;
            return null;
        }

        System.out.print("Success cooking sausage?: ");
        boolean sausage = nextBoolean(scanner);

        System.out.print("Success cooking bacon?: ");
        boolean bacon = nextBoolean(scanner);

        System.out.print("Success cooking eggs?: ");
        boolean eggs = nextBoolean(scanner);

        System.out.print("Success cooking waffles?: ");
        boolean waffles = nextBoolean(scanner);

        System.out.print("Success cooking pancakes?: ");
        boolean pancakes = nextBoolean(scanner);

        System.out.print("Success setting up syrup?: ");
        boolean syrup = nextBoolean(scanner);

        System.out.print("Success setting up yogurt?: ");
        boolean yogurt = nextBoolean(scanner);


        System.out.print("Were there any issues?: ");
        boolean areIssues = nextBoolean(scanner);
        String issues = "";
        if (areIssues) {
            System.out.println("What were the issues?");
            issues = scanner.nextLine();
        } else {
            issues = "No issues";
        }

        int id = nextId++;
        Menu menu = new Menu(id, issues, sausage, bacon, eggs, waffles, pancakes, syrup, yogurt, guests);
        System.out.println("New Breakfast Report Created: " + menu);

        return menu;
    }

//    private Menu makeCookBreakfastReportFromInput(Scanner scanner) throws IOException {
//        System.out.println("Enter the following details for a new Breakfast Report:");
//
//        System.out.print("Number of guests: ");
//        int guests;
//        try {
//            guests = scanner.nextInt();
//            scanner.nextLine();
//            if (guests <= 0) {
//                System.out.println("Number of guests must be positive.");
//                return null;
//            }
//        } catch (InputMismatchException e) {
//            scanner.nextLine();
//            System.out.println("Invalid input. Operation cancelled.");
//            return null;
//        }
//
//        //todo
//        System.out.print("Are there any issues?: ");
//        boolean areIssues = nextBoolean(scanner);
//        String issues = "";
//        if (areIssues) {
//            System.out.println("What are the issues?");
//            issues = scanner.nextLine();
//        } else {
//            issues = "No issues";
//        }
//
//
//        Map<String, Map<String, Double>> recipes = getRecipes();
//
//        Map<String, Double> totalIngredientsNeeded = calculateTotalIngredients(recipes, guests);
//
//        Map<String, Double> availableInventory = getAvailableIngredients();
//
//        int possibleGuests = determineMaxGuests(totalIngredientsNeeded, availableInventory, guests);
//
//        if (possibleGuests < guests) {
//            System.out.printf("Not enough ingredients for %d guests. Creating report for %d guests.%n", guests, possibleGuests);
//            guests = possibleGuests;
//        }
//
//        deductIngredients(totalIngredientsNeeded, guests);
//
//        System.out.print("Success cooking sausage?: ");
//        boolean sausage = nextBoolean(scanner);
//
//        System.out.print("Success cooking bacon?: ");
//        boolean bacon = nextBoolean(scanner);
//
//        System.out.print("Success cooking eggs?: ");
//        boolean eggs = nextBoolean(scanner);
//
//        System.out.print("Success cooking waffles?: ");
//        boolean waffles = nextBoolean(scanner);
//
//        System.out.print("Success cooking pancakes?): ");
//        boolean pancakes = nextBoolean(scanner);
//
//        System.out.print("Success setting up syrup?: ");
//        boolean syrup = nextBoolean(scanner);
//
//        System.out.print("Success setting up yogurt?: ");
//        boolean yogurt = nextBoolean(scanner);
//
//        int id = nextId++;
//        Menu menu = new Menu(id, issues, sausage, bacon, eggs, waffles, pancakes, syrup, yogurt, guests);
//        System.out.println("New Breakfast Report Created: " + menu);
//
//        return menu;
//    }

    public void viewStock() throws IOException {
        Map<String, Object> inventory = getIngredientInventory();

        if (inventory == null || inventory.isEmpty()) {
            System.out.println("Ingredient inventory is empty or not initialized.");
            return;
        }

        System.out.println("=== Current Ingredient Stock ===");

        int ingredientWidth = 15;
        int amountWidth = 15;
        int unitWidth = 10;

        System.out.printf("%-" + ingredientWidth + "s %-" + amountWidth + "s %-" + unitWidth + "s%n",
                "Ingredient", "Quantity", "Unit");
        System.out.println("-------------------------------------------------------");

        for (Map.Entry<String, Object> entry : inventory.entrySet()) {
            String ingredient = entry.getKey();
            if (ingredient.equals("lastModified")) {
                continue;
            }

            double quantity = ((Number) entry.getValue()).doubleValue();


            String unit = getUnit(ingredient);
            System.out.printf("%-" + ingredientWidth + "s %-" + amountWidth + ".2f %-" + unitWidth + "s%n",
                    capitalize(ingredient), quantity, unit);
        }

        System.out.println("=====================================");
    }


    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private Map<String, Double> calculateTotalIngredients(Map<String, Map<String, Double>> recipes, int guests) {
        Map<String, Double> total = new HashMap<>();
        for (Map<String, Double> recipe : recipes.values()) {
            for (Map.Entry<String, Double> entry : recipe.entrySet()) {
                String ingredient = entry.getKey();
                Double amountPerGuest = entry.getValue();
                total.put(ingredient, total.getOrDefault(ingredient, 0.0) + (amountPerGuest * guests));
            }
        }
        return total;
    }

    private Map<String, Double> getAvailableIngredients() throws IOException {
        Map<String, Object> inventoryData = getIngredientInventory();
        Map<String, Double> available = new HashMap<>();
        if (inventoryData != null) {
            for (Map.Entry<String, Object> entry : inventoryData.entrySet()) {
                if (entry.getKey().equals("lastModified")) {
                    continue;
                }
                available.put(entry.getKey(), ((Number) entry.getValue()).doubleValue());
            }
        }
        return available;
    }

    private int determineMaxGuests(Map<String, Map<String, Double>> recipes, Map<String, Double> available) {
        int maxGuests = Integer.MAX_VALUE;

        for (Map.Entry<String, Map<String, Double>> recipeEntry : recipes.entrySet()) {
            String dish = recipeEntry.getKey();
            Map<String, Double> ingredients = recipeEntry.getValue();

            for (Map.Entry<String, Double> ingredientEntry : ingredients.entrySet()) {
                String ingredient = ingredientEntry.getKey();
                double amountPerGuest = ingredientEntry.getValue();

                if (amountPerGuest <= 0) {
                    continue;
                }

                double availableAmount = available.getOrDefault(ingredient, 0.0);
                int possibleForThisIngredient = (int) Math.floor(availableAmount / amountPerGuest);

                if (possibleForThisIngredient < maxGuests) {
                    maxGuests = possibleForThisIngredient;
                }

                //cant have any guests
                if (maxGuests == 0) {
                    return 0;
                }
            }
        }

        return maxGuests == Integer.MAX_VALUE ? 0 : maxGuests;
    }

//    private int determineMaxGuests(Map<String, Double> required, Map<String, Double> available, int requestedGuests) {
//        int maxGuests = requestedGuests;
//        for (Map.Entry<String, Double> entry : required.entrySet()) {
//            String ingredient = entry.getKey();
//            double needed = entry.getValue();
//            double availableAmount = available.getOrDefault(ingredient, 0.0);
//            if (needed == 0) continue;
//            int possible = (int) Math.floor(availableAmount / (needed / requestedGuests));
//            if (possible < maxGuests) {
//                maxGuests = possible;
//            }
//        }
//        return maxGuests;
//    }

//    private void deductIngredients(Map<String, Double> totalNeeded, int guests) throws IOException {
//        Map<String, Object> inventoryData = getIngredientInventory();
//        if (inventoryData == null) {
//            System.out.println("Ingredient inventory is not available.");
//            return;
//        }
//
//        for (Map.Entry<String, Double> entry : totalNeeded.entrySet()) {
//            String ingredient = entry.getKey();
//            double totalAmountNeeded = entry.getValue();
//            double amountPerGuest = totalAmountNeeded / guests;
//            double totalToDeduct = amountPerGuest * guests;
//            double currentAmount = ((Number) inventoryData.getOrDefault(ingredient, 0.0)).doubleValue();
//            inventoryData.put(ingredient, currentAmount - totalToDeduct);
//        }
//        inventoryStorageHelper.getStore(INVENTORY_STORE_NAME).save(INGREDIENTS_STORAGE_KEY, inventoryData);
//    }

    private void deductIngredients(Map<String, Double> totalNeeded) throws IOException {
        Map<String, Object> inventoryData = getIngredientInventory();
        if (inventoryData == null) {
            System.out.println("Ingredient inventory is not available.");
        }

        boolean missingIngredient = false;
        // Check for all insufficient ingredients
        for (Map.Entry<String, Double> entry : totalNeeded.entrySet()) {
            String ingredient = entry.getKey();
            double amountToDeduct = entry.getValue();
            double currentAmount = ((Number) inventoryData.getOrDefault(ingredient, 0.0)).doubleValue();

            if (currentAmount < amountToDeduct) {
                missingIngredient = true;
                double shortage = amountToDeduct - currentAmount;
                System.out.printf("Insufficient %s. Required: %.2f, Available: %.2f, Need: %.2f%n",
                        capitalize(ingredient), amountToDeduct, currentAmount, shortage);
            }
        }

        if (missingIngredient) {
            System.out.println("Missing ingredeint so not deducting ingredients from inventory");
            return;
        }


        // Proceed with deduction since all ingredients are sufficient
        for (Map.Entry<String, Double> entry : totalNeeded.entrySet()) {
            String ingredient = entry.getKey();
            double amountToDeduct = entry.getValue();
            double currentAmount = ((Number) inventoryData.getOrDefault(ingredient, 0.0)).doubleValue();
            inventoryData.put(ingredient, currentAmount - amountToDeduct);
        }

        inventoryStorageHelper.getStore(INVENTORY_STORE_NAME).save(INGREDIENTS_STORAGE_KEY, inventoryData);
    }

    private Map<String, Map<String, Double>> getRecipes() {
        Map<String, Map<String, Double>> recipes = new HashMap<>();

        // Sausage
        Map<String, Double> sausageRecipe = new HashMap<>();
        sausageRecipe.put("sausage", 1.0); // 1 piece per guest
        recipes.put("sausage", sausageRecipe);

        // Bacon
        Map<String, Double> baconRecipe = new HashMap<>();
        baconRecipe.put("bacon", 2.0); // 2 pieces per guest
        recipes.put("bacon", baconRecipe);

        // Eggs
        Map<String, Double> eggsRecipe = new HashMap<>();
        eggsRecipe.put("eggs", 2.0); // 2 pieces per guest
        eggsRecipe.put("butter", 10.0); // grams per guest
        recipes.put("eggs", eggsRecipe);

        // Waffles
        Map<String, Double> wafflesRecipe = new HashMap<>();
        wafflesRecipe.put("flour", 100.0); // grams per guest
        wafflesRecipe.put("milk", 0.2);    // liters per guest
        wafflesRecipe.put("sugar", 10.0);  // grams per guest
        wafflesRecipe.put("butter", 20.0); // grams per guest
        recipes.put("waffles", wafflesRecipe);

        // Pancakes
        Map<String, Double> pancakesRecipe = new HashMap<>();
        pancakesRecipe.put("flour", 100.0);      // grams per guest
        pancakesRecipe.put("milk", 0.2);         // liters per guest
        pancakesRecipe.put("eggs", 1.0);         // pieces per guest
        pancakesRecipe.put("sugar", 10.0);       // grams per guest
        pancakesRecipe.put("bakingPowder", 5.0); // grams per guest
        pancakesRecipe.put("butter", 15.0);      // grams per guest
        recipes.put("pancakes", pancakesRecipe);

        // Syrup
        Map<String, Double> syrupRecipe = new HashMap<>();
        syrupRecipe.put("syrup", 0.05); // liters per guest
        recipes.put("syrup", syrupRecipe);

        // Yogurt
        Map<String, Double> yogurtRecipe = new HashMap<>();
        yogurtRecipe.put("yogurt", 0.1); // liters per guest
        recipes.put("yogurt", yogurtRecipe);

        return recipes;
    }

    //unused
//    private int determineMaxGuestsBasedOnIngredients(Map<String, Map<String, Double>> recipes, int guests) throws IOException {
//        Map<String, Double> totalIngredientsNeeded = calculateTotalIngredients(recipes, guests);
//        Map<String, Double> availableInventory = getAvailableIngredients();
//        return determineMaxGuests(totalIngredientsNeeded, availableInventory, guests);
//    }

    public List<Map<String, Object>> getAllCookingReportsReadings() throws IOException {
        return storageHelper.getStore(COOK_BREAKFAST_STORE_NAME).loadAll();
    }

    private Map<String, Object> convertMenuToMap(Menu menu) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", menu.getId());
        map.put("issues", menu.getIssues());
        map.put("sausage", menu.isSausage());
        map.put("bacon", menu.isBacon());
        map.put("eggs", menu.isEggs());
        map.put("waffles", menu.isWaffles());
        map.put("pancakes", menu.isPancakes());
        map.put("syrup", menu.isSyrup());
        map.put("yogurt", menu.isYogurt());
        map.put("guests", menu.getGuests());
        return map;
    }

    public Menu convertMapToMenu(Map<String, Object> map) {
        Number idNumber = (Number) map.get("id");
        int id = idNumber != null ? idNumber.intValue() : 0;
        String issues = (String) map.get("issues");
        boolean sausage = (boolean) map.get("sausage");
        boolean bacon = (boolean) map.get("bacon");
        boolean eggs = (boolean) map.get("eggs");
        boolean waffles = (boolean) map.get("waffles");
        boolean pancakes = (boolean) map.get("pancakes");
        boolean syrup = (boolean) map.get("syrup");
        boolean yogurt = (boolean) map.get("yogurt");
        Number guestsNumber = (Number) map.get("guests");
        int guests = guestsNumber != null ? guestsNumber.intValue() : 0;

        return new Menu(id, issues, sausage, bacon, eggs, waffles, pancakes, syrup, yogurt, guests);
    }

    public void createBreakfastReport(Menu menu) throws IOException {
        Map<String, Object> readingMap = convertMenuToMap(menu);
        storageHelper.getStore(COOK_BREAKFAST_STORE_NAME).save(String.valueOf(menu.getId()), readingMap);
    }

    public Menu getBreakfastReport(int id) throws IOException {
        Map<String, Object> data = storageHelper.getStore(COOK_BREAKFAST_STORE_NAME).load(String.valueOf(id));
        return data != null ? convertMapToMenu(data) : null;
    }

    //TODO
    public void deleteReport(Scanner scanner) throws IOException {
        List<Map<String, Object>> allReports = storageHelper.getStore(COOK_BREAKFAST_STORE_NAME).loadAll();

        if (allReports.isEmpty()) {
            System.out.println("No breakfast reports found.");
            return;
        }

        System.out.println("Available Breakfast Reports:");
        for (Map<String, Object> reportMap : allReports) {
            int reportId = ((Number) reportMap.get("id")).intValue();
            Menu menu = convertMapToMenu(reportMap);
            System.out.println("ID: " + reportId + " - " + menu);
        }

        System.out.print("Enter the ID of the report you wish to delete: ");
        int idToDelete;
        try {
            idToDelete = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Invalid input. Please enter a valid report ID.");
            return;
        }

        // does ID exists
        Map<String, Object> reportToDelete = storageHelper.getStore(COOK_BREAKFAST_STORE_NAME).load(String.valueOf(idToDelete));
        if (reportToDelete == null) {
            System.out.println("No report found with ID: " + idToDelete);
            return;
        }

        storageHelper.getStore(COOK_BREAKFAST_STORE_NAME).delete(String.valueOf(idToDelete));
        System.out.println("Report with ID " + idToDelete + " has been successfully deleted.");
    }

    public int getNumReports() throws IOException {
        List<Map<String, Object>> allReadings = storageHelper.getStore(COOK_BREAKFAST_STORE_NAME).loadAll();
        return allReadings.size();
    }

    private void printBreakfastInstructions() {
        StringBuilder instructions = new StringBuilder();

        instructions.append("To Make Bacon:\n")
                .append("1. Heat pan over medium heat.\n")
                .append("2. Place bacon strips in pan.\n")
                .append("3. Cook until crispy, turning occasionally.\n\n");

        instructions.append("To Make Eggs:\n")
                .append("1. Crack eggs into a bowl and whisk.\n")
                .append("2. Heat a pan over medium heat and add butter.\n")
                .append("3. Pour eggs into pan and stir gently until cooked.\n\n");

        instructions.append("To Make Waffles:\n")
                .append("1. Preheat waffle iron.\n")
                .append("2. Pour waffle batter into the center of the iron.\n")
                .append("3. Close iron and cook until golden brown.\n\n");

        instructions.append("To Make Pancakes:\n")
                .append("1. Heat a pan over medium heat and lightly oil.\n")
                .append("2. Pour pancake batter into pan and cook until bubbles form on the surface.\n")
                .append("3. Flip and cook the other side until golden brown.\n\n");

        instructions.append("To Prepare Syrup:\n")
                .append("1. Arrange Syrup in serving dishes.\n\n");

        instructions.append("To Serve Yogurt:\n")
                .append("1. Scoop yogurt into a bowl.\n")
                .append("2. Add toppings if desired.\n\n");

        System.out.println(instructions.toString());
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

    public void makeBreakfast(Scanner scanner) throws IOException {
        System.out.println("=== Complete New Breakfast ===");

        printBreakfastInstructions();

        Menu menu = makeCookBreakfastReportFromInput(scanner);
        if (menu != null) {
            createBreakfastReport(menu);
        }
    }

    public void viewAllReports() throws IOException {
        List<Map<String, Object>> reports = getAllCookingReportsReadings();
        if (reports.isEmpty()) {
            System.out.println("No breakfast reports found.");
            return;
        }

        for (Map<String, Object> report : reports) {
            Menu menu = convertMapToMenu(report);
            System.out.println(menu);
        }
    }
}