package Controllers;

import java.io.IOException;
import java.util.Map;

import Models.Hotel;
import Storage.HotelStorageHelper;

public class HotelController {
    private HotelStorageHelper hotelStorageHelper;

    public HotelController(String baseDirectory) throws IOException {
        this.hotelStorageHelper = new HotelStorageHelper(baseDirectory);
    }

    public void createHotel(Hotel hotel) throws IOException {
        hotelStorageHelper.getStore("hotels").save(String.valueOf(hotel.getId()), hotel.toMap());
    }

    public Hotel getHotel(int id) throws IOException {
        Map<String, Object> data = hotelStorageHelper.getStore("hotels").load(String.valueOf(id));
        return data != null ? Hotel.fromMap(data) : null;
    }

    public void updateHotel(Hotel hotel) throws IOException {
        hotelStorageHelper.getStore("hotels").save(String.valueOf(hotel.getId()), hotel.toMap());
    }

    public void deleteHotel(int id) throws IOException {
        hotelStorageHelper.getStore("hotels").delete(String.valueOf(id));
    }
}