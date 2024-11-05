package Models;

import java.util.Map;

public class Receptionist extends Staff {
    private double wage;
    private String shiftHours;
    private int experience;

    public Receptionist(int id, String name, String contactInfo, String role, String status, double wage, String shiftHours, int experience) {
        super(id, name, contactInfo, role, status);
        this.wage = wage;
        this.shiftHours = shiftHours;
        this.experience = experience;
    }

    // Getters and setters
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

    // Convert object to map
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("wage", wage);
        map.put("shiftHours", shiftHours);
        map.put("experience", experience);
        return map;
    }

    // Convert map to object
    public static Receptionist fromMap(Map<String, Object> map) {
        int id = (Integer) map.get("id");
        String name = (String) map.get("name");
        String contactInfo = (String) map.get("contactInfo");
        String role = (String) map.get("role");
        String status = (String) map.get("status");
        double wage = (Double) map.get("wage");
        String shiftHours = (String) map.get("shiftHours");
        int experience = (Integer) map.get("experience");
        return new Receptionist(id, name, contactInfo, role, status, wage, shiftHours, experience);
    }
}