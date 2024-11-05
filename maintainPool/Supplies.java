package maintainPool;

import java.util.HashMap;
import java.util.Map;

public class Supplies {

    private Map<String, Integer> supplies;
    private Map<String, Integer> defaultUsage;

    public Supplies() {
        supplies = new HashMap<>();
        defaultUsage = new HashMap<>();

        supplies.put("Towels", 100);
        supplies.put("CleaningSolution", 50);
        supplies.put("Sanitizer", 30);
    }

    /**
     * Sets default usage
     * @param supply name of the supply
     * @param amount default amount to use
     */
    public void setDefaultUsage(String supply, int amount) {
        if (supplies.containsKey(supply)) {
            defaultUsage.put(supply, amount);
        }
    }

    /**
     * Use default amount of  supply
     * @param supply the name of the supply
     */
    public void useSupplies(String supply) {
        Integer usageAmount = defaultUsage.get(supply);
        if (usageAmount != null) {
            useSupplies(supply, usageAmount);
        }
    }

    /**
     * Use amount of supply
     * @param supply name of supply
     * @param amount amount used
     */
    public void useSupplies(String supply, int amount) {
        if (supplies.containsKey(supply)) {
            int currentAmount = supplies.get(supply);
            int newAmount = Math.max(0, currentAmount - amount);
            supplies.put(supply, newAmount);
            System.out.println("Used " + amount + " of " + supply + ". Remaining: " + newAmount);
        } else {
            System.out.println("Supply not found: " + supply);
        }
    }

    /**
     * Get stock of supply
     * @param supply name
     * @return amount of stock
     */
    public int getSupplyAmount(String supply) {
        return supplies.getOrDefault(supply, 0);
    }

    /**
     * Set amount of a supply
     * @param supply name
     * @param amount amount to set
     */
    public void setSupplyAmount(String supply, int amount) {
        if (supplies.containsKey(supply)) {
            supplies.put(supply, Math.max(0, amount));
            System.out.println("Set " + supply + " stock to " + amount);
        } else {
            System.out.println("Supply not found: " + supply);
        }
    }
}
