package Controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import Models.*;
import Models.RoomServiceOrder.OrderStatus;
import Storage.StorageHelper;

public class KitchenController {
    private static final String INVENTORY_STORE = "inventory";
    private static final String DATA_ORDER_NAME = "room_service_orders";
    private static final double MIN_INVENTORY_THRESHOLD = 5.0;

    private final StorageHelper inventoryStorageHelper;
    private final StorageHelper orderStorageHelper;

    public KitchenController(String baseDirectory) throws IOException {
        this.inventoryStorageHelper = new StorageHelper(baseDirectory, INVENTORY_STORE);
        this.orderStorageHelper = new StorageHelper(baseDirectory, DATA_ORDER_NAME);
    }

    public boolean prepareOrder(RoomServiceOrder order, Scanner scanner) throws IOException {
        String orderId = order.getOrderId();

        System.out.println("Kitchen available to prepare order? (yes/no):");
        if (!scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            order.setNotes("Kitchen currently unavailable");
            updateOrderStatus(orderId, OrderStatus.CANCELLED);
            return false;
        }

        if (!checkInventory(order.getItems())) {
            reportMissingIngredients(orderId);
            return false;
        }

        updateInventory(order.getItems());
        System.out.println("Order preparation started.");
        order.setStatus(OrderStatus.IN_PREPARATION);
        orderStorageHelper.getStore(DATA_ORDER_NAME).save(orderId, order.toMap());

        System.out.println("Order " + orderId + " should be ready. Please check.");
        updateOrderStatus(orderId, OrderStatus.READY_FOR_DELIVERY);
        return true;
    }

    private RoomServiceOrder getOrder(String orderId) throws IOException {
        Map<String, Object> orderData = orderStorageHelper.getStore(DATA_ORDER_NAME).load(orderId);
        return new RoomServiceOrder().fromMap(orderData);
    }

    private void updateOrderStatus(String orderId, OrderStatus status) throws IOException {
        RoomServiceOrder order = getOrder(orderId);
        order.setStatus(status);
        orderStorageHelper.getStore(DATA_ORDER_NAME).save(orderId, order.toMap());
    }

    public boolean checkInventory(List<MenuItem> items) throws IOException {
        Map<String, Object> inventoryData = inventoryStorageHelper.getStore(INVENTORY_STORE).load("current");
        if (inventoryData == null) {
            throw new IOException("Inventory data not found");
        }

        KitchenInventory inventory = new KitchenInventory().fromMap(inventoryData);
        Map<String, Double> required = new HashMap<>();

        for (MenuItem item : items) {
            String category = item.getCategory().toLowerCase();
            required.merge(category, 1.0, Double::sum);
        }

        for (Map.Entry<String, Double> entry : required.entrySet()) {
            Double available = inventory.getIngredients().get(entry.getKey());
            if (available == null || available < entry.getValue()) {
                return false;
            }
        }

        return true;
    }

    private void updateInventory(List<MenuItem> items) throws IOException {
        Map<String, Object> inventoryData = inventoryStorageHelper.getStore(INVENTORY_STORE).load("current");
        KitchenInventory inventory = new KitchenInventory().fromMap(inventoryData);

        for (MenuItem item : items) {
            String category = item.getCategory().toLowerCase();
            double current = inventory.getIngredients().getOrDefault(category, 0.0);
            inventory.getIngredients().put(category, current - 1.0);

            if (current - 1.0 < MIN_INVENTORY_THRESHOLD) {
                System.out.printf("WARNING: Low stock for %s: %.1f items remaining%n",
                        category, current - 1.0);
            }
        }

        inventoryData = inventory.toMap();
        inventoryData.put("lastUpdated", LocalDateTime.now().toString());
        inventoryStorageHelper.getStore(INVENTORY_STORE).save("current", inventoryData);
    }

    public void reportMissingIngredients(String orderId) throws IOException {
        RoomServiceOrder order = getOrder(orderId);
        Set<String> missingCategories = new HashSet<>();

        for (MenuItem item : order.getItems()) {
            missingCategories.add(item.getCategory().toLowerCase());
        }

        order.setNotes("Missing items from categories: " + String.join(", ", missingCategories));
        updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }
}
