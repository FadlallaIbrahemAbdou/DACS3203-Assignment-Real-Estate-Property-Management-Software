package realestate.service;

import realestate.dao.LeaseDAO;
import realestate.dao.RenterDAO;
import realestate.dao.UnitDAO;
import realestate.model.Lease;
import realestate.model.Renter;
import realestate.model.Unit;
import realestate.security.AuditLogger;
import realestate.security.ValidationUtil;
import java.util.List;

public class RenterLeaseService {

    private final RenterDAO renterDAO = new RenterDAO();
    private final LeaseDAO leaseDAO = new LeaseDAO();
    private final UnitDAO unitDAO = new UnitDAO();

    public String addRenter(Renter renter) {
        AccessControlService.requireLogin();

        if (ValidationUtil.isNullOrEmpty(renter.getFullName())){
            return "Renter full name is required.";
        }
        if (!ValidationUtil.isValidPhone(renter.getPhone())){
            return "Valid phone number is required.";
        }
        if (!ValidationUtil.isValidEmail(renter.getEmail())){
            return "Invalid email format.";
        }
        boolean ok = renterDAO.insert(renter);
        if (ok) {
            AuditLogger.log("CREATE_RENTER", "Renter", renter.getRenterId(), "Created renter: " + renter.getFullName());
            return null;
        }
        return "Failed to add renter.";
    }

    public String updateRenter(Renter renter) {
        AccessControlService.requireLogin();

        if (ValidationUtil.isNullOrEmpty(renter.getFullName())){
            return "Renter full name is required.";
        }
        if (!ValidationUtil.isValidPhone(renter.getPhone())){
            return "Valid phone number is required.";
        }
        if (!ValidationUtil.isValidEmail(renter.getEmail())){
            return "Invalid email format.";
        }
        boolean ok = renterDAO.update(renter);
        if (ok) {
            AuditLogger.log("UPDATE_RENTER", "Renter", renter.getRenterId(), "Updated renter: " + renter.getFullName());
            return null;
        }
        return "Failed to update renter.";
    }

    public List<Renter> listRenters() {
        AccessControlService.requireLogin();
        return renterDAO.findAll();
    }

    public String createLease(Lease lease) {
        AccessControlService.requireLogin();
        if (!ValidationUtil.isDateAfter(lease.getStartDate(), lease.getEndDate())) {
            return "End date must be after start date.";
        }
        if (!ValidationUtil.isPositiveAmount(lease.getMonthlyRent())) {
            return "Monthly rent must be positive.";
        }
        Unit unit = unitDAO.findById(lease.getUnitId());
        if (unit == null){
            return "Selected unit does not exist.";
        }
        if (!"AVAILABLE".equals(unit.getStatus())) {
            return "Unit is not available. Current status: " + unit.getStatus();
        }

        Lease existing = leaseDAO.findActiveByUnit(lease.getUnitId());
        if (existing != null) {
            return "Unit already has an active lease.";
        }
        lease.setStatus("ACTIVE");
        boolean ok = leaseDAO.insert(lease);
        if (!ok){
            return "Failed to create lease.";
        }
        unitDAO.updateStatus(lease.getUnitId(), "OCCUPIED");
        AuditLogger.log("CREATE_LEASE", "Lease", lease.getLeaseId(), "Created lease for unit ID " + lease.getUnitId() + ", renter ID " + lease.getRenterId());
        return null;
    }

    public List<Lease> listLeases() {
        AccessControlService.requireLogin();
        return leaseDAO.findAll();
    }
    public List<Lease> listActiveLeases() {
        AccessControlService.requireLogin();
        return leaseDAO.findActiveLeases();
    }
}
