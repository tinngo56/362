package Models.EmployeeHiring;

import Models.Mappable;

public class JobPosting extends Mappable<JobPosting> {
    private int id;
    private String title;
    private String qualifications;
    private String responsibilities;
    private double hourlyRate;
    private double hoursAWeek;
    private double sallary;
    private String applicantIDs;
    private String shorlistedApplicantIDs;
    private int selectedCandidateId;

    public JobPosting(int id, String title, String qualifications, String responsibilities, double hourlyRate, double hoursAWeek, String applicantIDs, String shorlistedApplicantIDs,  int selectedCandidateId) {
        this.id = id;
        this.title = title;
        this.qualifications = qualifications;
        this.responsibilities = responsibilities;
        this.hourlyRate = hourlyRate;
        this.hoursAWeek = hoursAWeek;
        this.sallary = hourlyRate * hoursAWeek * 52.1429;
        this.applicantIDs = applicantIDs;
        this.shorlistedApplicantIDs = shorlistedApplicantIDs;
        this.selectedCandidateId = selectedCandidateId;
    }

    public JobPosting() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
        updateSallary();
    }

    public double getHoursAWeek() {
        return hoursAWeek;
    }

    public void setHoursAWeek(double hoursAWeek) {
        this.hoursAWeek = hoursAWeek;
        updateSallary();
    }

    public double getSallary() {
        return sallary;
    }

    private void updateSallary() {
        this.sallary = this.hourlyRate * this.hoursAWeek * 52.1429;
    }

    public int getSelectedCandidateId() {
        return selectedCandidateId;
    }

    public void setSelectedCandidateId(int selectedCandidate) {
        this.selectedCandidateId = selectedCandidate;
    }

    public void addApplicantIDs(int id) {
        this.applicantIDs += " " + id;
    }

    public int[] getApplicantIDsArray() {
        if (this.applicantIDs == null || this.applicantIDs.isEmpty()) {
            return new int[0];
        }
        String[] idStrings = this.applicantIDs.trim().split(" ");
        int[] ids = new int[idStrings.length];
        for (int i = 0; i < idStrings.length; i++) {
            ids[i] = Integer.parseInt(idStrings[i]);
        }
        return ids;
    }

    public void addShortlistedApplicantIDs(int id) {
        this.shorlistedApplicantIDs += " " + id;
    }

    public int[] getShortListedApplicantIDsArray() {
        if (this.shorlistedApplicantIDs == null || this.shorlistedApplicantIDs.isEmpty()) {
            return new int[0];
        }
        String[] idStrings = this.shorlistedApplicantIDs.trim().split(" ");
        int[] ids = new int[idStrings.length];
        for (int i = 0; i < idStrings.length; i++) {
            ids[i] = Integer.parseInt(idStrings[i]);
        }
        return ids;
    }


}