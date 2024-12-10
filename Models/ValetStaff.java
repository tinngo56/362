package Models;

public class ValetStaff extends Mappable<ValetStaff> {
    private int id;
    private String name;
    private String contactInfo;
    private String status;
    private double wage;
    private String shiftHours;
    private int experience;

    public ValetStaff() {
    } // Required for Mappable

    public ValetStaff(int id, String name, String contactInfo, String status,
            double wage, String shiftHours, int experience) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.status = status;
        this.wage = wage;
        this.shiftHours = shiftHours;
        this.experience = experience;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }

    public String getShiftHours() {
        return shiftHours;
    }

    public void setShiftHours(String shiftHours) {
        this.shiftHours = shiftHours;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}