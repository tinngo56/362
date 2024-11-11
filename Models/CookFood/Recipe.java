package Models.CookFood;

import java.util.*;
import java.util.Map;

public class Recipe {
    private final Map<String, IngredientDetail> ingredients; // ingredient name -> IngredientDetail (has quanitity and unit)
    private double servings;
    private final String instructions;

    public Recipe(Map<String, IngredientDetail> ingredients, String instructions, double servings) {
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.servings = servings;
    }

    public Recipe(Map<String, IngredientDetail> ingredients, String instructions) {
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.servings = 1;
    }

    public String getInstructions() {
        return instructions;
    }

    public double getServings(){
        return servings;
    }

    public ArrayList<Map.Entry<String, IngredientDetail>> getIngredients(){
        return new ArrayList<>(ingredients.entrySet());
    }
}