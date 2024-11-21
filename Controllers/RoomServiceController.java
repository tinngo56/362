package Controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import Models.*;
import Models.Charge.ChargeType;
import Models.RoomServiceOrder.OrderStatus;
import Storage.StorageHelper;

public class RoomServiceController {
    private static final String DATA_ORDER_NAME = "room_service_orders";
    private static final String DATA_CHARGE_NAME = "charges";
    private final StorageHelper orderStorageHelper;
    private final StorageHelper menuStorageHelper;
    private final StorageHelper chargeStorageHelper;

    public RoomServiceController(String baseDirectory) throws IOException {
        this.orderStorageHelper = new StorageHelper(baseDirectory, DATA_ORDER_NAME);
        this.menuStorageHelper = new StorageHelper(baseDirectory, "room_service_menu");
        this.chargeStorageHelper = new StorageHelper(baseDirectory, DATA_CHARGE_NAME);
    }

    @SuppressWarnings("unchecked")
    public RoomServiceOrder createOrder(int roomNumber, Scanner scanner) throws IOException {
        Map<String, Object> menuData = menuStorageHelper.getStore("room_service_menu").load("current_menu");
        List<MenuItem> menuItems = ((List<Map<String, Object>>) menuData.get("items")).stream()
                .map(item -> new MenuItem().fromMap(item))
                .collect(Collectors.toList());

        List<MenuItem> selectedItems = new ArrayList<>();
        while (true) {
            printMenu();
            System.out.println("\nEnter item name to order (or 'done' to finish): ");
            String choice = scanner.nextLine().trim();

            if (choice.equalsIgnoreCase("done")) {
                if (!selectedItems.isEmpty())
                    break;
                System.out.println("Please select at least one item.");
                continue;
            }

            handleItemSelection(choice, scanner, menuItems, selectedItems);
        }

        RoomServiceOrder order = createOrderFromItems(roomNumber, selectedItems, scanner);
        // Create and save charge
        Charge charge = new Charge(
            order.getTotalAmount(),
            String.valueOf(roomNumber),
            ChargeType.ROOM_SERVICE,
            "G" + String.format("%03d", roomNumber)
        );
        chargeStorageHelper.getStore(DATA_CHARGE_NAME)
            .save(roomNumber + "_" + order.getOrderId(), charge.toMap());
        
        printOrderSummary(selectedItems, order.getTotalAmount());
        orderStorageHelper.getStore(DATA_ORDER_NAME)
            .save(roomNumber + "_" + order.getOrderId(), order.toMap());
            
        return order;
    }

    private void handleItemSelection(String choice, Scanner scanner, List<MenuItem> menuItems,
            List<MenuItem> selectedItems) {
        MenuItem selectedItem = menuItems.stream()
                .filter(item -> item.getName().equalsIgnoreCase(choice))
                .findFirst()
                .orElse(null);

        if (selectedItem != null) {
            System.out.println("Enter quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine().trim());

            for (int i = 0; i < quantity; i++) {
                selectedItems.add(selectedItem);
            }
            System.out.printf("Added %dx %s to your order%n", quantity, selectedItem.getName());
        } else {
            System.out.println("Item not found. Please try again.");
        }
    }

    private RoomServiceOrder createOrderFromItems(int roomNumber, List<MenuItem> items, Scanner scanner) {
        RoomServiceOrder order = new RoomServiceOrder();
        order.setOrderId(UUID.randomUUID().toString());
        order.setRoomNumber(roomNumber);
        order.setItems(items);
        order.setStatus(OrderStatus.RECEIVED);
        order.setOrderTime(LocalDateTime.now());
        order.setTotalAmount(items.stream().mapToDouble(MenuItem::getPrice).sum());

        System.out.println("Any special instructions? (press Enter to skip):");
        String instructions = scanner.nextLine().trim();
        if (!instructions.isEmpty()) {
            order.setNotes(instructions);
        }

        return order;
    }

    private void printOrderSummary(List<MenuItem> items, double total) {
        System.out.println("\nOrder Summary:");
        items.stream()
                .collect(Collectors.groupingBy(MenuItem::getName, Collectors.counting()))
                .forEach((name, count) -> {
                    MenuItem item = items.stream()
                            .filter(i -> i.getName().equals(name))
                            .findFirst()
                            .get();
                    System.out.printf("%dx %-30s $%.2f%n", count, name, item.getPrice() * count);
                });
        System.out.printf("Total: $%.2f%n", total);
    }

    @SuppressWarnings("unchecked")
    public void printMenu() throws IOException {
        Map<String, Object> menuData = menuStorageHelper.getStore("room_service_menu").load("current_menu");
        if (menuData != null) {
            List<MenuItem> menuItems = ((List<Map<String, Object>>) menuData.get("items")).stream()
                    .map(item -> new MenuItem().fromMap(item))
                    .collect(Collectors.toList());

            menuItems.stream()
                    .collect(Collectors.groupingBy(MenuItem::getCategory))
                    .forEach((category, categoryItems) -> {
                        System.out.println("\n=== " + category.toUpperCase() + " ===");
                        categoryItems.forEach(item -> {
                            if (item.isAvailable()) {
                                System.out.printf("%-30s $%.2f%n", item.getName(), item.getPrice());
                            }
                        });
                    });
        }
    }
}
