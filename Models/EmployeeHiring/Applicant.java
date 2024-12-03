package Models.EmployeeHiring;

import Models.Person;

import java.util.Map;


public class Applicant extends Person {
    private int yearsOfExperience;
    private String education;
    private String jobStats;
    private String bankInfo;


    public Applicant(int id, String name, String contactInfo, int yearsOfExperience, String education, String jobStats, String bankInfo) {
        super(id, name, contactInfo);
        this.jobStats = jobStats;
        this.yearsOfExperience = yearsOfExperience;
        this.education = education;
        this.bankInfo = bankInfo;
    }

    public Applicant(){
        super();
    }

    // Getters
    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public String getEducation() {
        return education;
    }

    public String getJobStats() {
        return jobStats;
    }

    public String getBankInfo(){
        return bankInfo;
    }

    // Setters
    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setJobStats(String jobStats) {
        this.jobStats = jobStats;
    }

    public void setBankInfo(String bankInfo){
        this.bankInfo = bankInfo;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap(); // Get fields from Applicant

        // Manually add inherited fields from Person
        map.put("id", getId());
        map.put("name", getName());
        map.put("contactInfo", getContactInfo());

        return map;
    }

    @Override
    public Applicant fromMap(Map<String, Object> map) {
        int id = ((Number) map.get("id")).intValue(); // Safely cast to int
        String name = (String) map.get("name");
        String contactInfo = (String) map.get("contactInfo");
        int yearsOfExperience = ((Number) map.get("yearsOfExperience")).intValue(); // Safely cast to int
        String education = (String) map.get("education");
        String jobStats = (String) map.get("jobStats");
        String bankInfo = (String) map.get("bankInfo");

        return new Applicant(id, name, contactInfo, yearsOfExperience, education, jobStats, bankInfo);
    }

}

