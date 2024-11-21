package Controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Models.CleaningStaff;
import Models.RoomServiceOrder;
import Models.RoomServiceOrder.OrderStatus;
import Models.RoomServiceStaff;
import Storage.StorageHelper;

public class RoomServiceStaffController {
    private final StorageHelper staffStorageHelper;
    private final StorageHelper orderStorageHelper;
    private static final String DATA_STAFF = "room_service_staff";
        private static final String DATA_ORDER_NAME = "room_service_orders";
    
        public RoomServiceStaffController(String baseDirectory) throws IOException {
            this.staffStorageHelper = new StorageHelper(baseDirectory, DATA_STAFF);
            this.orderStorageHelper = new StorageHelper(baseDirectory, DATA_ORDER_NAME);
        }
    
        public RoomServiceStaff getAvailableRoomServiceStaff() throws IOException {
            List<Map<String, Object>> list_of_room_service_staff = staffStorageHelper.getStore(DATA_STAFF)
                    .loadAll();
            List<Map<String, Object>> availableStaff = list_of_room_service_staff.stream()
                    .filter(staff -> staff.get("status").equals("AVAILABLE"))
                    .collect(Collectors.toList());
            if (availableStaff.isEmpty()) {
                throw new IOException("No available room service staff");
            }
            return (RoomServiceStaff) new RoomServiceStaff().fromMap(availableStaff.get(0));
        }
    
        public void assignOrder(RoomServiceStaff staff, String orderId) throws IOException {
            Map<String, Object> orderData = orderStorageHelper.getStore(DATA_ORDER_NAME).load(orderId);
        RoomServiceOrder order = new RoomServiceOrder().fromMap(orderData);

        // Set staff to order
        order.setStatus(OrderStatus.CONFIRMED);

        // Update staff status
        updateStaffStatus(String.valueOf(staff.getId()), orderId);

        // Save updated order
        orderStorageHelper.getStore(DATA_ORDER_NAME).save(orderId, order.toMap());
    }

    public void updateStaffStatus(String staffId, String status) throws IOException {
        Map<String, Object> staffData = staffStorageHelper.getStore(DATA_STAFF).load(staffId);
        RoomServiceStaff staff = (RoomServiceStaff) new RoomServiceStaff().fromMap(staffData);
        staff.setStatus(status);
        staffStorageHelper.getStore(DATA_STAFF).save(staffId, staff.toMap());
    }

    public void requestOrder(RoomServiceOrder order, RoomServiceStaff staff) throws IOException {
        order.setStatus(OrderStatus.IN_PREPARATION);
        orderStorageHelper.getStore(DATA_ORDER_NAME).save(order.getOrderId(), order.toMap());
    }

    public void deliverOrder(RoomServiceOrder order, RoomServiceStaff staff) throws IOException {
        order.setStatus(OrderStatus.DELIVERED);
        orderStorageHelper.getStore(DATA_ORDER_NAME).save(String.valueOf(order.getRoomNumber()) + "_" + order.getOrderId(), order.toMap());

        // Update staff status
        updateStaffStatus(String.valueOf(staff.getId()), "AVAILABLE");
    }
}
