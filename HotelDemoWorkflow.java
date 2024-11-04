import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HotelDemoWorkflow {
    private final HotelStorageHelper storage;
    private final TinUseCases hotel;
    private static final DateTimeFormatter DATE_FORMAT = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public HotelDemoWorkflow(String dataDirectory) throws IOException {
        this.storage = new HotelStorageHelper(dataDirectory);
        this.hotel = new TinUseCases(dataDirectory);
    }

    public void setupInitialData() throws IOException {
        // Setup Rooms - all AVAILABLE initially
        setupRooms();
        
        // Setup Inventory
        setupInventory();
        
        // Setup Staff
        setupStaff();
        
        // Setup Room Service Menu
        setupRoomServiceMenu();
        
        System.out.println("Initial setup completed successfully.");
    }

    private void setupRooms() throws IOException {
        // All rooms start as AVAILABLE
        createRoom("101", "STANDARD", 150.00);
        createRoom("102", "STANDARD", 150.00);
        createRoom("201", "DELUXE", 250.00);
        createRoom("202", "DELUXE", 250.00);
        createRoom("301", "SUITE", 350.00);
    }

    private void createRoom(String number, String type, double price) 
            throws IOException {
        Map<String, Object> room = new HashMap<>();
        room.put("number", number);
        room.put("type", type);
        room.put("status", "AVAILABLE");
        room.put("price", price);
        room.put("currentGuest", null);
        room.put("lastCleaned", LocalDateTime.now().format(DATE_FORMAT));
        storage.getStore("rooms").save(number, room);
    }

    private void setupInventory() throws IOException {
        createInventoryItem("towels", 100L);
        createInventoryItem("toiletries", 200L);
        createInventoryItem("bedsheets", 150L);
        createInventoryItem("cleaning_supplies", 50L);
        createInventoryItem("burger_ingredients", 50L);
        createInventoryItem("beverages", 100L);
    }

    private void createInventoryItem(String itemId, Long quantity) throws IOException {
        Map<String, Object> item = new HashMap<>();
        item.put("itemId", itemId);
        item.put("quantity", quantity);
        item.put("lastRestocked", LocalDateTime.now().format(DATE_FORMAT));
        storage.getStore("inventory").save(itemId, item);
    }

    private void setupStaff() throws IOException {
        createStaffMember("CS001", "Alice Johnson", "CLEANING");
        createStaffMember("CS002", "Bob Smith", "CLEANING");
        createStaffMember("RS001", "Carol White", "ROOM_SERVICE");
        createStaffMember("RS002", "David Brown", "ROOM_SERVICE");
    }

    private void createStaffMember(String id, String name, String role) throws IOException {
        Map<String, Object> staff = new HashMap<>();
        staff.put("id", id);
        staff.put("name", name);
        staff.put("role", role);
        staff.put("status", "ACTIVE");
        storage.getStore("staff").save(id, staff);
    }

    private void setupRoomServiceMenu() throws IOException {
        List<Map<String, Object>> menuItems = new ArrayList<>();
        createMenuItem(menuItems, "Burger", 15.99, "Main Course");
        createMenuItem(menuItems, "Caesar Salad", 12.99, "Starter");
        createMenuItem(menuItems, "Chocolate Cake", 8.99, "Dessert");
        createMenuItem(menuItems, "Soft Drinks", 3.99, "Beverages");
        
        Map<String, Object> menu = new HashMap<>();
        menu.put("items", menuItems);
        menu.put("lastUpdated", LocalDateTime.now().format(DATE_FORMAT));
        storage.getStore("room_service_menu").save("current_menu", menu);
    }

    private void createMenuItem(List<Map<String, Object>> menuItems, 
            String name, double price, String category) {
        Map<String, Object> item = new HashMap<>();
        item.put("name", name);
        item.put("price", price);
        item.put("category", category);
        item.put("available", true);
        menuItems.add(item);
    }

    public void runFullDemo() throws IOException {
        System.out.println("\n=== Starting Hotel System Demo ===\n");

        // 1. Simulate guest check-in
        System.out.println("1. Checking in guest to room 201...");
        checkInGuest("G001", "John Doe", "201");
        System.out.println("   Guest checked in successfully\n");

        // 2. Demonstrate room cleaning for room 101
        System.out.println("2. Making room 101 dirty for cleaning demo...");
        updateRoomStatus("101", "DIRTY");
        
        System.out.println("3. Running room cleaning process...");
        runCleaningDemo("101");
        System.out.println("   Room cleaning completed\n");

        // 3. Demonstrate room service
        System.out.println("4. Processing room service order...");
        runRoomServiceDemo("201", "G001");
        System.out.println("   Room service order completed\n");

        System.out.println("=== Demo Completed Successfully ===");
    }

    private void checkInGuest(String guestId, String name, String roomNumber) throws IOException {
        // Create guest record
        Map<String, Object> guest = new HashMap<>();
        guest.put("id", guestId);
        guest.put("name", name);
        guest.put("status", "CHECKED_IN");
        guest.put("roomNumber", roomNumber);
        storage.getStore("guests").save(guestId, guest);

        // Update room status
        Map<String, Object> room = storage.getStore("rooms").load(roomNumber);
        room.put("status", "OCCUPIED");
        room.put("currentGuest", guestId);
        storage.getStore("rooms").save(roomNumber, room);
    }

    private void updateRoomStatus(String roomNumber, String status) throws IOException {
        Map<String, Object> room = storage.getStore("rooms").load(roomNumber);
        room.put("status", status);
        storage.getStore("rooms").save(roomNumber, room);
    }

    private void runCleaningDemo(String roomNumber) throws IOException {
        // Start cleaning process
        hotel.handleRoomCleaning(roomNumber, "CS001");

        // Record inspection issues
        Map<String, List<String>> issues = new HashMap<>();
        issues.put("maintenanceIssues", Arrays.asList("Minor wall scuff"));
        issues.put("foundItems", Arrays.asList("Phone charger"));
        issues.put("roomNumber", Arrays.asList(roomNumber));
        hotel.recordInspectionIssues("CR_" + roomNumber + "_" + 
            LocalDateTime.now().format(DATE_FORMAT), issues);

        // Update inventory
        Map<String, Integer> supplies = new HashMap<>();
        supplies.put("towels", 2);
        supplies.put("toiletries", 1);
        supplies.put("bedsheets", 1);
        hotel.updateRoomInventory(roomNumber, supplies);

        // Complete cleaning
        hotel.completeRoomCleaning("CR_" + roomNumber + "_" + 
            LocalDateTime.now().format(DATE_FORMAT));
    }

    private void runRoomServiceDemo(String roomNumber, String guestId) throws IOException {
        // Create order
        List<Map<String, Object>> items = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("name", "Burger");
        item.put("quantity", 1);
        item.put("price", 15.99);
        items.add(item);

        String orderId = hotel.createRoomServiceOrder(roomNumber, guestId, items);
        hotel.updateOrderStatus(orderId, "PREPARING", "In kitchen");
        hotel.completeRoomServiceDelivery(orderId, 15.99);
    }

    public static void main(String[] args) throws IOException {
        HotelDemoWorkflow demo = new HotelDemoWorkflow("hotel_data");
        
        // Setup clean initial state
        demo.setupInitialData();
        
        // Run the full demo workflow
        demo.runFullDemo();
    }
}