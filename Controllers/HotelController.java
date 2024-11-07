package Controllers;

import java.io.IOException;
import java.util.Map;

import Models.Hotel;
import Storage.StorageHelper;

public class HotelController {
    private StorageHelper storageHelper;

    public HotelController(String baseDirectory) throws IOException {
        this.storageHelper = new StorageHelper(baseDirectory, "hotels");
    }

    public void createHotel(Hotel hotel) throws IOException {
        storageHelper.getStore("hotels").save(String.valueOf(hotel.getId()), hotel.toMap());
    }

    public Hotel getHotel(int id) throws IOException {
        Map<String, Object> data = storageHelper.getStore("hotels").load(String.valueOf(id));
        return data != null ? new Hotel().fromMap(data) : null;
    }

    public void updateHotel(Hotel hotel) throws IOException {
        storageHelper.getStore("hotels").save(String.valueOf(hotel.getId()), hotel.toMap());
    }

    public void deleteHotel(int id) throws IOException {
        storageHelper.getStore("hotels").delete(String.valueOf(id));
    }
}