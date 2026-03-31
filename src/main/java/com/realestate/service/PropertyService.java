package realestate.service;


import realestate.dao.PropertyDAO;
import realestate.dao.UnitDAO;
import realestate.model.Property;
import realestate.model.Unit;
import realestate.security.AuditLogger;
import realestate.security.ValidationUtil;
import java.math.BigDecimal;
import java.util.List;

public class PropertyService {

    private final PropertyDAO propertyDAO = new PropertyDAO();
    private final UnitDAO unitDAO = new UnitDAO();

    public String addProperty(Property property) {
        AccessControlService.requireLogin();

        if (ValidationUtil.isNullOrEmpty(property.getPropertyName())){
            return "Property name is required.";
        }
        if (ValidationUtil.isNullOrEmpty(property.getAddress())){
            return "Address is required.";
        }
        if (ValidationUtil.isNullOrEmpty(property.getCity())){
            return "City is required.";
        }
        boolean ok = propertyDAO.insert(property);
        if (ok) {
            AuditLogger.log("CREATE_PROPERTY", "Property", property.getPropertyId(), "Created property: " + property.getPropertyName());
            return null;
        }
        return "Failed to add property.";
    }

    public String updateProperty(Property property) {
        AccessControlService.requireLogin();

        if (ValidationUtil.isNullOrEmpty(property.getPropertyName())){
            return "Property name is required.";
        }
        if (ValidationUtil.isNullOrEmpty(property.getAddress())){
            return "Address is required.";
        }
        if (ValidationUtil.isNullOrEmpty(property.getCity())){
            return "City is required.";
        }

        boolean ok = propertyDAO.update(property);
        if (ok) {
            AuditLogger.log("UPDATE_PROPERTY", "Property", property.getPropertyId(), "Updated property: " + property.getPropertyName());
            return null;
        }
        return "Failed to update property.";
    }

    public List<Property> listProperties() {
        AccessControlService.requireLogin();
        return propertyDAO.findAll();
    }

    public List<Property> searchProperties(String keyword) {
        AccessControlService.requireLogin();
        if (ValidationUtil.isNullOrEmpty(keyword)){
            return propertyDAO.findAll();
        }
        return propertyDAO.search(keyword);
    }

    public String addUnit(Unit unit) {
        AccessControlService.requireLogin();

        if (ValidationUtil.isNullOrEmpty(unit.getUnitNumber())){
            return "Unit number is required.";
        }
        if (!ValidationUtil.isPositiveAmount(unit.getRentAmount())){
            return "Rent amount must be positive.";
        }
        if (unitDAO.existsByPropertyAndNumber(unit.getPropertyId(), unit.getUnitNumber())) {
            return "Unit number already exists in this property.";
        }

        boolean ok = unitDAO.insert(unit);
        if (ok) {
            AuditLogger.log("CREATE_UNIT", "Unit", unit.getUnitId(), "Created unit: " + unit.getUnitNumber() + " in property ID " + unit.getPropertyId());
            return null;
        }
        return "Failed to add unit.";
    }

    public String updateUnit(Unit unit) {
        AccessControlService.requireLogin();

        if (ValidationUtil.isNullOrEmpty(unit.getUnitNumber())){
            return "Unit number is required.";
        }
        if (!ValidationUtil.isPositiveAmount(unit.getRentAmount())){
            return "Rent amount must be positive.";
        }

        boolean ok = unitDAO.update(unit);
        if (ok) {
            AuditLogger.log("UPDATE_UNIT", "Unit", unit.getUnitId(), "Updated unit: " + unit.getUnitNumber());
            return null;
        }
        return "Failed to update unit.";
    }
    public List<Unit> listUnitsByProperty(int propertyId) {
        AccessControlService.requireLogin();
        return unitDAO.findByProperty(propertyId);
    }

    public List<Unit> listAllUnits() {
        AccessControlService.requireLogin();
        return unitDAO.findAll();
    }

    public List<Unit> listAvailableUnits() {
        AccessControlService.requireLogin();
        return unitDAO.findAvailable();
    }

}
