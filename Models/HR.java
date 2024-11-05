package Models;

public class HR {
    private int id;
    private String name;
    private double wageBudget;
    private int numberOfEmployeesManaged;
    private String policiesImplemented;

    public HR(int id, String name, double wageBudget, int numberOfEmployeesManaged, String policiesImplemented) {
        this.id = id;
        this.name = name;
        this.wageBudget = wageBudget;
        this.numberOfEmployeesManaged = numberOfEmployeesManaged;
        this.policiesImplemented = policiesImplemented;
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