package Controllers;

import Models.Booking;
import Models.EmployeeHiring.Applicant;
import Models.EmployeeHiring.JobPosting;
import Storage.StorageHelper;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class HiringController {

    private StorageHelper jobPostingsStorageHelper;
    private StorageHelper applicantStorageHelper;
    private StorageHelper oldJobPostingsStorageHelper;
    private final String POSTINGS_NAME = "jobPostings";
    private final String APPLICANTS_NAME = "applicants";
    private final String OLD_POSTINGS_NAME = "oldJobPostings";
    private int nextJobPostingId;
    private int nextOldJobPostingId;

    public HiringController(String baseDirectory) throws IOException {
        this.jobPostingsStorageHelper = new StorageHelper(baseDirectory, POSTINGS_NAME);
        this.applicantStorageHelper = new StorageHelper(baseDirectory, APPLICANTS_NAME);
        this.oldJobPostingsStorageHelper = new StorageHelper(baseDirectory, OLD_POSTINGS_NAME);
        this.nextJobPostingId = 1;
        this.nextOldJobPostingId = 1;
    }

    public void createJobPosting(JobPosting jobPosting) throws IOException {
        jobPosting.setId(nextJobPostingId);
        nextJobPostingId++;
        jobPostingsStorageHelper.getStore(POSTINGS_NAME).save(String.valueOf(jobPosting.getId()), jobPosting.toMap());
    }

    public JobPosting getJobPosting(int id) throws IOException {
        Map<String, Object> data = jobPostingsStorageHelper.getStore(POSTINGS_NAME).load(String.valueOf(id));
        return data != null ? new JobPosting().fromMap(data) : null;
    }

    public void updateJobPosting(JobPosting jobPosting) throws IOException {
        jobPostingsStorageHelper.getStore(POSTINGS_NAME).save(String.valueOf(jobPosting.getId()), jobPosting.toMap());
    }

    public void deleteJobPosting(int id) throws IOException {
        createOldJobPosting(getJobPosting(id));
        jobPostingsStorageHelper.getStore(POSTINGS_NAME).delete(String.valueOf(id));
    }




    public void createApplicant(Applicant applicant) throws IOException {
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



    public void createOldJobPosting(JobPosting jobPosting) throws IOException {
        jobPosting.setId(nextOldJobPostingId);
        nextOldJobPostingId++;
        oldJobPostingsStorageHelper.getStore(OLD_POSTINGS_NAME).save(String.valueOf(jobPosting.getId()), jobPosting.toMap());
    }


    public void requestNewHire(Scanner scanner) throws IOException {
        System.out.print("Job Title: ");
        String title = scanner.nextLine();

        System.out.print("Responsibilities: ");
        String responsibilities = scanner.nextLine();

        System.out.print("Hourly Rate: ");
        double hourlyRate = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Hours per Week: ");
        double hoursAWeek = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Years of Experience Required: ");
        int requiredExperience = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Education Level Required (e.g., Bachelor's, Master's): ");
        String requiredEducation = scanner.nextLine();

        String qualifications = requiredExperience + " years of experience, " + requiredEducation;

        int jobId = 1;
        JobPosting jobPosting = new JobPosting(jobId, title, qualifications, responsibilities, hourlyRate, hoursAWeek,"", "", 0);

        createJobPosting(jobPosting);
        System.out.println("The HR Department has created the job posting with ID: " + jobId);
    }

    public void shortListApplicants(Scanner scanner) throws IOException {
        System.out.print("Enter Job Posting ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        JobPosting jobPosting = getJobPosting(id);
        if (jobPosting == null) {
            System.out.println("Job posting not found with ID: " + id);
            return;
        }


        String[] qualificationsParts = jobPosting.getQualifications().split(", ");
        int requiredExperience = Integer.parseInt(qualificationsParts[0].replaceAll("[^0-9]", ""));
        String requiredEducation = qualificationsParts[1];

        int[] applicantIds = jobPosting.getApplicantIDsArray();
        for (int applicantId : applicantIds) {
            Applicant applicant = getApplicant(applicantId);
            if (applicant != null){
                if (applicant.getYearsOfExperience() < requiredExperience ||
                    !applicant.getEducation().equalsIgnoreCase(requiredEducation)) {
                    System.out.println("HR Removed Applicant: " + applicant.getName() + " because they didn't meet requirements");
                }else{
                    jobPosting.addShortlistedApplicantIDs(applicantId);
                    updateJobPosting(jobPosting);
                    System.out.println("HR added Applicant: " + applicant.getName());
                }
            } else {
                System.out.println("applicant not found");
            }
        }
    }
    public void scheduleInterview(Scanner scanner) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Suggest an interview date (e.g., one week from today)
        LocalDate suggestedDate = LocalDate.now().plus(1, ChronoUnit.WEEKS);
        System.out.println("Suggested interview date: " + suggestedDate.format(formatter));

        // Ask the user if the date works
        System.out.print("Does this date work for you? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        // Process the response
        if (response.equals("yes")) {
            System.out.println("Great! The interview is scheduled for " + suggestedDate.format(formatter) + ".");
            // Additional logic for confirming the date could go here, such as storing it
        } else if (response.equals("no")) {
            System.out.println("Okay, please contact us to reschedule at your earliest convenience.");
            // Logic for rescheduling or alternative actions can go here
        } else {
            System.out.println("Invalid response. Please reply with 'yes' or 'no'.");
        }
    }

    public void selectCandidate(Scanner scanner) throws IOException {
        System.out.print("Enter Job Posting ID: ");
        int jobPostingId = scanner.nextInt();
        scanner.nextLine();
        JobPosting jobPosting = getJobPosting(jobPostingId);

        if (jobPosting != null) {
            System.out.print("Enter id of candidate to offer job: ");
            int id = scanner.nextInt();


            if (getApplicant(id) != null) {
                jobPosting.setSelectedCandidateId(id);
                System.out.println("Offer extended to " + getApplicant(id).getName());
                updateJobPosting(jobPosting);
            } else {
                System.out.println("Candidate not found.");
            }
        } else {
            System.out.println("Job Posting not found.");
        }
    }

    public void cancelHiringProcess(Scanner scanner) throws IOException {
        System.out.print("Enter Job Posting ID to cancel hiring: ");
        int jobPostingId = scanner.nextInt();
        scanner.nextLine();

        JobPosting jobPosting = getJobPosting(jobPostingId);
        if (jobPosting != null) {
            deleteJobPosting(jobPostingId);
            System.out.println("Job Posting " + jobPosting.getTitle() + " has been removed and hiring process canceled.");
        } else {
            System.out.println("Job Posting not found.");
        }

    }

    public void interviewShortlistedApplicants(Scanner scanner) throws IOException {
        System.out.print("Enter Job Posting ID for interviews: ");
        int jobPostingId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        JobPosting jobPosting = getJobPosting(jobPostingId);

        if (jobPosting == null) {
            System.out.println("Job posting not found.");
            return;
        }


        int[] shortlistedApplicantIds = jobPosting.getShortListedApplicantIDsArray();

        if (shortlistedApplicantIds.length == 0) {
            System.out.println("No applicants have been shortlisted for this job posting.");
            return;
        }

        for (int applicantId : shortlistedApplicantIds) {
            Applicant applicant = getApplicant(applicantId);

            if (applicant != null) {
                System.out.println("Interview with: " + applicant.getName());

                // Display applicant's job stats if available
                Map<String, Integer> jobStats = parseJobStats(applicant.getJobStats());
                if (jobStats != null && !jobStats.isEmpty()) {
                    System.out.println("Applicant Job Stats:");
                    for (Map.Entry<String, Integer> stat : jobStats.entrySet()) {
                        System.out.println(stat.getKey() + ": " + stat.getValue());
                    }
                } else {
                    System.out.println("No job stats available for this applicant.");
                }

                // Here you could add code to record interview results if needed
            } else {
                System.out.println("Applicant with ID " + applicantId + " not found.");
            }
        }

        // Save any updates to the job posting (if needed)
        updateJobPosting(jobPosting);
    }

    private Map<String, Integer> parseJobStats(String jobStatsString) {
        Map<String, Integer> jobStats = new HashMap<>();
        if (jobStatsString != null && !jobStatsString.isEmpty()) {
            String[] stats = jobStatsString.replaceAll("[{}]", "").split(", ");
            for (String stat : stats) {
                String[] keyValue = stat.split("=");
                if (keyValue.length == 2) {
                    try {
                        jobStats.put(keyValue[0], Integer.parseInt(keyValue[1]));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid stat format for " + keyValue[0]);
                    }
                }
            }
        }
        return jobStats;
    }
}
