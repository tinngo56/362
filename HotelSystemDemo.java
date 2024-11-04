import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HotelSystemDemo {
    private final HotelStorageHelper storage;
    private static final DateTimeFormatter DATE_FORMAT = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HotelSystemDemo(String dataDirectory) throws IOException {
        this.storage = new HotelStorageHelper(dataDirectory);
    }

    public void setupInitialData() throws IOException {
        // Setup Rooms
        setupRooms();
        
        // Setup Inventory
        setupInventory();
        
        // Setup Staff
        setupStaff();
        
        // Setup Guests
        setupGuests();
        
        // Setup Room Service Menu
        setupRoomServiceMenu();
        
        System.out.println("Initial setup completed successfully.");
    }

    private void setupRooms() throws IOException {
        // Create different room types
        createRoom("101", "STANDARD", 150.00, "DIRTY");
        createRoom("102", "STANDARD", 150.00, "AVAILABLE");
        createRoom("201", "DELUXE", 250.00, "OCCUPIED");
        createRoom("202", "DELUXE", 250.00, "AVAILABLE");
        createRoom("301", "SUITE", 350.00, "AVAILABLE");
    }

    private void createRoom(String number, String type, double price, String status) 
            throws IOException {
        Map<String, Object> room = new HashMap<>();
        room.put("number", number);
        room.put("type", type);
        room.put("status", status);
        room.put("price", price);
        room.put("lastCleaned", status.equals("AVAILABLE") ? 
            LocalDateTime.now().format(DATE_FORMAT) : "");
        storage.getStore("rooms").save(number, room);
    }

    private void setupInventory() throws IOException {
        // Setup cleaning supplies
        createInventoryItem("towels", 100);
        createInventoryItem("toiletries", 200);
        createInventoryItem("bedsheets", 150);
        createInventoryItem("cleaning_supplies", 50);
        
        // Setup room service supplies
        createInventoryItem("burger_ingredients", 50);
        createInventoryItem("beverages", 100);
        createInventoryItem("room_service_supplies", 75);
    }

    private void createInventoryItem(String itemId, int quantity) throws IOException {
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

    private void setupGuests() throws IOException {
        // Create a checked-in guest in room 201
        Map<String, Object> guest = new HashMap<>();
        guest.put("id", "G001");
        guest.put("name", "John Doe");
        guest.put("email", "john@example.com");
        guest.put("phone", "555-0123");
        guest.put("status", "CHECKED_IN");
        guest.put("roomNumber", "201");
        storage.getStore("guests").save("G001", guest);
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

    public void runCleaningUseCase() throws IOException {
        System.out.println("\nRunning Cleaning Use Case...");
        
        TinUseCases hotel = new TinUseCases("hotel_data");
        String roomNumber = "101";
        String staffId = "CS001";

        // 1. Start cleaning process
        System.out.println("1. Starting cleaning process for room " + roomNumber);
        hotel.handleRoomCleaning(roomNumber, staffId);

        // 2. Record inspection issues
        System.out.println("2. Recording inspection results");
        Map<String, List<String>> issues = new HashMap<>();
        issues.put("maintenanceIssues", Arrays.asList("Minor wall scuff"));
        issues.put("foundItems", Arrays.asList("Phone charger"));
        issues.put("roomNumber", Arrays.asList(roomNumber));
        hotel.recordInspectionIssues("CR_" + roomNumber + "_" + 
            LocalDateTime.now().format(DATE_FORMAT), issues);

        // 3. Update inventory for used supplies
        System.out.println("3. Updating inventory");
        Map<String, Integer> supplies = new HashMap<>();
        supplies.put("towels", 2);
        supplies.put("toiletries", 1);
        supplies.put("bedsheets", 1);
        hotel.updateRoomInventory(roomNumber, supplies);

        // 4. Complete cleaning
        System.out.println("4. Completing room cleaning");
        hotel.completeRoomCleaning("CR_" + roomNumber + "_" + 
            LocalDateTime.now().format(DATE_FORMAT));

        // 5. Verify room status
        Map<String, Object> room = storage.getStore("rooms").load(roomNumber);
        System.out.println("5. Final room status: " + room.get("status"));
    }

    public void runRoomServiceUseCase() throws IOException {
        System.out.println("\nRunning Room Service Use Case...");
        
        TinUseCases hotel = new TinUseCases("hotel_data");
        String roomNumber = "201"; // Using the occupied room
        String guestId = "G001";

        // 1. Create room service order
        System.out.println("1. Creating room service order");
        List<Map<String, Object>> items = new ArrayList<>();
        Map<String, Object> item1 = new HashMap<>();
        item1.put("name", "Burger");
        item1.put("quantity", 1);
        item1.put("price", 15.99);
        items.add(item1);

        String orderId = hotel.createRoomServiceOrder(roomNumber, guestId, items);
        System.out.println("   Order created: " + orderId);

        // 2. Update order status during preparation
        System.out.println("2. Updating order status to PREPARING");
        hotel.updateOrderStatus(orderId, "PREPARING", "In kitchen");

        // 3. Complete delivery and charge
        System.out.println("3. Completing delivery and charging room");
        hotel.completeRoomServiceDelivery(orderId, 15.99);

        // 4. Verify order status
        Map<String, Object> order = storage.getStore("room_service_orders").load(orderId);
        System.out.println("4. Final order status: " + order.get("status"));
    }

    public static void main(String[] args) throws IOException {
        HotelSystemDemo demo = new HotelSystemDemo("hotel_data");

        // Setup initial data
        System.out.println("Setting up initial data...");
        demo.setupInitialData();

        // Run use cases
        demo.runCleaningUseCase();
        demo.runRoomServiceUseCase();
    }
}