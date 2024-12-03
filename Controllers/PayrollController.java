package Controllers;

import Models.EmployeeHiring.Applicant;
import Models.EmployeeHiring.Employee;
import Models.Payroll;
import Storage.StorageHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PayrollController {
    private StorageHelper employeeStorageHelper;
    private StorageHelper payrollStorageHelper;
    private final String EMPLOYEES_NAME = "employees";
    private final String PAYROLLS_NAME = "payrolls";
    private int nextPayrollId;

    public PayrollController(String baseDirectory) throws IOException {
        this.employeeStorageHelper = new StorageHelper(baseDirectory, EMPLOYEES_NAME);
        this.payrollStorageHelper = new StorageHelper(baseDirectory, PAYROLLS_NAME);
        this.nextPayrollId = 1;
    }

    public void createEmployee(Employee employee) throws IOException {
        employeeStorageHelper.getStore(EMPLOYEES_NAME).save(String.valueOf(employee.getId()), employee.toMap());
    }

    public Employee getEmployee(int id) throws IOException {
        Map<String, Object> data = employeeStorageHelper.getStore(EMPLOYEES_NAME).load(String.valueOf(id));
        return data != null ? (Employee) new Employee().fromMap(data) : null;
    }

    public void updateEmployee(Employee employee) throws IOException {
        employeeStorageHelper.getStore(EMPLOYEES_NAME).save(String.valueOf(employee.getId()), employee.toMap());
    }

    public void deleteEmployee(int id) throws IOException {
        employeeStorageHelper.getStore(EMPLOYEES_NAME).delete(String.valueOf(id));
    }
    private void createPayroll(Payroll payroll) throws IOException {
        payrollStorageHelper.getStore(PAYROLLS_NAME).save(String.valueOf(payroll.getId()), payroll.toMap());
    }

    public void payEmployee(Scanner scanner) throws IOException {
        System.out.print("Enter Employee ID to pay: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();

        Employee employee = getEmployee(employeeId);
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        double basePay = employee.getHourlyRate() * employee.getHoursWorked();
        double overtimePay = employee.getHourlyRate() * 1.5 * employee.getOvertimeHours();
        double totalPayment = basePay + overtimePay - employee.getDeductions();

        System.out.println("Total payment calculated: $" + totalPayment);

        if (isBankInfoValid(employee.getBankAccountInfo())) {
            System.out.println("Proceed with payment? (yes/no)");
            String response = scanner.nextLine();

            if ("yes".equals(response)) {
                employee.setBankTotal(totalPayment + employee.getBankTotal());
                updateEmployee(employee);

                // Create Payroll record
                Payroll payroll = new Payroll(nextPayrollId, employee.getId(), totalPayment, 0.0, "2024-11-14", "Paid");
                createPayroll(payroll);

                System.out.println("Payment of $" + totalPayment + " processed for employee: " + employee.getName());
            } else {
                System.out.println("Payment process canceled.");
            }
        } else {
            handleBankInfoIssue(employee, scanner);
        }
    }

    public void verifyPayrollInfo(Scanner scanner) throws IOException {
        System.out.print("Enter Employee ID: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();

        Employee employee = getEmployee(employeeId);

        System.out.println("Please verify the payroll information for employee: " + employee.getName());
        System.out.println("Employee ID: " + employee.getId());
        System.out.println("Role: " + employee.getRole());
        System.out.println("Hourly Rate: $" + employee.getHourlyRate());
        System.out.println("Hours Worked: " + employee.getHoursWorked());
        System.out.println("Overtime Hours: " + employee.getOvertimeHours());
        System.out.println("Bank Account Info: " + (employee.getBankAccountInfo() != null ? employee.getBankAccountInfo() : "Not provided"));

        System.out.println("Is this information correct? (yes/no)");
        String response = scanner.nextLine().trim().toLowerCase();

        if ("yes".equals(response)) {
            System.out.println("Information confirmed.");
        } else if ("no".equals(response)) {
            System.out.println("Please specify which field you'd like to update:");
            System.out.println("1. Hourly Rate");
            System.out.println("2. Hours Worked");
            System.out.println("3. Overtime Hours");
            System.out.println("4. Bank Account Info");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter new Hourly Rate: ");
                    double newHourlyRate = scanner.nextDouble();
                    scanner.nextLine();
                    employee.setHourlyRate(newHourlyRate);
                    break;
                case 2:
                    System.out.print("Enter new Hours Worked: ");
                    double newHoursWorked = scanner.nextDouble();
                    scanner.nextLine();
                    employee.setHoursWorked(newHoursWorked);
                    break;
                case 3:
                    System.out.print("Enter new Overtime Hours: ");
                    double newOvertimeHours = scanner.nextDouble();
                    scanner.nextLine();
                    employee.setOvertimeHours(newOvertimeHours);
                    break;
                case 4:
                    System.out.print("Enter new Bank Account Info: ");
                    String newBankInfo = scanner.nextLine();
                    employee.setBankAccountInfo(newBankInfo);
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
            updateEmployee(employee);
            System.out.println("Information updated.");
        } else {
            System.out.println("Invalid response. Please enter 'yes' or 'no'.");
        }
    }

    private boolean isBankInfoValid(String bankInfo) {
        return bankInfo != null && !bankInfo.isEmpty();
    }

    private void handleBankInfoIssue(Employee employee, Scanner scanner) throws IOException {
        System.out.println("Bank information is outdated or incorrect. Contacting employee...");
        System.out.print("Please enter updated bank info: ");
        String updatedBankInfo = scanner.nextLine();
        employee.setBankAccountInfo(updatedBankInfo);
        updateEmployee(employee);
    }



}
