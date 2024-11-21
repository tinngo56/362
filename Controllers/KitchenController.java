package Controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import Models.*;
import Models.RoomServiceOrder.OrderStatus;
import Storage.StorageHelper;

public class KitchenController {
    private final StorageHelper inventoryStorageHelper;
    private final StorageHelper orderStorageHelper;
    private final RoomServiceController roomServiceController;
    
    private static final String INVENTORY_STORE = "inventory";
    private static final String ORDER_STORE = "orders";
    private static final double MIN_INVENTORY_THRESHOLD = 5.0;
    
    public KitchenController(String baseDirectory) throws IOException {
        this.inventoryStorageHelper = new StorageHelper(baseDirectory, INVENTORY_STORE);
        this.orderStorageHelper = new StorageHelper(baseDirectory, ORDER_STORE);
        this.roomServiceController = new RoomServiceController(baseDirectory);
    }
    
    public boolean prepareOrder(String orderId, Scanner scanner) throws IOException {
        RoomServiceOrder order = roomServiceController.getOrder(orderId);
        
        // Check if kitchen is accepting orders
        System.out.println("Kitchen available to prepare order? (yes/no):");
        if (!scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            order.setNotes("Kitchen currently unavailable");
            roomServiceController.updateOrderStatus(orderId, OrderStatus.CANCELLED);
            return false;
        }
        
        // Estimate preparation time
        System.out.println("Estimated preparation time (minutes):");
        int prepTime = Integer.parseInt(scanner.nextLine().trim());
        order.setEstimatedPrepTime(prepTime);
        
        // Start preparation
        if (!checkInventory(order.getItems())) {
            reportMissingIngredients(orderId);
            return false;
        }
        
        // Update inventory and begin preparation
        updateInventory(order.getItems());
        System.out.println("Order preparation started. Estimated completion in " + prepTime + " minutes.");
        
        // Schedule completion check
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    System.out.println("Order " + orderId + " should be ready. Please check.");
                    roomServiceController.updateOrderStatus(orderId, OrderStatus.READY_FOR_DELIVERY);
                } catch (IOException e) {
                    System.err.println("Failed to update order status: " + e.getMessage());
                }
            }
        }, prepTime * 60 * 1000);
        
        return true;
    }
    
    public boolean checkInventory(List<MenuItem> items) throws IOException {
        Map<String, Integer> required = new HashMap<>();
        
        // Calculate total required ingredients
        for (MenuItem item : items) {
            String itemCategory = item.getCategory().toLowerCase();
            required.merge(itemCategory, 1, Integer::sum);
        }
        
        // Check against inventory
        Map<String, Object> inventoryData = inventoryStorageHelper.getStore(INVENTORY_STORE).load("current");
        if (inventoryData == null) return false;
        
        Map<String, Integer> available = (Map<String, Integer>) inventoryData.get("stock");
        return required.entrySet().stream()
            .allMatch(entry -> available.getOrDefault(entry.getKey(), 0) >= entry.getValue());
    }
    
    private void updateInventory(List<MenuItem> items) throws IOException {
        Map<String, Object> inventoryData = inventoryStorageHelper.getStore(INVENTORY_STORE).load("current");
        Map<String, Integer> stock = (Map<String, Integer>) inventoryData.get("stock");
        
        // Update stock levels
        for (MenuItem item : items) {
            String category = item.getCategory().toLowerCase();
            stock.merge(category, -1, Integer::sum);
        }
        
        // Check for low stock
        checkLowStock(stock);
        
        // Save updated inventory
        inventoryData.put("stock", stock);
        inventoryData.put("lastUpdated", LocalDateTime.now().toString());
        inventoryStorageHelper.getStore(INVENTORY_STORE).save("current", inventoryData);
    }
    
    private void checkLowStock(Map<String, Integer> stock) {
        stock.forEach((category, quantity) -> {
            if (quantity < MIN_INVENTORY_THRESHOLD) {
                System.out.printf("WARNING: Low stock for %s: %d items remaining%n", 
                    category, quantity);
            }
        });
    }
    
    public void reportMissingIngredients(String orderId) throws IOException {
        RoomServiceOrder order = roomServiceController.getOrder(orderId);
        Set<String> missingCategories = new HashSet<>();
        
        for (MenuItem item : order.getItems()) {
            missingCategories.add(item.getCategory().toLowerCase());
        }
        
        order.setNotes("Missing items from categories: " + String.join(", ", missingCategories));
        roomServiceController.updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }
}