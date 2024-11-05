package Models;

import java.util.Map;

public abstract class Staff extends Person {
    protected String role;
    protected String status;

    public Staff(int id, String name, String contactInfo, String role, String status) {
        super(id, name, contactInfo);
        this.role = role;
        this.status = status;
    }

    // Getters and setters
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Convert object to map
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("role", role);
        map.put("status", status);
        return map;
    }

    // Convert map to object
    public static Staff fromMap(Map<String, Object> map) {
        int id = (Integer) map.get("id");
        String name = (String) map.get("name");
        String contactInfo = (String) map.get("contactInfo");
        String role = (String) map.get("role");
        String status = (String) map.get("status");
        return new Staff(id, name, contactInfo, role, status) {};
    }
}