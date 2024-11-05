import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TinUseCases {
    private final HotelStorageHelper storage;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private final Hotel hotel;

    public TinUseCases(String dataDirectory) throws IOException {
        this.storage = new HotelStorageHelper(dataDirectory);
    }

    // Use Case 3: Clean Hotel Room
    public void handleRoomCleaning(String roomNumber, String staffId) throws IOException {
        if (!isRoomReadyForCleaning(roomNumber)) {
            throw new IllegalStateException("Room " + roomNumber + " is not ready for cleaning");
        }

        // 1. Create cleaning record
        String cleaningId = "CR_" + roomNumber + "_" + LocalDateTime.now().format(DATE_FORMAT);
        CleaningRecord cleaningRecord = new CleaningRecord();
        cleaningRecord.setRoomNumber(roomNumber);
        cleaningRecord.setStaffId(staffId);
        cleaningRecord.setStartTime(LocalDateTime.now());
        cleaningRecord.setStatus("IN_PROGRESS");

        DataStore<CleaningRecord> cleaningStore = storage.getStore("cleaning_records");
        cleaningStore.saveObject(cleaningId, cleaningRecord);

        // 2. Update room status to cleaning
        updateRoomStatus(roomNumber, "CLEANING");

        // 3. Record inspection results
        Map<String, Object> inspection = new HashMap<>();
        inspection.put("cleaningId", cleaningId);
        inspection.put("damage", new ArrayList<String>());
        inspection.put("maintenanceIssues", new ArrayList<String>());
        inspection.put("missingItems", new ArrayList<String>());
        inspection.put("consumablesNeeded", new ArrayList<String>());
        
        DataStore<Map<String, Object>> inspectionStore = storage.getStore("inspections");
        inspectionStore.save(cleaningId, inspection);
    }

    public void recordInspectionIssues(String cleaningId, Map<String, List<String>> issues)
            throws IOException {
        // Record any maintenance issues
        if (!issues.get("maintenanceIssues").isEmpty()) {
            String maintRequestId = "MR_" + LocalDateTime.now().format(DATE_FORMAT);

            MaintenanceRequest request = new MaintenanceRequest();
            request.setRoomNumber(issues.get("roomNumber").get(0));
            request.setIssues(issues.get("maintenanceIssues"));
            request.setStatus("PENDING");
            request.setPriority("MEDIUM");

            DataStore<MaintenanceRequest> maintenanceStore = storage.getStore("maintenance_requests");
            maintenanceStore.saveObject(maintRequestId, request);

            updateRoomStatus(issues.get("roomNumber").get(0), "MAINTENANCE_REQUIRED");
        }

        // Record lost and found items
        if (!issues.get("foundItems").isEmpty()) {
            String lostFoundId = "LF_" + LocalDateTime.now().format(DATE_FORMAT);

            LostAndFound lostFound = new LostAndFound();
            lostFound.setItems(issues.get("foundItems"));
            lostFound.setRoomNumber(issues.get("roomNumber").get(0));
            lostFound.setFoundDate(LocalDateTime.now());
            lostFound.setStatus("FOUND");

            DataStore<LostAndFound> lostFoundStore = storage.getStore("lost_found");
            lostFoundStore.saveObject(lostFoundId, lostFound);
        }
    }

    public void updateRoomInventory(String roomNumber, Map<String, Integer> supplies)
            throws IOException {
        DataStore<Inventory> inventoryStore = storage.getStore("inventory");

        for (Map.Entry<String, Integer> supply : supplies.entrySet()) {
            Inventory inventory = inventoryStore.loadObject(supply.getKey(), Inventory.class, hotel);

            if (inventory == null) {
                inventory = new Inventory();
                inventory.setItemId(supply.getKey());
                inventory.setQuantity(0L);
            }

            long newQuantity = inventory.getQuantity() - supply.getValue();

            if (newQuantity < 0) {
                requestSupplies(supply.getKey(), Math.abs((int) newQuantity));
                continue;
            }

            inventory.setQuantity(newQuantity);
            inventoryStore.saveObject(supply.getKey(), inventory);
        }
    }

    public void completeRoomCleaning(String cleaningId) throws IOException {
        DataStore<CleaningRecord> cleaningStore = storage.getStore("cleaning_records");
        CleaningRecord cleaningRecord = cleaningStore.loadObject(cleaningId, CleaningRecord.class, hotel);

        cleaningRecord.setEndTime(LocalDateTime.now());
        cleaningRecord.setStatus("COMPLETED");
        cleaningStore.saveObject(cleaningId, cleaningRecord);

        updateRoomStatus(cleaningRecord.getRoomNumber(), "AVAILABLE");
    }

    // Use Case 4: Process Room Service Order
    public String createRoomServiceOrder(String roomNumber, String guestId,
            List<Map<String, Object>> items) throws IOException {
        if (!isGuestCheckedIn(roomNumber, guestId)) {
            throw new IllegalStateException("Guest not checked into room");
        }

        if (!isRoomServiceAvailable()) {
            throw new IllegalStateException("Room service not available at this time");
        }

        String orderId = "RS_" + LocalDateTime.now().format(DATE_FORMAT);

        RoomServiceOrder order = new RoomServiceOrder();
        order.setOrderId(orderId);
        order.setRoomNumber(roomNumber);
        order.setGuestId(guestId);
        order.setItems(items);
        order.setStatus("PENDING");
        order.setOrderTime(LocalDateTime.now());

        DataStore<RoomServiceOrder> orderStore = storage.getStore("room_service_orders");
        orderStore.saveObject(orderId, order);

        return orderId;
    }

    public void updateOrderStatus(String orderId, String status, String notes) throws IOException {
        DataStore<RoomServiceOrder> orderStore = storage.getStore("room_service_orders");
        RoomServiceOrder order = orderStore.loadObject(orderId, RoomServiceOrder.class, hotel);
        
        order.setStatus(status);
        order.setStatusNotes(notes);
        order.setLastUpdated(LocalDateTime.now());
        orderStore.saveObject(orderId, order);
    }

    public void completeRoomServiceDelivery(String orderId, double amount) throws IOException {
        DataStore<RoomServiceOrder> orderStore = storage.getStore("room_service_orders");
        RoomServiceOrder order = orderStore.loadObject(orderId, RoomServiceOrder.class, hotel);

        String chargeId = "CHG_" + LocalDateTime.now().format(DATE_FORMAT);

        Charge charge = new Charge();
        charge.setGuestId(order.getGuestId());
        charge.setRoomNumber(order.getRoomNumber());
        charge.setAmount(amount);
        charge.setType("ROOM_SERVICE");
        charge.setTimestamp(LocalDateTime.now());

        DataStore<Charge> chargeStore = storage.getStore("charges");
        chargeStore.saveObject(chargeId, charge);

        order.setStatus("COMPLETED");
        order.setChargeId(chargeId);
        order.setCompletionTime(LocalDateTime.now());
        orderStore.saveObject(orderId, order);
    }

    // Helper methods
    private boolean isRoomReadyForCleaning(String roomNumber) throws IOException {
        DataStore<Room> roomStore = storage.getStore("rooms");
        Room room = roomStore.loadObject(roomNumber, Room.class, hotel);
        return room != null &&
                ("DIRTY".equals(room.getStatus()) ||
                        "CHECKOUT".equals(room.getStatus()));
    }

    private void updateRoomStatus(String roomNumber, String status) throws IOException {
        DataStore<Room> roomStore = storage.getStore("rooms");
        Room room = roomStore.loadObject(roomNumber, Room.class, hotel);
        room.setStatus(status);
        room.setLastStatusUpdate(LocalDateTime.now());
        roomStore.saveObject(roomNumber, room);
    }

    private void requestSupplies(String itemId, int quantity) throws IOException {
        String requestId = "SR_" + LocalDateTime.now().format(DATE_FORMAT);

        SupplyRequest request = new SupplyRequest();
        request.setItemId(itemId);
        request.setQuantity(quantity);
        request.setStatus("PENDING");
        request.setRequestTime(LocalDateTime.now());

        DataStore<SupplyRequest> supplyStore = storage.getStore("supply_requests");
        supplyStore.saveObject(requestId, request);
    }

    private boolean isGuestCheckedIn(String roomNumber, String guestId) throws IOException {
        DataStore<Room> roomStore = storage.getStore("rooms");
        Room room = roomStore.loadObject(roomNumber, Room.class, hotel);
        return room != null &&
                "OCCUPIED".equals(room.getStatus()) &&
                guestId.equals(room.getCustomer().getId());
    }

    private boolean isRoomServiceAvailable() {
        return true;
    }
}