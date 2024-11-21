package Models;

import java.util.ArrayList;
import java.util.List;

public class RoomServiceMenu extends Mappable<RoomServiceMenu> {
    private String lastUpdated;
    private String lastModified;
    private List<MenuItem> items;

    // Constructor, getters, setters
    public RoomServiceMenu() {
        this.items = new ArrayList<>();
    }
    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }
}
