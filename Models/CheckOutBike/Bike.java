package Models.CheckOutBike;

import java.util.HashMap;
import java.util.Map;

public class Bike {
    private String bikeName;
    private String bikeBrand;
    private int id;
    private String bikeState;

    public Bike(String bikeName, String bikeBrand, int id, String bikeState) {
        this.bikeName = bikeName;
        this.bikeBrand = bikeBrand;
        this.id = id;
        this.bikeState = bikeState;
    }

    public Bike(Map<String, Object> map) {
        this.bikeName = (String) map.get("bikeName");
        this.bikeBrand = (String) map.get("bikeBrand");
        this.id = ((Number)map.get("id")).intValue();
        this.bikeState = (String) map.get("bikeState");
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("bikeName", bikeName);
        map.put("bikeBrand", bikeBrand);
        map.put("id", id);
        map.put("bikeState", bikeState);
        return map;
    }

    public String getBikeName() {
        return bikeName;
    }

    public void setBikeName(String bikeName) {
        this.bikeName = bikeName;
    }

    public String getBikeBrand() {
        return bikeBrand;
    }

    public void setBikeBrand(String bikeBrand) {
        this.bikeBrand = bikeBrand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBikeState() {
        return bikeState;
    }

    public void setBikeState(String bikeState) {
        this.bikeState = bikeState;
    }
}
