package realestate.service;

import realestate.dao.ContractorDAO;
import realestate.dao.MaintenanceRequestDAO;
import realestate.model.Contractor;
import realestate.model.MaintenanceRequest;
import realestate.security.AuditLogger;
import realestate.security.ValidationUtil;
import java.util.List;

public class MaintainceService {

    private final ContractorDAO contractorDAO = new ContractorDAO();
    private final MaintenanceRequestDAO requestDAO = new MaintenanceRequestDAO();

    public String addContractor(Contractor contractor) {
        AccessControlService.requireLogin();

        if (ValidationUtil.isNullOrEmpty(contractor.getCompanyName())){
            return "Company name is required.";
        }
        if (ValidationUtil.isNullOrEmpty(contractor.getContactPerson())){
            return "Contact person is required.";
        }
        if (!ValidationUtil.isValidPhone(contractor.getPhone())){
            return "Valid phone number is required.";
        }
        if (ValidationUtil.isNullOrEmpty(contractor.getServiceType())){
            return "Service type is required.";
        }

        boolean ok = contractorDAO.insert(contractor);
        if (ok) {
            AuditLogger.log("CREATE_CONTRACTOR", "Contractor", contractor.getContractorId(), "Created contractor: " + contractor.getCompanyName());
            return null;
        }
        return "Failed to add contractor.";
    }

    public String updateContractor(Contractor contractor) {
        AccessControlService.requireLogin();

        if (ValidationUtil.isNullOrEmpty(contractor.getCompanyName())){
            return "Company name is required.";
        }
        if (ValidationUtil.isNullOrEmpty(contractor.getContactPerson())){
            return "Contact person is required.";
        }
        if (!ValidationUtil.isValidPhone(contractor.getPhone())){
            return "Valid phone number is required.";
        }
        if (ValidationUtil.isNullOrEmpty(contractor.getServiceType())){
            return "Service type is required.";
        }

        boolean ok = contractorDAO.update(contractor);
        if (ok) {
            AuditLogger.log("UPDATE_CONTRACTOR", "Contractor", contractor.getContractorId(), "Updated contractor: " + contractor.getCompanyName());
            return null;
        }
        return "Failed to update contractor.";
    }

    public List<Contractor> listContractors() {
        AccessControlService.requireLogin();
        return contractorDAO.findAll();
    }

    public String createRequest(MaintenanceRequest request) {
        AccessControlService.requireLogin();

        if (ValidationUtil.isNullOrEmpty(request.getTitle())){
            return "Title is required.";
        }
        if (ValidationUtil.isNullOrEmpty(request.getDescription())){
            return "Description is required.";
        }
        if (request.getRequestDate() == null){
            return "Request date is required.";
        }
        if (!ValidationUtil.isValidStatus(request.getPriority(), "LOW", "MEDIUM", "HIGH")) {
            return "Invalid priority value.";
        }
        request.setStatus("OPEN");
        boolean ok = requestDAO.insert(request);
        if (ok) {
            AuditLogger.log("CREATE_MAINTENANCE", "MaintenanceRequest", request.getRequestId(), "Created request: " + request.getTitle() + " for unit ID " + request.getUnitId());
            return null;
        }
        return "Failed to create maintenance request.";
    }

    public String updateRequestStatus(int requestId, String newStatus) {
        AccessControlService.requireLogin();

        if (!ValidationUtil.isValidStatus(newStatus, "OPEN", "IN_PROGRESS", "COMPLETED")) {
            return "Invalid status value.";
        }
        boolean ok = requestDAO.updateStatus(requestId, newStatus);
        if (ok) {
            AuditLogger.log("UPDATE_MAINTENANCE_STATUS", "MaintenanceRequest", requestId, "Status changed to " + newStatus);
            return null;
        }
        return "Failed to update request status.";
    }

    public String updateRequest(MaintenanceRequest request) {
        AccessControlService.requireLogin();

        if (ValidationUtil.isNullOrEmpty(request.getTitle())){
            return "Title is required.";
        }
        if (ValidationUtil.isNullOrEmpty(request.getDescription())){
            return "Description is required.";
        }
        boolean ok = requestDAO.update(request);
        if (ok) {
            AuditLogger.log("UPDATE_MAINTENANCE", "MaintenanceRequest", request.getRequestId(), "Updated request: " + request.getTitle());
            return null;
        }
        return "Failed to update maintenance request.";
    }

    public List<MaintenanceRequest> listRequests() {
        AccessControlService.requireLogin();
        return requestDAO.findAll();
    }
}
