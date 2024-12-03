package Models.Grounds;

import Models.Mappable;

public class Grounds extends Mappable<Grounds> {
    private int id;
    private String date;
    private double grassHeight;
    private double bushLength;
    private int treeCount;
    private int weedCount;
    private String notes;

    public Grounds(int id, String date, double grassHeight, double bushLength, int treeCount, int weedCount, String notes) {
        this.id = id;
        this.date = date;
        this.grassHeight = grassHeight;
        this.bushLength = bushLength;
        this.treeCount = treeCount;
        this.weedCount = weedCount;
        this.notes = notes;
    }

    public Grounds(){
        super();
    }


    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public double getGrassHeight() {
        return grassHeight;
    }

    public void setGrassHeight(double grassHeight) {
        this.grassHeight = grassHeight;
    }

    public double getBushLength() {
        return bushLength;
    }

    public void setBushLength(double bushLength) {
        this.bushLength = bushLength;
    }

    public int getTreeCount() {
        return treeCount;
    }

    public void setTreeCount(int treeCount) {
        this.treeCount = treeCount;
    }

    public int getWeedCount() {
        return weedCount;
    }

    public void setWeedCount(int weedCount) {
        this.weedCount = weedCount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes){
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Grounds Inspection {\n" +
                "  ID: " + id + "\n" +
                "  Date: " + date + "\n" +
                "  Grass Height: " + grassHeight + " cm\n" +
                "  Bush Length: " + bushLength + " cm\n" +
                "  Tree Count: " + treeCount + "\n" +
                "  Weed Count: " + weedCount + "\n" +
                "  Notes: " + (notes == null || notes.isEmpty() ? "None" : notes) + "\n" +
                "}";
    }
}
