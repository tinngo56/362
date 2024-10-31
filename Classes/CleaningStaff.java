public class CleaningStaff {
    private int id;
    private String name;
    private String contactInfo;
    private double wage;
    private String shiftHours;
    private int experience;

    public CleaningStaff(int id, String name, String contactInfo, double wage, String shiftHours, int experience) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.wage = wage;
        this.shiftHours = shiftHours;
        this.experience = experience;
    }

    // Getters and setters
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