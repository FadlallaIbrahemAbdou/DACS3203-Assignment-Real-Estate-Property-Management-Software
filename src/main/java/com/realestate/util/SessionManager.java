package realestate.util;

import realestate.model.User;

public class SessionManager {
    private static User currentUser;

    public static void setCurrentUser(User currentUser) {
        SessionManager.currentUser = currentUser;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    
    public static void clearSession(){
        currentUser= null;
    }

    public static boolean isLoggedIn(){
        return currentUser != null;
    }

    public static boolean isAdmin(){
        return currentUser != null && currentUser.isAdmin();
    }
}
