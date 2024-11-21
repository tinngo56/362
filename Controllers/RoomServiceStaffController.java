package Controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Models.RoomServiceStaff;
import Storage.StorageHelper;

public class RoomServiceStaffController {
    private final StorageHelper staffStorageHelper;
    private static final String DATA_STAFF = "room_service_staff";

    public RoomServiceStaffController(String baseDirectory) throws IOException {
        this.staffStorageHelper = new StorageHelper(baseDirectory, DATA_STAFF);
    }

    public RoomServiceStaff getAvailableStaff() throws IOException {
        List<Map<String, Object>> allStaff = staffStorageHelper.getStore(DATA_STAFF).loadAll();

        for (Map<String, Object> staffData : allStaff) {
            RoomServiceStaff staff = (RoomServiceStaff) new RoomServiceStaff().fromMap(staffData);
            if ("AVAILABLE".equals(staff.getStatus())) {
                return staff;
            }
        }
        return null;
    }

    public void assignOrder(String staffId, String orderId) throws IOException {
        Map<String, Object> staffData = staffStorageHelper.getStore(DATA_STAFF).load(staffId);
        RoomServiceStaff staff = (RoomServiceStaff) new RoomServiceStaff().fromMap(staffData);
        staff.setStatus("BUSY");
        staffStorageHelper.getStore(DATA_STAFF).save(staffId, staff.toMap());
    }

    public void updateStaffStatus(String staffId, String status) throws IOException {
        Map<String, Object> staffData = staffStorageHelper.getStore(DATA_STAFF).load(staffId);
        RoomServiceStaff staff = (RoomServiceStaff) new RoomServiceStaff().fromMap(staffData);
        staff.setStatus(status);
        staffStorageHelper.getStore(DATA_STAFF).save(staffId, staff.toMap());
    }
}
