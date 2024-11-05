package Models;

public class Payroll {
    private int id;
    private int employeeId;
    private double salary;
    private double bonuses;
    private String dateIssued;
    private String paymentStatus;

    public Payroll(int id, int employeeId, double salary, double bonuses, String dateIssued, String paymentStatus) {
        this.id = id;
        this.employeeId = employeeId;
        this.salary = salary;
        this.bonuses = bonuses;
        this.dateIssued = dateIssued;
        this.paymentStatus = paymentStatus;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getBonuses() {
        return bonuses;
    }

    public void setBonuses(double bonuses) {
        this.bonuses = bonuses;
    }

    public String getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(String dateIssued) {
        this.dateIssued = dateIssued;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}