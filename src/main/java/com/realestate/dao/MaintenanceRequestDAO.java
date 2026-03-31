package realestate.dao;

import realestate.model.MaintenanceRequest;
import realestate.util.DBconnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceRequestDAO {

    public boolean insert(MaintenanceRequest request) {
        String sql = "INSERT INTO maintenance_requests (unit_id, contractor_id, title, description, priority, status, request_date, scheduled_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, request.getUnitId());
            if (request.getContractorId() != null){
                ps.setInt(2, request.getContractorId());
            } else{
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, request.getTitle());
            ps.setString(4, request.getDescription());
            ps.setString(5, request.getPriority());
            ps.setString(6, request.getStatus());
            ps.setDate(7, request.getRequestDate());
            if (request.getScheduledDate() != null){
                ps.setDate(8, request.getScheduledDate());
            } else{
                ps.setNull(8, Types.DATE);
            }
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) request.setRequestId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return false;
    }

    public boolean updateStatus(int requestId, String status) {
        String sql = "UPDATE maintenance_requests SET status = ? WHERE request_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, requestId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return false;
    }

    public boolean update(MaintenanceRequest request) {
        String sql = "UPDATE maintenance_requests SET contractor_id = ?, title = ?, description = ?, priority = ?, status = ?, scheduled_date = ? WHERE request_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (request.getContractorId() != null){
                ps.setInt(1, request.getContractorId());
            } else{
                ps.setNull(1, Types.INTEGER);
            }
            ps.setString(2, request.getTitle());
            ps.setString(3, request.getDescription());
            ps.setString(4, request.getPriority());
            ps.setString(5, request.getStatus());
            if (request.getScheduledDate() != null){
                ps.setDate(6, request.getScheduledDate());
            } else{
                ps.setNull(6, Types.DATE);
            }
            ps.setInt(7, request.getRequestId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return false;
    }

    public List<MaintenanceRequest> findAll() {
        List<MaintenanceRequest> list = new ArrayList<>();
        String sql = "SELECT m.*, u.unit_number, c.company_name AS contractor_name FROM maintenance_requests m " +
                "JOIN units u ON m.unit_id = u.unit_id " +
                "LEFT JOIN contractors c ON m.contractor_id = c.contractor_id " +
                "ORDER BY m.created_at DESC";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return list;
    }

    private MaintenanceRequest mapRow(ResultSet rs) throws SQLException {
        MaintenanceRequest m = new MaintenanceRequest();
        m.setRequestId(rs.getInt("request_id"));
        m.setUnitId(rs.getInt("unit_id"));
        m.setContractorId(rs.getObject("contractor_id") != null ? rs.getInt("contractor_id") : null);
        m.setTitle(rs.getString("title"));
        m.setDescription(rs.getString("description"));
        m.setPriority(rs.getString("priority"));
        m.setStatus(rs.getString("status"));
        m.setRequestDate(rs.getDate("request_date"));
        m.setScheduledDate(rs.getDate("scheduled_date"));
        m.setCreatedAt(rs.getTimestamp("created_at"));
        try { m.setUnitNumber(rs.getString("unit_number")); } catch (SQLException ignored) {}
        try { m.setContractorName(rs.getString("contractor_name")); } catch (SQLException ignored) {}
        return m;
    }

}
