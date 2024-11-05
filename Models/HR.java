package Models;

import java.util.Map;

public class HR extends Staff {
    private double wageBudget;
    private int numberOfEmployeesManaged;
    private String policiesImplemented;

    public HR(int id, String name, String contactInfo, String role, String status, double wageBudget, int numberOfEmployeesManaged, String policiesImplemented) {
        super(id, name, contactInfo, role, status);
        this.wageBudget = wageBudget;
        this.numberOfEmployeesManaged = numberOfEmployeesManaged;
        this.policiesImplemented = policiesImplemented;
    }

    // Getters and setters
    public double getWageBudget() {
        return wageBudget;
    }

    public void setWageBudget(double wageBudget) {
        this.wageBudget = wageBudget;
    }

    public int getNumberOfEmployeesManaged() {
        return numberOfEmployeesManaged;
    }

    public void setNumberOfEmployeesManaged(int numberOfEmployeesManaged) {
        this.numberOfEmployeesManaged = numberOfEmployeesManaged;
    }

    public String getPoliciesImplemented() {
        return policiesImplemented;
    }

    public void setPoliciesImplemented(String policiesImplemented) {
        this.policiesImplemented = policiesImplemented;
    }

    // Convert object to map
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("wageBudget", wageBudget);
        map.put("numberOfEmployeesManaged", numberOfEmployeesManaged);
        map.put("policiesImplemented", policiesImplemented);
        return map;
    }

    // Convert map to object
    public static HR fromMap(Map<String, Object> map) {
        int id = (Integer) map.get("id");
        String name = (String) map.get("name");
        String contactInfo = (String) map.get("contactInfo");
        String role = (String) map.get("role");
        String status = (String) map.get("status");
        double wageBudget = (Double) map.get("wageBudget");
        int numberOfEmployeesManaged = (Integer) map.get("numberOfEmployeesManaged");
        String policiesImplemented = (String) map.get("policiesImplemented");
        return new HR(id, name, contactInfo, role, status, wageBudget, numberOfEmployeesManaged, policiesImplemented);
    }
}