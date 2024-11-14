package Controllers;

import Models.Room;
import Storage.StorageHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomController {
    private StorageHelper storageHelper;

    public RoomController(String baseDirectory) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, "rooms");
    }

    public void createRoom(Room room) throws IOException {
        storageHelper.getStore("rooms").save(String.valueOf(room.getRoomNumber()), room.toMap());
    }

    public Room getRoom(int id) throws IOException {
        Map<String, Object> data = storageHelper.getStore("rooms").load(String.valueOf(id));
        return data != null ? new Room().fromMap(data) : null;
    }

    public void updateRoom(Room room) throws IOException {
        storageHelper.getStore("rooms").save(String.valueOf(room.getRoomNumber()), room.toMap());
    }

    public void deleteRoom(int id) throws IOException {
        storageHelper.getStore("rooms").delete(String.valueOf(id));
    }

    public boolean isRoomAvailable(Room room) {
        return room.getStatus().equals("AVAILABLE");
    }

    public Room isARoomAvailableFromRequirements(String roomType) throws IOException {
        List<Room> rooms = getAllRooms();
        for(Room room : rooms) {
            if(room.getRoomType().equals(roomType) && isRoomAvailable(room)) {
                return room;
            }
        }

        return null;
    }

    public List<Room> getAllRooms() throws IOException {
        List<Map<String, Object>> data = storageHelper.getStore("rooms").loadAll();
        if (data == null || data.isEmpty()) return null;
        List<Room> rooms = new ArrayList<>();
        for (Map<String, Object> room : data) {
            rooms.add(new Room().fromMap(room));
        }
       return rooms;
    }
}
