package realestate.dao;

import realestate.model.AuditLog;
import realestate.util.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditLogDAO {

    public void insert(AuditLog log) {
        String sql= "INSERT INTO audit_logs (user_id, action, entity_name, entity_id, details) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (log.getUserId() != null) ps.setInt(1, log.getUserId());
            else ps.setNull(1, Types.INTEGER);
            ps.setString(2, log.getAction());
            ps.setString(3, log.getEntityName());
            if (log.getEntityId() != null) ps.setInt(4, log.getEntityId());
            else ps.setNull(4, Types.INTEGER);
            ps.setString(5, log.getDetails());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
    }

    public List<AuditLog> findAll() {
        List<AuditLog> list = new ArrayList<>();
        String sql = "SELECT a.*, u.full_name AS user_name FROM audit_logs a " +
                "LEFT JOIN users u ON a.user_id = u.user_id ORDER BY a.created_at DESC";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return list;
    }

    private AuditLog mapRow(ResultSet rs) throws SQLException {
        AuditLog log = new AuditLog();
        log.setLogId(rs.getInt("log_id"));
        log.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
        log.setAction(rs.getString("action"));
        log.setEntityName(rs.getString("entity_name"));
        log.setEntityId(rs.getObject("entity_id") != null ? rs.getInt("entity_id") : null);
        log.setDetails(rs.getString("details"));
        log.setCreatedAt(rs.getTimestamp("created_at"));
        try { log.setUserName(rs.getString("user_name")); }
        catch (SQLException ignored) {}
        return log;
    }
}
