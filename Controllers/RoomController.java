package Controllers;

import Models.Hotel;
import Models.Room;
import storage.StorageHelper;

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
