package Models.CookFood;

import Models.Mappable;

public class Menu extends Mappable<Menu> {
    private int id;
    private String issues;
    private boolean sausage;
    private boolean bacon;
    private boolean eggs;
    private boolean waffles;
    private boolean pancakes;
    private boolean syrup;
    private boolean yogurt;
    private int guests;

    public Menu(int id, String issues, boolean sausage, boolean bacon, boolean eggs, boolean waffles, boolean pancakes, boolean syrup, boolean yogurt, int guests) {
        this.id = id;
        this.issues = issues;
        this.sausage = sausage;
        this.bacon = bacon;
        this.eggs = eggs;
        this.waffles = waffles;
        this.pancakes = pancakes;
        this.syrup = syrup;
        this.yogurt = yogurt;
        this.guests = guests;
    }

    public Menu(int id) {
        this.id = id;
    }



    public Menu() {
        super();
    }


    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIssues(String issues) {
        this.issues = issues;
    }

    public boolean isSausage() {
        return sausage;
    }

    public void setSausage(boolean sausage) {
        this.sausage = sausage;
    }

    public boolean isBacon() {
        return bacon;
    }

    public void setBacon(boolean bacon) {
        this.bacon = bacon;
    }

    public boolean isEggs() {
        return eggs;
    }

    public void setEggs(boolean eggs) {
        this.eggs = eggs;
    }

    public boolean isWaffles() {
        return waffles;
    }

    public void setWaffles(boolean waffles) {
        this.waffles = waffles;
    }

    public boolean isPancakes() {
        return pancakes;
    }

    public void setPancakes(boolean pancakes) {
        this.pancakes = pancakes;
    }

    public boolean isSyrup() {
        return syrup;
    }

    public void setSyrup(boolean syrup) {
        this.syrup = syrup;
    }

    public boolean isYogurt() {
        return yogurt;
    }

    public void setYogurt(boolean yogurt) {
        this.yogurt = yogurt;
    }

    public String getIssues() {
        return issues;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Menu Report:\n")
                .append("ID: ").append(id).append("\n")
                .append("Number of Guests: ").append(guests).append("\n");

        if (issues != null && !issues.isEmpty()) {
            result.append("Issues: ").append(issues).append("\n");
        } else {
            result.append("Issues: None\n");
        }

        result.append(sausage ? "Success making sausage.\n" : "Failed to make sausage.\n");
        result.append(bacon ? "Success making bacon.\n" : "Failed to make bacon.\n");
        result.append(eggs ? "Success making eggs.\n" : "Failed to make eggs.\n");
        result.append(waffles ? "Success making waffles.\n" : "Failed to make waffles.\n");
        result.append(pancakes ? "Success making pancakes.\n" : "Failed to make pancakes.\n");
        result.append(syrup ? "Success preparing syrup.\n" : "Failed to prepare syrup.\n");
        result.append(yogurt ? "Success preparing yogurt.\n" : "Failed to prepare yogurt.\n");

        return result.toString();
    }

}
