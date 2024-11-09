package Models.PoolMaintenance;

public class ChemicalAdition {
    String chemical;
    double amountAdded;

    public ChemicalAdition(double amountAdded, String chemical) {
        this.amountAdded = amountAdded;
        this.chemical = chemical;
    }

    public String getChemical() {
        return chemical;
    }

    public void setChemical(String chemical) {
        this.chemical = chemical;
    }

    public double getAmountAdded() {
        return amountAdded;
    }

    public void setAmountAdded(double amountAdded) {
        this.amountAdded = amountAdded;
    }
}
