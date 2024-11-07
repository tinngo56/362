package Models;

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

    public HR() {
        super();
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
}