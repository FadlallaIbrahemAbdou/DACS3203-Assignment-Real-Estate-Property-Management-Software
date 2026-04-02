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

    public String login(String username, String password) {
        System.out.println("Trying login for username: [" + username + "]");

        if (ValidationUtil.isNullOrEmpty(username) || ValidationUtil.isNullOrEmpty(password)) {
            return "Username and password are required.";
        }

        if (LoginAttemptService.isBlocked(username)) {
            return "Account temporarily locked due to too many failed attempts. Please try again later.";
        }

        User user = userDAO.findByUsername(username);

        System.out.println("User found: " + (user != null));

        if (user != null) {
            System.out.println("DB username: " + user.getUsername());
            System.out.println("DB status: " + user.getStatus());
            System.out.println("DB role: " + user.getRole());
            System.out.println("Password matches hash: " + PasswordUtil.verifyPassword(password, user.getPasswordHash()));
        }

        if (user == null || !PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            LoginAttemptService.recordFailedAttempt(username);
            AuditLogger.log("LOGIN_FAILED", "User", "Failed login attempt for username: " + username);
            return "Invalid username or password.";
        }

        if (!user.isActive()) {
            AuditLogger.log("LOGIN_FAILED", "User", user.getUserId(), "Inactive account login attempt");
            return "Invalid username or password.";
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
