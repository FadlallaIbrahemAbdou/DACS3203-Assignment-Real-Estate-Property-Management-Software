package realestate.model;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;


public class Unit {
    private int unitId;
    private int propertyId;
    private String unitNumber;
    private BigDecimal rentAmount;
    private String status;
    private Timestamp createdAt;
    public String propertyName;

    public Unit(){}

    public Unit(int propertyId, String unitNumber, BigDecimal rentAmount, String status) {
        this.propertyId = propertyId;
        this.unitNumber = unitNumber;
        this.rentAmount = rentAmount;
        this.status = status;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public BigDecimal getRentAmount() {
        return rentAmount;
    }

    public void setRentAmount(BigDecimal rentAmount) {
        this.rentAmount = rentAmount;
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

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String toString() {
        return unitNumber + (propertyName != null ? " (" + propertyName + ")" : "");
    }
}
