package realestate.security;

import realestate.dao.AuditLogDAO;
import realestate.model.AuditLog;
import realestate.util.SessionManager;


public class AuditLogger {

    private static final AuditLogDAO auditLogDAO = new AuditLogDAO();

    public static void log(String action, String entityName, Integer entityId, String details){
        Integer userId = null;
        if (SessionManager.getCurrentUser() != null){
            userId = SessionManager.getCurrentUser().getUserId();
        }
        AuditLog entry = new AuditLog(userId, action , entityName ,entityId, details);
        auditLogDAO.insert(entry);
    }

    public static void log(String action, String entityName, String details){
        log(action, entityName, null, details);
    }

}
