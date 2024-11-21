package Controllers;

import Models.*;
import Storage.StorageHelper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class CleaningStaffController {
    private StorageHelper cleaningStaffStorageHelper;
    private StorageHelper roomStorageHelper;
    private StorageHelper inspectionStorageHelper;
    private StorageHelper inventoryStorageHelper;
    private StorageHelper maintenanceStorageHelper;
    private StorageHelper lostAndFoundStorageHelper;

    private final String DATA_STAFF_NAME = "cleaning_staff";
    private final String DATA_ROOM_NAME = "rooms";
    private final String DATA_INSPECTION_NAME = "inspections";
    private final String DATA_INVENTORY_NAME = "inventory";
    private final String DATA_MAINTENANCE_NAME = "maintenance";
    private final String DATA_LOST_FOUND_NAME = "lost_found";

    private final String STATUS_AVAILABLE = "AVAILABLE";
    private final String STATUS_CLEANING = "CLEANING";
    private final String STATUS_MAINTENANCE = "MAINTENANCE_REQUIRED";
    private final String STATUS_CLEAN = "CLEAN";

    private CleaningStaff cleaningStaff;

    public CleaningStaffController(String baseDirectory) throws IOException {
        cleaningStaffStorageHelper = new StorageHelper(baseDirectory, DATA_STAFF_NAME);
        roomStorageHelper = new StorageHelper(baseDirectory, DATA_ROOM_NAME);
        inspectionStorageHelper = new StorageHelper(baseDirectory, DATA_INSPECTION_NAME);
        inventoryStorageHelper = new StorageHelper(baseDirectory, DATA_INVENTORY_NAME);
        maintenanceStorageHelper = new StorageHelper(baseDirectory, DATA_MAINTENANCE_NAME);
        lostAndFoundStorageHelper = new StorageHelper(baseDirectory, DATA_LOST_FOUND_NAME);
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
                

                
    public void cleanRoom(String roomNumber, CleaningStaff cleaningStaff) throws IOException {
        try {
            // Update staff status to busy
            updateStaffStatus(cleaningStaff, STATUS_CLEANING);

            // Perform initial inspection
            RoomInspection inspection = performInspection(roomNumber, cleaningStaff);

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

    private RoomInspection performInspection(String roomNumber, CleaningStaff staff) throws IOException {
        RoomInspection inspection = new RoomInspection();
        inspection.setRoomNumber(roomNumber);
        inspection.setInspectedBy(staff.getName());
        inspection.setInspectionTime(LocalDateTime.now());

        Scanner scanner = new Scanner(System.in);
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
        scanner.close();

        // Save inspection results
        inspectionStorageHelper.getStore(DATA_INSPECTION_NAME)
                .save(roomNumber + "_" + System.currentTimeMillis(), inspection.toMap());

        return inspection;
    }

    private void handleMaintenanceIssues(String roomNumber, RoomInspection inspection, CleaningStaff staff)
            throws IOException {
        // Create maintenance request
        MaintenanceRequest request = new MaintenanceRequest();
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
                lostItem.setRoomNumber(roomNumber);
                lostItem.setDescription(item);
                lostItem.setFoundBy(staff.getName());

                // Save lost and found item
                lostAndFoundStorageHelper.getStore(DATA_LOST_FOUND_NAME)
                        .save(lostItem.getItemId(), lostItem.toMap());
            }
        }
    }

    private void performCleaning(String roomNumber, CleaningStaff staff) throws IOException {
        // Update room status to indicate cleaning in progress
        Map<String, Object> room = roomStorageHelper.getStore(DATA_ROOM_NAME).load(roomNumber);
        room.put("status", STATUS_CLEANING);
        room.put("cleanedBy", staff.getName());
        roomStorageHelper.getStore(DATA_ROOM_NAME).save(roomNumber, room);
    }

    private void updateRoomInventory(String roomNumber, CleaningStaff staff) throws IOException {
        RoomInventory inventory = new RoomInventory();
        inventory.setRoomNumber(roomNumber);
        inventory.setUpdatedBy(staff.getName());

        // Update default consumables
        inventory.updateConsumable("soap", 2);
        inventory.updateConsumable("shampoo", 2);
        inventory.updateConsumable("toilet_paper", 3);

        // Update linens
        inventory.updateLinens("towels", 4);
        inventory.updateLinens("bed_sheets", 2);
        inventory.updateLinens("pillowcases", 4);

        // Save inventory
        inventoryStorageHelper.getStore(DATA_INVENTORY_NAME)
                .save(roomNumber, inventory.toMap());
    }

    private void updateRoomStatus(String roomNumber, String status, CleaningStaff staff) throws IOException {
        Map<String, Object> room = roomStorageHelper.getStore(DATA_ROOM_NAME).load(roomNumber);
        room.put("status", status);
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
        RoomInventory inventory = (RoomInventory) new RoomInventory()
                .fromMap(inventoryStorageHelper.getStore(DATA_INVENTORY_NAME).load(roomNumber));

        boolean needsSupplies = false;
        Map<String, Integer> consumables = inventory.getConsumables();

        // Check minimum thresholds
        if (consumables.getOrDefault("soap", 0) < 2 ||
                consumables.getOrDefault("shampoo", 0) < 2 ||
                consumables.getOrDefault("toilet_paper", 0) < 2) {
            needsSupplies = true;
        }

        return needsSupplies;
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
