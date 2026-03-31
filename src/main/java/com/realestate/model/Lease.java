package realestate.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Lease {

    private int leaseId;
    private int renterId;
    private int unitId;
    private Date startDate;
    private Date endDate;
    private BigDecimal depositAmount;
    private BigDecimal monthlyRent;
    private String status;
    private Timestamp createdAt;

    private String renterName;
    private String unitNumber;

    public Lease(int renterId, int unitId, Date startDate, Date endDate, BigDecimal depositAmount, BigDecimal monthlyRent, String status) {
        this.renterId = renterId;
        this.unitId = unitId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.depositAmount = depositAmount;
        this.monthlyRent = monthlyRent;
        this.status = status;
    }

    public int getLeaseId() {
        return leaseId;
    }

    public void setLeaseId(int leaseId) {
        this.leaseId = leaseId;
    }

    public int getRenterId() {
        return renterId;
    }

    public void setRenterId(int renterId) {
        this.renterId = renterId;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public BigDecimal getMonthlyRent() {
        return monthlyRent;
    }

    public void setMonthlyRent(BigDecimal monthlyRent) {
        this.monthlyRent = monthlyRent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getRenterName() {
        return renterName;
    }

    public void setRenterName(String renterName) {
        this.renterName = renterName;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    @Override
    public String toString() {
        return "Lease{" +
                "leaseId=" + leaseId +
                '}';
    }
}
