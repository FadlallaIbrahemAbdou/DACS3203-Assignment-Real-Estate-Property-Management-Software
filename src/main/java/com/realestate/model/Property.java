package realestate.model;

import java.sql.Timestamp;

public class Property {
    private int propertyId;
    private String propertyName;
    private String address;
    private String city;
    private String description;
    private Timestamp createdAt;


    public Property() {}

    public Property(String propertyName, String address, String city, String description) {
        this.propertyName = propertyName;
        this.address = address;
        this.city = city;
        this.description = description;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}


