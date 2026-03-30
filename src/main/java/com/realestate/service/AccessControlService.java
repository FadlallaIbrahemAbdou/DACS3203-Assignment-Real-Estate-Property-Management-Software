package realestate.service;

import realestate.util.SessionManager;

public class AccessControlService {

    public static boolean isAdmin(){
        return SessionManager.isAdmin();
    }

    public static  boolean isLoggedIn(){
        return SessionManager.isLoggedIn();
    }

    public static void requireAdmin() throws SecurityException{
        if (!isAdmin()){
            throw new SecurityException("Access denied, Admin privilege required.");
        }
    }

    public static void requireLogin() throws SecurityException{
        if (!isLoggedIn()){
            throw new SecurityException("Access denied, you have to be logged in first.");
        }
    }
}
