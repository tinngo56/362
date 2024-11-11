
package Models.CookFood;

import java.util.ArrayList;
import java.util.Map;

public abstract class Food {
    private final String name;
    protected Recipe recipe;

    public Food(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Map.Entry<String, IngredientDetail>> getRecipe() {
        return recipe.getIngredients();
    }

    public String getDescription() {
        return "Delicious " + name + ".";
    }

    public String getPreparationInstructions() {
        return recipe.getInstructions();
    }

    public Food getFoodItem() {
        return this;
    }
}