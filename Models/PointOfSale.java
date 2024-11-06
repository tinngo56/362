package Models;

public class PointOfSale extends Mappable<PointOfSale> {
    private int id;
    private String operator;
    private double totalSales;
    private String validationSystem;

    public PointOfSale(int id, String operator, double totalSales, String validationSystem) {
        this.id = id;
        this.operator = operator;
        this.totalSales = totalSales;
        this.validationSystem = validationSystem;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public String getValidationSystem() {
        return validationSystem;
    }

    public void setValidationSystem(String validationSystem) {
        this.validationSystem = validationSystem;
    }
}