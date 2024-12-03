package Controllers;

import Models.EmployeeHiring.Applicant;
import Models.EmployeeHiring.JobPosting;
import Models.EmployeeHiring.Employee;
import Storage.StorageHelper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ApplicantController {

    private StorageHelper applicantStorageHelper;
    private StorageHelper jobPostingStorageHelper;
    private StorageHelper employeeStorageHelper;
    private final String APPLICANTS_NAME = "applicants";
    private final String JOB_POSTINGS_NAME = "jobPostings";
    private final String EMPLOYEES_NAME = "employees";
    private int nextApplicantId;
    private int nextEmployeeId;

    public ApplicantController(String baseDirectory) throws IOException {
        this.applicantStorageHelper = new StorageHelper(baseDirectory, APPLICANTS_NAME);
        this.jobPostingStorageHelper = new StorageHelper(baseDirectory, JOB_POSTINGS_NAME);
        this.employeeStorageHelper = new StorageHelper(baseDirectory, EMPLOYEES_NAME);
        this.nextApplicantId = 1;
        this.nextEmployeeId = 1;
    }

    public void createApplicant(Applicant applicant) throws IOException {
        applicant.setId(nextApplicantId);
        nextApplicantId++;
        applicantStorageHelper.getStore(APPLICANTS_NAME).save(String.valueOf(applicant.getId()), applicant.toMap());
    }

    public Applicant getApplicant(int id) throws IOException {
        Map<String, Object> data = applicantStorageHelper.getStore(APPLICANTS_NAME).load(String.valueOf(id));
        return data != null ? (Applicant) new Applicant().fromMap(data) : null;
    }

    public void updateApplicant(Applicant applicant) throws IOException {
        applicantStorageHelper.getStore(APPLICANTS_NAME).save(String.valueOf(applicant.getId()), applicant.toMap());
    }

    public void deleteApplicant(int id) throws IOException {
        applicantStorageHelper.getStore(APPLICANTS_NAME).delete(String.valueOf(id));
    }

    public void createJobPosting(JobPosting jobPosting) throws IOException {
        jobPostingStorageHelper.getStore(JOB_POSTINGS_NAME).save(String.valueOf(jobPosting.getId()), jobPosting.toMap());
    }

    public JobPosting getJobPosting(int id) throws IOException {
        Map<String, Object> data = jobPostingStorageHelper.getStore(JOB_POSTINGS_NAME).load(String.valueOf(id));
        return data != null ? new JobPosting().fromMap(data) : null;
    }

    public void updateJobPosting(JobPosting jobPosting) throws IOException {
        jobPostingStorageHelper.getStore(JOB_POSTINGS_NAME).save(String.valueOf(jobPosting.getId()), jobPosting.toMap());
    }

    public void deleteJobPosting(int id) throws IOException {
        jobPostingStorageHelper.getStore(JOB_POSTINGS_NAME).delete(String.valueOf(id));
    }

    public void createEmployee(Employee employee) throws IOException {
        employee.setId(nextEmployeeId);
        nextEmployeeId++;
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

    public void applyToJobPosting(Scanner scanner) throws IOException {
        System.out.print("Enter Applicant Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Contact Information: ");
        String contactInfo = scanner.nextLine();

        System.out.print("Enter Years of Experience: ");
        int yearsOfExperience = Integer.parseInt(scanner.next());
        scanner.nextLine();

        System.out.print("Enter Education Level (e.g., Bachelor's, Master's): ");
        String education = scanner.nextLine();

        System.out.print("Enter Bank Account Information: ");
        String bankInfo = scanner.nextLine();

        Map<String, Integer> jobStats = new HashMap<>();
        System.out.println("Enter Job Stats (e.g., Skill1: Level, Skill2: Level). Type 'done' to finish:");
        while (true) {
            System.out.print("Enter skill (or type 'done' to finish): ");
            String skill = scanner.nextLine();
            if ("done".equalsIgnoreCase(skill)) {
                break;
            }

            System.out.print("Enter level for " + skill + ": ");
            int level = Integer.parseInt(scanner.nextLine().trim());
            jobStats.put(skill, level);
        }

        Applicant applicant = new Applicant(nextApplicantId, name, contactInfo, yearsOfExperience, education, jobStats.toString(), bankInfo);
        createApplicant(applicant);

        System.out.print("Enter Job Posting ID to apply to: ");
        int jobPostingId = scanner.nextInt();


        JobPosting jobPosting = getJobPosting(jobPostingId);

        if (jobPosting == null) {
            System.out.println("Job posting not found.");
            return;
        }

        jobPosting.addApplicantIDs(applicant.getId());
        updateJobPosting(jobPosting);
        System.out.println(applicant.getName() + " has applied to " + jobPosting.getTitle());
    }

    public void acceptOrDeclineJobOffer(Scanner scanner) throws IOException {
        System.out.print("Enter Applicant ID: ");
        int applicantId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter Job Posting ID: ");
        int jobPostingId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        Applicant applicant = getApplicant(applicantId);
        JobPosting jobPosting = getJobPosting(jobPostingId);

        if (applicant == null) {
            System.out.println("Applicant not found.");
            return;
        }

        if (jobPosting == null) {
            System.out.println("Job posting not found.");
            return;
        }

        if(applicant.getId() != jobPosting.getSelectedCandidateId()){
            System.out.println("You haven't been offered this position");
            return;
        }

        System.out.print("Do you accept the job offer? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes")) {
            System.out.println(applicant.getName() + " has accepted the job offer for " + jobPosting.getTitle());

            // Create and store a new Employee record
            Employee newEmployee = new Employee(
                    nextEmployeeId,
                    applicant.getName(),
                    applicant.getContactInfo(),
                    jobPosting.getTitle(),
                    "Employed",
                    jobPosting.getHourlyRate(),
                    1, // hoursWorked
                    1, // overtimeHours
                    0,
                    applicant.getBankInfo(),
                    100
            );

            createEmployee(newEmployee);


            deleteJobPosting(jobPostingId);

        } else if (response.equals("no")) {
            System.out.println(applicant.getName() + " has declined the job offer for " + jobPosting.getTitle());
        } else {
            System.out.println("Invalid response. Please answer 'yes' or 'no'.");
        }
    }

}
