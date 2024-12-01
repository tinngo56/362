package Models.CompleteMaintenanceOrCleaningRequest;

import java.util.*;

public class WorkRequest {
    boolean isCleaningRequest;
    boolean resolved;
    boolean urgent;
    FacilityType facility;
    String issue;
    int id;

    public WorkRequest(){}

    public WorkRequest(boolean isCleaningRequest, boolean resolved, boolean urgent, FacilityType facility, String issue, int id) {
        this.isCleaningRequest = isCleaningRequest;
        this.resolved = resolved;
        this.urgent = urgent;
        this.facility = facility;
        this.issue = issue;
        this.id = id;
    }

    public Map<String, Object>  toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("isCleaningRequest", isCleaningRequest);
        map.put("resolved", resolved);
        map.put("urgent", urgent);
        switch (facility) {
            case POOl: {
                map.put("facility","pool");
                break;
            }
            case GAME_ROOM: {
                map.put("facility","gameRoom");
                break;
            }
            case GYM: {
                map.put("facility","gym");
                break;
            }
            case CONFERENCE_ROOM: {
                map.put("facility","conferenceRoom");
                break;
            }
        }
        map.put("issue", issue);
        return map;
    }

    public WorkRequest fromMap(Map<String, Object> map) {
        WorkRequest workRequest = new WorkRequest();

        workRequest.setId(((Number) map.get("id")).intValue());
        workRequest.setCleaningRequest((boolean) map.get("isCleaningRequest"));
        workRequest.setResolved((boolean) map.get("resolved"));
        workRequest.setUrgent((boolean) map.get("urgent"));

        String facilityString = (String) map.get("facility");
        switch (facilityString) {
            case "pool":
                workRequest.setFacility(FacilityType.POOl);
                break;
            case "gameRoom":
                workRequest.setFacility(FacilityType.GAME_ROOM);
                break;
            case "gym":
                workRequest.setFacility(FacilityType.GYM);
                break;
            case "conferenceRoom":
                workRequest.setFacility(FacilityType.CONFERENCE_ROOM);
                break;
            default:
            System.out.println("Invalid facility type in fomMap");
        }

        workRequest.setIssue((String) map.get("issue"));

        return workRequest;
    }

    @Override
    public String toString() {
        return "Work Request Details:" +
                "\n- Request ID: " + id +
                "\n- Type: " + (isCleaningRequest ? "Cleaning" : "Repair") +
                "\n- Status: " + (resolved ? "Resolved" : "Pending") +
                "\n- Urgency: " + (urgent ? "Urgent" : "Normal") +
                "\n- Facility: " + formatFacility(facility) +
                "\n- Issue: " + issue;
    }

    private String formatFacility(FacilityType facility) {
        return switch (facility) {
            case POOl -> "Pool";
            case GAME_ROOM -> "Game Room";
            case GYM -> "Gym";
            case CONFERENCE_ROOM -> "Conference Room";
            default -> "Unknown Facility";
        };
    }

    public boolean isCleaningRequest() {
        return isCleaningRequest;
    }

    public void setCleaningRequest(boolean cleaningRequest) {
        isCleaningRequest = cleaningRequest;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public FacilityType getFacility() {
        return facility;
    }

    public void setFacility(FacilityType facility) {
        this.facility = facility;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }
}