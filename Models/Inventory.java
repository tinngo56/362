package Models;

import java.util.HashMap;
import java.util.Map;

public class Inventory extends Mappable<Inventory> {
    private Map<String, Double> consumables;
    private Map<String, Double> linens;
    private String lastUpdatedBy;
    private String lastUpdateTime;

    // Default constructor required for Mappable
    public Inventory() {
    }

    public Map<String, Double> getConsumables() {
        return consumables;
    }

    public void setConsumables(Map<String, Double> consumables) {
        this.consumables = consumables;
    }

    public Map<String, Double> getLinens() {
        return linens;
    }

    public void setLinens(Map<String, Double> linens) {
        this.linens = linens;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    // Utility methods for inventory management
    public void updateConsumableQuantity(String item, double quantity) {
        consumables.put(item, quantity);
    }

    public void updateLinenQuantity(String item, double quantity) {
        linens.put(item, quantity);
    }

    public double getConsumableQuantity(String item) {
        return consumables.getOrDefault(item, 0.0);
    }

    public double getLinenQuantity(String item) {
        return linens.getOrDefault(item, 0.0);
    }

}