package Models;

import java.util.HashMap;
import java.util.Map;

public class Staff {
    private String id;
    private String name;
    private String role;
    private String status;

    public Staff(String id, String name, String role, String status) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.status = status;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("role", role);
        map.put("status", status);
        return map;
    }

    // Convert map to object
    public static Staff fromMap(Map<String, Object> map) {
        String id = (String) map.get("id");
        String name = (String) map.get("name");
        String role = (String) map.get("role");
        String status = (String) map.get("status");
        return new Staff(id, name, role, status);
    }
}