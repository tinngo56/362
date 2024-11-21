package Controllers;

import Models.*;
import Storage.StorageHelper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class CleaningStaffController {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private StorageHelper cleaningStaffStorageHelper;
    private StorageHelper roomStorageHelper;
    private StorageHelper inspectionStorageHelper;
    private StorageHelper inventoryStorageHelper;
    private StorageHelper maintenanceStorageHelper;
    private StorageHelper lostAndFoundStorageHelper;
    private StorageHelper supplyRequestStorageHelper;

    private final String DATA_STAFF_NAME = "cleaning_staff";
    private final String DATA_ROOM_NAME = "rooms";
    private final String DATA_INSPECTION_NAME = "inspections";
    private final String DATA_INVENTORY_NAME = "inventory";
    private final String DATA_MAINTENANCE_NAME = "maintenance";
    private final String DATA_LOST_FOUND_NAME = "lost_found";
    private final String DATA_SUPPLY_REQUEST = "supply_requests";

    private final String STATUS_AVAILABLE = "AVAILABLE";
    private final String STATUS_CLEANING = "CLEANING";
    private final String STATUS_MAINTENANCE = "MAINTENANCE_REQUIRED";
    private final String STATUS_CLEAN = "CLEAN";
    private final String STATUS_NEEDS_SUPPLIES = "NEEDS_SUPPLIES";

    private CleaningStaff cleaningStaff;

    public CleaningStaffController(String baseDirectory) throws IOException {
        cleaningStaffStorageHelper = new StorageHelper(baseDirectory, DATA_STAFF_NAME);
        roomStorageHelper = new StorageHelper(baseDirectory, DATA_ROOM_NAME);
        inspectionStorageHelper = new StorageHelper(baseDirectory, DATA_INSPECTION_NAME);
        inventoryStorageHelper = new StorageHelper(baseDirectory, DATA_INVENTORY_NAME);
        maintenanceStorageHelper = new StorageHelper(baseDirectory, DATA_MAINTENANCE_NAME);
        lostAndFoundStorageHelper = new StorageHelper(baseDirectory, DATA_LOST_FOUND_NAME);
        supplyRequestStorageHelper = new StorageHelper(baseDirectory, DATA_SUPPLY_REQUEST);

    }

    public CleaningStaff getAvailableCleaningStaff() throws IOException {
        List<Map<String, Object>> list_of_cleaning_staff = cleaningStaffStorageHelper.getStore(DATA_STAFF_NAME)
                .loadAll();
        List<Map<String, Object>> availableStaff = list_of_cleaning_staff.stream()
                .filter(staff -> staff.get("status").equals(STATUS_AVAILABLE))
                .collect(Collectors.toList());
        if (availableStaff.isEmpty()) {

            throw new IOException("No available cleaning staff");
        }
        return cleaningStaff = (CleaningStaff) new CleaningStaff().fromMap(availableStaff.get(0));
    }

    public void cleanRoom(String roomNumber, CleaningStaff cleaningStaff, Scanner scanner) throws IOException {
        try {
            // Update staff status to busy
            updateStaffStatus(cleaningStaff, STATUS_CLEANING);

            // Perform initial inspection
            RoomInspection inspection = performInspection(roomNumber, cleaningStaff, scanner);

            // Handle any maintenance issues if found
            if (inspection.isNeedsMaintenance()) {
                handleMaintenanceIssues(roomNumber, inspection, cleaningStaff);
            }

            // Handle any lost and found items
            handleLostAndFound(roomNumber, inspection, cleaningStaff);

            // Perform cleaning and update inventory
            performCleaning(roomNumber, cleaningStaff);
            updateRoomInventory(roomNumber, cleaningStaff);

            // Perform final inspection
            if (!inspection.isNeedsMaintenance()) {
                // Update room status to clean
                updateRoomStatus(roomNumber, STATUS_CLEAN, cleaningStaff);
            }

        } finally {
            // Always set staff back to available
            updateStaffStatus(cleaningStaff, STATUS_AVAILABLE);
        }
    }

    private RoomInspection performInspection(String roomNumber, CleaningStaff staff, Scanner scanner)
            throws IOException {
        RoomInspection inspection = new RoomInspection();
        inspection.setRoomNumber(roomNumber);
        inspection.setInspectedBy(staff.getName());
        inspection.setInspectionTime(LocalDateTime.now());

        System.out.println("Is there any damage in the room? (yes/no): ");
        String hasDamage = scanner.nextLine();
        if (hasDamage.equalsIgnoreCase("yes")) {
            inspection.setHasDamage(true);
            System.out.println("Please describe the damage: ");
            inspection.setDamageDetails(scanner.nextLine());
        }

        System.out.println("Are there any maintenance issues? (yes/no): ");
        String needsMaintenance = scanner.nextLine();
        if (needsMaintenance.equalsIgnoreCase("yes")) {
            inspection.setNeedsMaintenance(true);
            System.out.println("Please describe the maintenance issues: ");
            inspection.setMaintenanceIssues(scanner.nextLine());
            System.out.println("Please enter the Priority: ");
            inspection.setInspectedBy(scanner.nextLine());
        }

        System.out.println("Are there any lost and found items? (yes/no): ");
        String hasLostAndFound = scanner.nextLine();
        if (hasLostAndFound.equalsIgnoreCase("yes")) {
            // while loop to get multiple items
            System.out.println("Please list the items: (separated by commas)");
            String items = scanner.nextLine();
            inspection.setLostAndFoundItems(Arrays.asList(items.split(",")));
        }

        // Save inspection results
        inspectionStorageHelper.getStore(DATA_INSPECTION_NAME)
                .save(roomNumber + "_" + System.currentTimeMillis(), inspection.toMap());

        return inspection;
    }

    private void handleMaintenanceIssues(String roomNumber, RoomInspection inspection, CleaningStaff staff)
            throws IOException {
        // Create maintenance request
        MaintenanceRequest request = new MaintenanceRequest();
        request.setRequestId(roomNumber + "_" + System.currentTimeMillis());
        request.setRequestId(UUID.randomUUID().toString());
        request.setRoomNumber(roomNumber);
        request.setIssueDescription(String.format("Maintenance Issues: %s%nDamage Details: %s",
                inspection.getMaintenanceIssues(), inspection.getDamageDetails()));
        request.setReportedBy(staff.getName());

        // Save maintenance request
        maintenanceStorageHelper.getStore(DATA_MAINTENANCE_NAME)
                .save(request.getRequestId(), request.toMap());

        // Update room status
        updateRoomStatus(roomNumber, STATUS_MAINTENANCE, staff);
    }

    private void handleLostAndFound(String roomNumber, RoomInspection inspection, CleaningStaff staff)
            throws IOException {
        if (inspection.getMissingItems() != null && !inspection.getMissingItems().isEmpty()) {
            for (String item : inspection.getMissingItems()) {
                LostAndFoundItem lostItem = new LostAndFoundItem();
                lostItem.setItemId(UUID.randomUUID().toString());
                lostItem.setRoomNumber(roomNumber);
                lostItem.setDescription(item);
                lostItem.setFoundBy(staff.getName());

                // Save lost and found item
                lostAndFoundStorageHelper.getStore(DATA_LOST_FOUND_NAME)
                        .save(roomNumber + "_" + lostItem.getItemId(), lostItem.toMap());
            }
        }
    }

    private void performCleaning(String roomNumber, CleaningStaff staff) throws IOException {
        Map<String, Object> room = roomStorageHelper.getStore(DATA_ROOM_NAME).load(roomNumber);
        Map<String, Object> inventoryData = inventoryStorageHelper.getStore(DATA_INVENTORY_NAME).load("room_inventory");
        Inventory mainInventory = new Inventory().fromMap(inventoryData);

        if (mainInventory.getLinenQuantity("towels") < 1 ||
                mainInventory.getLinenQuantity("bed_sheets") < 1 ||
                mainInventory.getLinenQuantity("pillowcases") < 1 ||
                mainInventory.getConsumableQuantity("soap") < 1 ||
                mainInventory.getConsumableQuantity("shampoo") < 1 ||
                mainInventory.getConsumableQuantity("toilet_paper") < 1) {
            room.put("status", STATUS_NEEDS_SUPPLIES);
        } else {
            room.put("status", STATUS_CLEANING);
        }
        room.put("cleanedBy", staff.getName());
        roomStorageHelper.getStore(DATA_ROOM_NAME).save(roomNumber, room);
    }

    private void updateRoomInventory(String roomNumber, CleaningStaff staff) throws IOException {
        // Load room data
        Map<String, Object> roomData = roomStorageHelper.getStore(DATA_ROOM_NAME).load(roomNumber);
        Map<String, Object> room = new Room().fromMap(roomData).toMap();

        // Load main inventory data and convert to Inventory object
        Map<String, Object> inventoryData = inventoryStorageHelper.getStore(DATA_INVENTORY_NAME).load("room_inventory");
        Inventory mainInventory = new Inventory().fromMap(inventoryData);

        // Prepare new quantities for room
        Map<String, Double> roomConsumables = (Map<String, Double>) room.get("consumables");
        Map<String, Double> roomLinens = (Map<String, Double>) room.get("linens");

        // Calculate needs
        // Calculate needs and available stock
        double soapStock = mainInventory.getConsumableQuantity("soap");
        double shampooStock = mainInventory.getConsumableQuantity("shampoo");
        double toiletPaperStock = mainInventory.getConsumableQuantity("toilet_paper");
        double towelsStock = mainInventory.getLinenQuantity("towels");
        double bedSheetsStock = mainInventory.getLinenQuantity("bed_sheets");
        double pillowcasesStock = mainInventory.getLinenQuantity("pillowcases");

        // Set room quantities based on available stock
        roomConsumables.put("soap", Math.min(2.0, soapStock));
        roomConsumables.put("shampoo", Math.min(2.0, shampooStock));
        roomConsumables.put("toilet_paper", Math.min(3.0, toiletPaperStock));

        roomLinens.put("towels", Math.min(4.0, towelsStock));
        roomLinens.put("bed_sheets", Math.min(2.0, bedSheetsStock));
        roomLinens.put("pillowcases", Math.min(4.0, pillowcasesStock));

        // Create supply requests for shortages
        checkAndCreateRequest("soap", 2.0, soapStock, staff);
        checkAndCreateRequest("shampoo", 2.0, shampooStock, staff);
        checkAndCreateRequest("toilet_paper", 3.0, toiletPaperStock, staff);
        checkAndCreateRequest("towels", 4.0, towelsStock, staff);
        checkAndCreateRequest("bed_sheets", 2.0, bedSheetsStock, staff);
        checkAndCreateRequest("pillowcases", 4.0, pillowcasesStock, staff);

        // Update main inventory with actual used amounts
        mainInventory.updateConsumableQuantity("soap", soapStock - roomConsumables.get("soap"));
        mainInventory.updateConsumableQuantity("shampoo", shampooStock - roomConsumables.get("shampoo"));
        mainInventory.updateConsumableQuantity("toilet_paper", toiletPaperStock - roomConsumables.get("toilet_paper"));
        mainInventory.updateLinenQuantity("towels", towelsStock - roomLinens.get("towels"));
        mainInventory.updateLinenQuantity("bed_sheets", bedSheetsStock - roomLinens.get("bed_sheets"));
        mainInventory.updateLinenQuantity("pillowcases", pillowcasesStock - roomLinens.get("pillowcases"));

        // Update room data
        room.put("consumables", roomConsumables);
        room.put("linens", roomLinens);
        room.put("lastInventoryUpdate", LocalDateTime.now().format(DATE_FORMAT));
        room.put("lastUpdatedBy", staff.getName());

        // Save updated room data
        roomStorageHelper.getStore(DATA_ROOM_NAME).save(roomNumber, room);

        // Update main inventory status and save
        mainInventory.setLastUpdatedBy(staff.getName());
        mainInventory.setLastUpdateTime(LocalDateTime.now().format(DATE_FORMAT));
        inventoryStorageHelper.getStore(DATA_INVENTORY_NAME)
                .save("room_inventory", mainInventory.toMap());
    }

    private void checkAndCreateRequest(String item, double required, double available, CleaningStaff staff)
            throws IOException {
        if (available < required) {
            SupplyRequest request = new SupplyRequest();
            request.setRequestId(UUID.randomUUID().toString());
            request.setItem(item);
            request.setQuantityNeeded(required - available);
            request.setRequestedBy(staff.getName());
            request.setPriority("MEDIUM");
            request.setStatus("PENDING");
            supplyRequestStorageHelper.getStore(DATA_SUPPLY_REQUEST).save(request.getRequestId(), request.toMap());
        }
    }

    private void updateRoomStatus(String roomNumber, String status, CleaningStaff staff) throws IOException {
        Map<String, Object> room = roomStorageHelper.getStore(DATA_ROOM_NAME).load(roomNumber);
        if (!room.get("status").equals(STATUS_NEEDS_SUPPLIES)) {
            room.put("status", status);
        }
        if (status.equals(STATUS_CLEAN)) {
            room.put("lastCleaned", LocalDateTime.now().format(DATE_FORMAT));
        }
        roomStorageHelper.getStore(DATA_ROOM_NAME).save(roomNumber, room);
    }

    private void updateStaffStatus(CleaningStaff staff, String status) throws IOException {
        staff.setStatus(status);
        cleaningStaffStorageHelper.getStore(DATA_STAFF_NAME)
                .save(String.valueOf(staff.getId()), staff.toMap());
    }

    public int getNextId() throws IOException {
        List<Map<String, Object>> list_of_cleaning_staff = cleaningStaffStorageHelper.getStore(DATA_STAFF_NAME)
                .loadAll();
        return list_of_cleaning_staff.size() + 1;
    }

    // Method to check supplies and request if needed
    public boolean checkAndRequestSupplies(String roomNumber) throws IOException {
        return false;
    }

    public void createCleaningStaff() throws IOException {
        int id = this.getNextId();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter name:");
        String name = scanner.nextLine();
        System.out.println("Enter contact info:");
        String contactInfo = scanner.nextLine();
        String role = "CLEAN";
        String status = "AVAILABLE";
        System.out.println("Enter wage:");
        double wage = scanner.nextDouble();
        System.out.println("Enter shift hours:");
        String shiftHours = scanner.next();
        System.out.println("Enter experience:");
        int experience = scanner.nextInt();
        CleaningStaff staff = new CleaningStaff(id, name, contactInfo, role, status, wage, shiftHours, experience);
        cleaningStaffStorageHelper.getStore(DATA_STAFF_NAME).save(String.valueOf(id), staff.toMap());
        scanner.close();
    }
}
