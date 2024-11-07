package Models;

public abstract class Staff extends Person {
    protected String role;
    protected String status;

    public Staff(int id, String name, String contactInfo, String role, String status) {
        super(id, name, contactInfo);
        this.role = role;
        this.status = status;
    }

    public Staff() {
        super();
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
}