package Models.PoolMaintenance;


import Models.Mappable;


public class PoolEquipmentInspection extends Mappable<PoolEquipmentInspection> {
    private int id;
    private String maintainedDate;
    private boolean pump;
    private boolean filter;
    private boolean heater;


    public PoolEquipmentInspection(int id, String maintainedDate, boolean pump, boolean filter, boolean heater, ChemicalAdition[] addedChemicals) {
        this.id = id;
        this.maintainedDate = maintainedDate;
        this.pump = pump;
        this.filter = filter;
        this.heater = heater;
    }


    public PoolEquipmentInspection(int id, String maintainedDate, boolean pump, boolean filter, boolean heater) {
        this.id = id;
        this.maintainedDate = maintainedDate;
        this.pump = pump;
        this.filter = filter;
        this.heater = heater;
    }


    public PoolEquipmentInspection() {
        super();
    }


    // Getters and setters
    public boolean getHeater() {
        return heater;
    }


    public void setHeater(boolean heater) {
        this.heater = heater;
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

    public boolean getPump() {
        return pump;
    }

    public void setPump(boolean pump) {
        this.pump = pump;
    }

    public boolean getFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }
}