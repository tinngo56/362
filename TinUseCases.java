import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TinUseCases {
    private final HotelStorageHelper storage;
    private static final DateTimeFormatter DATE_FORMAT = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TinUseCases(String dataDirectory) throws IOException {
        this.storage = new HotelStorageHelper(dataDirectory);
    }

    // Use Case 3: Clean Hotel Room
    public void handleRoomCleaning(String roomNumber, String staffId) throws IOException {
        // Check preconditions
        if (!isRoomReadyForCleaning(roomNumber)) {
            throw new IllegalStateException("Room " + roomNumber + " is not ready for cleaning");
        }

        // 1. Create cleaning record
        String cleaningId = "CR_" + roomNumber + "_" + LocalDateTime.now().format(DATE_FORMAT);
        Map<String, Object> cleaningRecord = new HashMap<>();
        cleaningRecord.put("roomNumber", roomNumber);
        cleaningRecord.put("staffId", staffId);
        cleaningRecord.put("startTime", LocalDateTime.now().format(DATE_FORMAT));
        cleaningRecord.put("status", "IN_PROGRESS");
        storage.getStore("cleaning_records").save(cleaningId, cleaningRecord);

        // 2. Update room status to cleaning
        updateRoomStatus(roomNumber, "CLEANING");

        // 3. Record inspection results
        Map<String, Object> inspection = new HashMap<>();
        inspection.put("cleaningId", cleaningId);
        inspection.put("damage", new ArrayList<String>());
        inspection.put("maintenanceIssues", new ArrayList<String>());
        inspection.put("missingItems", new ArrayList<String>());
        inspection.put("consumablesNeeded", new ArrayList<String>());
        storage.getStore("inspections").save(cleaningId, inspection);
    }

    public void recordInspectionIssues(String cleaningId, Map<String, List<String>> issues) 
            throws IOException {
        // Record any maintenance issues
        if (!issues.get("maintenanceIssues").isEmpty()) {
            String maintRequestId = "MR_" + LocalDateTime.now().format(DATE_FORMAT);
            Map<String, Object> maintenanceRequest = new HashMap<>();
            maintenanceRequest.put("roomNumber", issues.get("roomNumber"));
            maintenanceRequest.put("issues", issues.get("maintenanceIssues"));
            maintenanceRequest.put("status", "PENDING");
            maintenanceRequest.put("priority", "MEDIUM");
            storage.getStore("maintenance_requests").save(maintRequestId, maintenanceRequest);
            
            // Update room status if maintenance is required
            updateRoomStatus(issues.get("roomNumber").get(0), "MAINTENANCE_REQUIRED");
        }

        // Record lost and found items
        if (!issues.get("foundItems").isEmpty()) {
            String lostFoundId = "LF_" + LocalDateTime.now().format(DATE_FORMAT);
            Map<String, Object> lostFound = new HashMap<>();
            lostFound.put("items", issues.get("foundItems"));
            lostFound.put("roomNumber", issues.get("roomNumber"));
            lostFound.put("foundDate", LocalDateTime.now().format(DATE_FORMAT));
            lostFound.put("status", "FOUND");
            storage.getStore("lost_found").save(lostFoundId, lostFound);
        }
    }

    public void updateRoomInventory(String roomNumber, Map<String, Integer> supplies) 
            throws IOException {
        // Update inventory for used supplies
        for (Map.Entry<String, Integer> supply : supplies.entrySet()) {
            Map<String, Object> inventory = storage.getStore("inventory")
                .load(supply.getKey());
            
            if (inventory == null) {
                inventory = new HashMap<>();
                inventory.put("itemId", supply.getKey());
                inventory.put("quantity", 0);
            }
            
            int currentQuantity = (int) inventory.get("quantity");
            int newQuantity = currentQuantity - supply.getValue();
            
            if (newQuantity < 0) {
                // Handle insufficient supplies
                requestSupplies(supply.getKey(), Math.abs(newQuantity));
                continue;
            }
            
            inventory.put("quantity", newQuantity);
            storage.getStore("inventory").save(supply.getKey(), inventory);
        }
    }

    public void completeRoomCleaning(String cleaningId) throws IOException {
        // Load cleaning record
        Map<String, Object> cleaningRecord = storage.getStore("cleaning_records")
            .load(cleaningId);
        String roomNumber = (String) cleaningRecord.get("roomNumber");

        // Update cleaning record
        cleaningRecord.put("endTime", LocalDateTime.now().format(DATE_FORMAT));
        cleaningRecord.put("status", "COMPLETED");
        storage.getStore("cleaning_records").save(cleaningId, cleaningRecord);

        // Update room status
        updateRoomStatus(roomNumber, "AVAILABLE");
    }

    // Use Case 4: Process Room Service Order
    public String createRoomServiceOrder(String roomNumber, String guestId, 
            List<Map<String, Object>> items) throws IOException {
        // Verify preconditions
        if (!isGuestCheckedIn(roomNumber, guestId)) {
            throw new IllegalStateException("Guest not checked into room");
        }

        if (!isRoomServiceAvailable()) {
            throw new IllegalStateException("Room service not available at this time");
        }

        // Create order
        String orderId = "RS_" + LocalDateTime.now().format(DATE_FORMAT);
        Map<String, Object> order = new HashMap<>();
        order.put("orderId", orderId);
        order.put("roomNumber", roomNumber);
        order.put("guestId", guestId);
        order.put("items", items);
        order.put("status", "PENDING");
        order.put("orderTime", LocalDateTime.now().format(DATE_FORMAT));
        
        storage.getStore("room_service_orders").save(orderId, order);
        return orderId;
    }

    public void updateOrderStatus(String orderId, String status, String notes) throws IOException {
        Map<String, Object> order = storage.getStore("room_service_orders").load(orderId);
        order.put("status", status);
        order.put("statusNotes", notes);
        order.put("lastUpdated", LocalDateTime.now().format(DATE_FORMAT));
        storage.getStore("room_service_orders").save(orderId, order);
    }

    public void completeRoomServiceDelivery(String orderId, double amount) throws IOException {
        Map<String, Object> order = storage.getStore("room_service_orders").load(orderId);
        
        // Add charge to guest's room
        String roomNumber = (String) order.get("roomNumber");
        String guestId = (String) order.get("guestId");
        
        Map<String, Object> charge = new HashMap<>();
        charge.put("guestId", guestId);
        charge.put("roomNumber", roomNumber);
        charge.put("amount", amount);
        charge.put("type", "ROOM_SERVICE");
        charge.put("timestamp", LocalDateTime.now().format(DATE_FORMAT));
        
        String chargeId = "CHG_" + LocalDateTime.now().format(DATE_FORMAT);
        storage.getStore("charges").save(chargeId, charge);

        // Update order status
        order.put("status", "COMPLETED");
        order.put("chargeId", chargeId);
        order.put("completionTime", LocalDateTime.now().format(DATE_FORMAT));
        storage.getStore("room_service_orders").save(orderId, order);
    }

    // Helper methods
    private boolean isRoomReadyForCleaning(String roomNumber) throws IOException {
        Map<String, Object> room = storage.getStore("rooms").load(roomNumber);
        return room != null && 
               ("DIRTY".equals(room.get("status")) || 
                "CHECKOUT".equals(room.get("status")));
    }

    private void updateRoomStatus(String roomNumber, String status) throws IOException {
        Map<String, Object> room = storage.getStore("rooms").load(roomNumber);
        room.put("status", status);
        room.put("lastStatusUpdate", LocalDateTime.now().format(DATE_FORMAT));
        storage.getStore("rooms").save(roomNumber, room);
    }

    private void requestSupplies(String itemId, int quantity) throws IOException {
        String requestId = "SR_" + LocalDateTime.now().format(DATE_FORMAT);
        Map<String, Object> request = new HashMap<>();
        request.put("itemId", itemId);
        request.put("quantity", quantity);
        request.put("status", "PENDING");
        request.put("requestTime", LocalDateTime.now().format(DATE_FORMAT));
        storage.getStore("supply_requests").save(requestId, request);
    }

    private boolean isGuestCheckedIn(String roomNumber, String guestId) throws IOException {
        Map<String, Object> room = storage.getStore("rooms").load(roomNumber);
        return room != null && 
               "OCCUPIED".equals(room.get("status")) && 
               guestId.equals(room.get("currentGuest"));
    }

    private boolean isRoomServiceAvailable() {
        // Add logic to check operational hours and staff availability
        return true;
    }

    // Main method with usage examples
    public static void main(String[] args) throws IOException {
        TinUseCases hotel = new TinUseCases("hotel_data");

        // Use Case 3: Clean Hotel Room Example
        try {
            // Start cleaning process
            hotel.handleRoomCleaning("101", "STAFF001");

            // Record inspection issues
            Map<String, List<String>> issues = new HashMap<>();
            issues.put("maintenanceIssues", Arrays.asList("Loose doorknob"));
            issues.put("foundItems", Arrays.asList("Watch", "Charger"));
            issues.put("roomNumber", Arrays.asList("101"));
            hotel.recordInspectionIssues("CR_101_2024-11-04 10:30:00", issues);

            // Update inventory
            Map<String, Integer> supplies = new HashMap<>();
            supplies.put("towels", 2);
            supplies.put("toiletries", 1);
            hotel.updateRoomInventory("101", supplies);

            // Complete cleaning
            hotel.completeRoomCleaning("CR_101_2024-11-04 10:30:00");
        } catch (IllegalStateException e) {
            System.out.println("Cleaning error: " + e.getMessage());
        }

        // Use Case 4: Room Service Order Example
        try {
            // Create order
            List<Map<String, Object>> items = new ArrayList<>();
            Map<String, Object> item1 = new HashMap<>();
            item1.put("name", "Burger");
            item1.put("quantity", 1);
            item1.put("price", 15.99);
            items.add(item1);

            String orderId = hotel.createRoomServiceOrder("101", "G001", items);

            // Update status during preparation
            hotel.updateOrderStatus(orderId, "PREPARING", "In kitchen");

            // Complete delivery and charge
            hotel.completeRoomServiceDelivery(orderId, 15.99);
        } catch (IllegalStateException e) {
            System.out.println("Room service error: " + e.getMessage());
        }
    }
}