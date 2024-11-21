package Controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import Models.CleaningStaff;
import Storage.StorageHelper;

public class CleaningStaffController {
    private StorageHelper cleaningStaffStorageHelper;
    private StorageHelper roomStorageHelper;

    private final String DATA_STAFF_NAME = "cleaning_staff";
    private final String DATA_ROOM_NAME = "rooms";
    private final String STATUS_AVAILABLE = "AVAILABLE";
    private CleaningStaff cleaningStaff;

    public CleaningStaffController(String baseDirectory) throws IOException {
        cleaningStaffStorageHelper = new StorageHelper(baseDirectory, DATA_STAFF_NAME);
        roomStorageHelper = new StorageHelper(baseDirectory, DATA_ROOM_NAME);
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

    public int getNextId() throws IOException {
        List<Map<String, Object>> list_of_cleaning_staff = cleaningStaffStorageHelper.getStore(DATA_STAFF_NAME)
                .loadAll();
        return list_of_cleaning_staff.size() + 1;
    }

    public void cleanRoom(String roomNumber, CleaningStaff cleaningStaff) throws IOException {
        Map<String, Object> room = roomStorageHelper.getStore(DATA_ROOM_NAME).load(roomNumber);
        cleaningStaff.setStatus("BUSY");
        cleaningStaffStorageHelper.getStore(DATA_STAFF_NAME).save(String.valueOf(cleaningStaff.getId()), cleaningStaff.toMap());
        room.put("lastCleaned", cleaningStaff.getName());
        room.put("status", "AVAILABLE");
        roomStorageHelper.getStore(DATA_ROOM_NAME).save(roomNumber, room);
        cleaningStaff.setStatus("AVAILABLE");
        cleaningStaffStorageHelper.getStore(DATA_STAFF_NAME).save(String.valueOf(cleaningStaff.getId()), cleaningStaff.toMap());
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
