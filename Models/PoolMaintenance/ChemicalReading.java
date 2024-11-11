package Models.PoolMaintenance;

import Models.Mappable;

public class ChemicalReading extends Mappable<PoolEquipmentInspection> {
    private int id;
    private String maintainedDate;
    private double phLevel;
    private double alkalinity;
    private double chlorine;
    private ChemicalAdition[] addedChemicals = new ChemicalAdition[2];

    public ChemicalReading(int id, String maintainedDate, double phLevel, double alkalinity, double chlorine) {
        this.id = id;
        this.maintainedDate = maintainedDate;
        this.phLevel = phLevel;
        this.alkalinity = alkalinity;
        this.chlorine = chlorine;
    }

    public ChemicalReading() {
        super();
    }

    // Getters and setters
    public double getChlorine() {
        return chlorine;
    }
    public void setChlorine(double chlorine) {
        this.chlorine = chlorine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaintainedDate() {
        return maintainedDate;
    }

    public void setMaintainedDate(String maintainedDate) {
        this.maintainedDate = maintainedDate;
    }

    public double getphLevel() {
        return phLevel;
    }

    public void setphLevel(double phLevel) {
        this.phLevel = phLevel;
    }

    public double getAlkalinity() {
        return alkalinity;
    }

    public void setAlkalinity(double Alkalinity) {
        this.alkalinity = Alkalinity;
    }

    public ChemicalAdition[] getAddedChemicals() {
        return addedChemicals;
    }

    public void setAddedChemicals(ChemicalAdition[] addedChemicals) {
        this.addedChemicals = addedChemicals;
    }
}
