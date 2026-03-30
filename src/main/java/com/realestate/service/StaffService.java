package realestate.service;

import realestate.dao.AuditLogDAO;
import realestate.dao.UserDAO;
import realestate.model.User;
import realestate.security.AuditLogger;
import realestate.security.PasswordUtil;
import realestate.security.ValidationUtil;

import java.util.List;

public class StaffService {
    private final UserDAO userDAO = new UserDAO();

    public String createStaff(String fullName, String username, String email, String password){
        AccessControlService.requireAdmin();

        if (ValidationUtil.isNullOrEmpty(fullName)){
            return "Full name is required please";
        } if (!ValidationUtil.isValidUsername(username)){
            return "Username must be 3-50 (Letters, Numbers or Underscores)";
        } if (!ValidationUtil.isValidPassword(password)){
            return "Password must be atleast 6 characters";
        } if (!ValidationUtil.isValidEmail(email)){
            return "Invalid Email format";
        } if (userDAO.existsByUsername(username)){
            return "Username is taken, try another username";
        }

        String hash = PasswordUtil.hashPassword(password);
        User staff = new User(fullName, username, email, hash, "STAFF", "ACTIVE");
        boolean ok = userDAO.insert(staff);
        if (ok){
            AuditLogger.log("CREATE_STAFF","User",staff.getUserId(),"Created staff: " +username);
            return null;
        }
        return "Failed to create staff account.";
    }

    public String updateStaff(User staff, String newPassword){
        AccessControlService.requireAdmin();

        if (ValidationUtil.isNullOrEmpty(staff.getFullName())){
            return "Full name is required";
        } if (!ValidationUtil.isValidEmail(staff.getEmail())){
            return "Invalid email format.";
        }

        boolean ok = userDAO.update(staff);
        if (!ok){
            return "Failed to update staff account.";
        } if (newPassword != null && !newPassword.isEmpty()){
            if(!ValidationUtil.isValidPassword(newPassword)){
                return "Password must be atleast 6 characters long";
            }
            String hash = PasswordUtil.hashPassword(newPassword);
            userDAO.updatePassword(staff.getUserId(), hash);
        }
        AuditLogger.log("UPDATE_STAFF","User",staff.getUserId(),"Updated Staff: "+ staff.getUsername());
        return null;
    }

    public String deactivateStaff(int userId){
        AccessControlService.requireAdmin();

        boolean ok = userDAO.deactivate(userId);
        if (ok){
            AuditLogger.log("DEACTIVATE_STAFF", "User", userId, "Deactivated staff user ID: "+ userId);
            return null;
        }
        return "Failed to deactivate staff account";

    }

    public List<User> listAllStaff(){
        AccessControlService.requireAdmin();
        return userDAO.findAllStaff();
    }
}
