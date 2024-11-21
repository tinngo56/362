package Models;

import java.time.LocalDateTime;

public class MaintenanceRequest extends Mappable<MaintenanceRequest> {
    private String requestId;
    private String roomNumber;
    private String issueDescription;
    private String priority;
    private String status;
    private LocalDateTime reportedTime;
    private String reportedBy;

    public MaintenanceRequest() {
        this.reportedTime = LocalDateTime.now();
        this.status = "PENDING";
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setIssueDescription(String issues) {
        this.issueDescription = issues;
    }

    public void setReportedBy(String name) {
        this.reportedBy = name;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}