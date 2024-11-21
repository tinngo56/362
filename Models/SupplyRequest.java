package Models;

public class SupplyRequest extends Mappable<SupplyRequest> {
    private String requestId;
    private String item;
    private double quantityNeeded;
    private String requestedBy;
    private String priority;
    private String status;
 
    public SupplyRequest() {
    }
 
    public String getRequestId() {
        return requestId;
    }
 
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
 
    public String getItem() {
        return item;
    }
 
    public void setItem(String item) {
        this.item = item;
    }
 
    public double getQuantityNeeded() {
        return quantityNeeded;
    }
 
    public void setQuantityNeeded(double quantityNeeded) {
        this.quantityNeeded = quantityNeeded;
    }
 
    public String getRequestedBy() {
        return requestedBy;
    }
 
    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }
 
    public String getPriority() {
        return priority;
    }
 
    public void setPriority(String priority) {
        this.priority = priority;
    }
 
    public String getStatus() {
        return status;
    }
 
    public void setStatus(String status) {
        this.status = status;
    }
 }
