package Models.CookFood;

public class IngredientDetail {
    private final double quantity;
    private final String unit;

    public IngredientDetail(double quantity, String unit) {
        this.quantity = quantity;
        this.unit = unit;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String toString(){
        return quantity + unit;
    }
}