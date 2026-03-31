package realestate.model;

import java.sql.Date;
import java.sql.Timestamp;

public class MaintenanceRequest {
    private int requestId;
    private int unitId;
    private Integer contractorId;
    private String title;
    private String description;
    private String priority;
    private String status;
    private Date requestDate;
    private Date scheduledDate;
    private Timestamp createdAt;

    private String unitNumber;
    private String contractorName;

    public MaintenanceRequest(){}

    public MaintenanceRequest(int unitId, Integer contractorId, String title, String priority, String description, String status, Date requestDate, Date scheduledDate) {
        this.unitId = unitId;
        this.contractorId = contractorId;
        this.title = title;
        this.priority = priority;
        this.description = description;
        this.status = status;
        this.requestDate = requestDate;
        this.scheduledDate = scheduledDate;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Integer getContractorId() {
        return contractorId;
    }

    public void setContractorId(Integer contractorId) {
        this.contractorId = contractorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }
}
