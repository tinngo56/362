package Models;

import java.util.List;

public class RoomServiceStaff extends Staff {
    private double wage;
    private String shiftHours;
    private int experience;

    public RoomServiceStaff(int id, String name, String contactInfo, String role, String status, double wage, String shiftHours, int experience) {
        super(id, name, contactInfo, role, status);
        this.wage = wage;
        this.shiftHours = shiftHours;
        this.experience = experience;
    }
    public RoomServiceStaff() {
        super();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}

