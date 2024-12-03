package Models.EmployeeHiring;

import Models.Staff;

import java.util.HashMap;
import java.util.Map;

public class Employee extends Staff {
    private double hourlyRate;
    private double hoursWorked;
    private double overtimeHours;
    private double deductions;
    private String bankAccountInfo;
    private double bankTotal;

    public Employee(int id, String name, String contactInfo, String role, String status,
                    double hourlyRate, double hoursWorked, double overtimeHours, double deductions, String bankAccountInfo, double bankTotal) {
        super(id, name, contactInfo, role, status);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
        this.overtimeHours = overtimeHours;
        this.bankAccountInfo = bankAccountInfo;
        this.bankTotal = bankTotal;
        this.deductions = deductions;
    }

    public Employee() {
        super();
    }

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(double hoursWorked) { this.hoursWorked = hoursWorked; }

    public double getOvertimeHours() { return overtimeHours; }
    public void setOvertimeHours(double overtimeHours) { this.overtimeHours = overtimeHours; }

    public double getDeductions() { return deductions; }
    public void setDeductions(double deductions) { this.deductions = deductions; }

    public String getBankAccountInfo() { return bankAccountInfo; }
    public void setBankAccountInfo(String bankAccountInfo) { this.bankAccountInfo = bankAccountInfo; }

    public double getBankTotal(){return bankTotal;}
    public void setBankTotal(double bankTotal){ this.bankTotal = bankTotal;}

//    private Map<String, Object> convertEmployeeToMap(Employee employee) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("id", employee.getId());
//        map.put("name", employee.getName());
//        map.put("role", employee.getRole());
//        map.put("hourlyRate", employee.getHourlyRate());
//        map.put("hoursWorked", employee.getHoursWorked());
//        map.put("overtimeHours", employee.getOvertimeHours());
//        map.put("deductions", employee.getDeductions());
//        map.put("bankAccountInfo", employee.getBankAccountInfo());
//        map.put("bankTotal", employee.getBankTotal());
//        return map;
//    }
//
//
//    private Employee convertMapToEmployee(Map<String, Object> data) {
//        int id = (int) data.get("id");
//        String name = (String) data.get("name");
//        String role = (String) data.get("role");
//        double hourlyRate = (double) data.get("hourlyRate");
//        double hoursWorked = (double) data.get("hoursWorked");
//        double overtimeHours = (double) data.get("overtimeHours");
//        double deductions = (double) data.get("deductions");
//        String bankAccountInfo = (String) data.get("bankAccountInfo");
//        double bankTotal = (double) data.get("bankTotal");
//
//        return new Employee(id, name, "", role, "", hourlyRate, hoursWorked, overtimeHours, deductions, bankAccountInfo, bankTotal);
//    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("id", getId());
        map.put("name", getName());
        map.put("contactInfo", getContactInfo());
        map.put("rolle", getRole());
        map.put("status", getStatus());

        return map;
    }

    @Override
    public Employee fromMap(Map<String, Object> map) {
        // Use `Number` to accommodate different numeric types
        Number idNumber = (Number) map.get("id");
        int id = idNumber.intValue();  // Convert to int from Long

        String name = (String) map.get("name");
        String contactInfo = (String) map.get("contactInfo");
        String role = (String) map.get("role");
        String status = (String) map.get("status");
        double hourlyRate = (Double) map.get("hourlyRate");
        double hoursWorked = (Double) map.get("hoursWorked");
        double overtimeHours = (Double) map.get("overtimeHours");
        double deductions = (Double) map.get("deductions");
        String bankAccountInfo = (String) map.get("bankAccountInfo");
        double bankTotal = (Double) map.get("bankTotal");

        return new Employee(id, name, contactInfo, role, status, hourlyRate, hoursWorked, overtimeHours, deductions, bankAccountInfo, bankTotal);
    }

}

