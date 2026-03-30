package realestate.service;

import realestate.dao.UserDAO;
import realestate.model.User;
import realestate.security.AuditLogger;
import realestate.security.LoginAttemptService;
import realestate.security.PasswordUtil;
import realestate.security.ValidationUtil;
import realestate.util.SessionManager;
public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public String login(String username, String password){
        if (ValidationUtil.isNullOrEmpty(username) || ValidationUtil.isNullOrEmpty(password)){
            return "Username and Password are required please";

        } if (LoginAttemptService.isBlocked(username)){
            return "Account has been locked due to too many failed attempts try again later";

        } User user = userDAO.findByUsername(username);

        if (user == null || PasswordUtil.verifyPassword(password, user.getPasswordHash())){
            LoginAttemptService.recordFailedAttempt(username);
            AuditLogger.log("LOGIN_FAILED", "User", "Failed login attempt for username: " + username);
            return "Invalid Username or Password";

        } if (!user.isActive()){
            AuditLogger.log("LOGIN_FAILED", "User", user.getUserId(), "Inactive account login attempt");
            return "Invalid Username or Password";
        }
        LoginAttemptService.resetAttempts(username);
        SessionManager.setCurrentUser(user);
        AuditLogger.log("LOGIN_SUCCESS", "User", user.getUserId(), "User logged in: " + user.getUsername());
        return null;
    }
    public void logout(){
        User user = SessionManager.getCurrentUser();
        if (user != null){
            AuditLogger.log("LOGOUT", "User", user.getUserId(), "User logged out: " + user.getUsername());
        }
        SessionManager.clearSession();
    }
}
