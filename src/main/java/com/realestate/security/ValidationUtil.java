package realestate.security;

import java.math.BigDecimal;
import java.sql.Date;

public class ValidationUtil {

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email))
            return true;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean isValidPhone(String phone) {
        if (isNullOrEmpty(phone))
            return false;
        return phone.matches("^[0-9+\\-() ]{7,20}$");
    }

    public static boolean isPositiveAmount(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isDateAfter(Date start, Date end) {
        if (start == null || end == null)
            return false;
        return end.after(start);
    }

    public static boolean isValidUsername(String username) {
        if (isNullOrEmpty(username))
            return false;
        return username.matches("^[A-Za-z0-9_]{3,50}$");
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidRole(String role) {
        return "ADMIN".equals(role) || "STAFF".equals(role);
    }

    public static boolean isValidStatus(String status, String... allowedValues) {
        if (isNullOrEmpty(status))
            return false;
        for (String allowed : allowedValues) {
            if (allowed.equals(status))
                return true;
        }
        return false;
    }
}
