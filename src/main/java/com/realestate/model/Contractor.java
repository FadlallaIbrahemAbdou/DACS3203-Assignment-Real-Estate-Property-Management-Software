package realestate.model;

import java.sql.Timestamp;

public class Contractor {
    private int contractorId;
    private String companyName;
    private String contactPerson;
    private String phone;
    private String serviceType;
    private Timestamp createdAt;

    public Contractor(){}

    public Contractor(String companyName, String contactPerson, String phone, String serviceType) {
        this.companyName = companyName;
        this.contactPerson = contactPerson;
        this.phone = phone;
        this.serviceType = serviceType;
    }

    public int getContractorId() {
        return contractorId;
    }

    public void setContractorId(int contractorId) {
        this.contractorId = contractorId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Contractor{" +
                "companyName='" + companyName + '\'' +
                ", serviceType='" + serviceType + '\'' +
                '}';
    }
}
