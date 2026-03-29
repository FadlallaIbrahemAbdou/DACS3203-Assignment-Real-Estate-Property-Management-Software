package realestate.model;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String fullName;
    private String username;
    private String email;
    private String passwordHash;
    private String role;
    private String status;
    private Timestamp createdAt;

    public User() {}

    public User(String fullName, String username, String email, String passwordHash, String role, String status) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.status = status;
    }

    public String getFullName() {
        return fullName;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAdmin(){
        return "ADMIN".equals(role);
    }
    public boolean isActive(){
        return "ACTIVE".equals(status);
    }
}
