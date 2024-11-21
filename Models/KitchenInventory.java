package Models;

import java.util.HashMap;
import java.util.Map;

public class KitchenInventory extends Mappable<KitchenInventory> {
    private Map<String, Double> ingredients;
    
    public KitchenInventory() {
        this.ingredients = new HashMap<>();
    }

    public Map<String, Double> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, Double> ingredients) {
        this.ingredients = ingredients;
    }
}