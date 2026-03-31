package realestate.dao;

import realestate.model.Lease;
import realestate.util.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaseDAO {
    public boolean insert(Lease lease) {
        String sql = "INSERT INTO leases (renter_id, unit_id, start_date, end_date, deposit_amount, monthly_rent, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, lease.getRenterId());
            ps.setInt(2, lease.getUnitId());
            ps.setDate(3, lease.getStartDate());
            ps.setDate(4, lease.getEndDate());
            ps.setBigDecimal(5, lease.getDepositAmount());
            ps.setBigDecimal(6, lease.getMonthlyRent());
            ps.setString(7, lease.getStatus());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) lease.setLeaseId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return false;
    }

    public boolean update(Lease lease) {
        String sql = "UPDATE leases SET status = ? WHERE lease_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lease.getStatus());
            ps.setInt(2, lease.getLeaseId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return false;
    }

    public List<Lease> findAll() {
        List<Lease> list = new ArrayList<>();
        String sql = "SELECT l.*, r.full_name AS renter_name, u.unit_number FROM leases l " +
                "JOIN renters r ON l.renter_id = r.renter_id " +
                "JOIN units u ON l.unit_id = u.unit_id ORDER BY l.created_at DESC";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return list;
    }

    public Lease findActiveByUnit(int unitId) {
        String sql = "SELECT l.*, r.full_name AS renter_name, u.unit_number FROM leases l " +
                "JOIN renters r ON l.renter_id = r.renter_id " +
                "JOIN units u ON l.unit_id = u.unit_id " +
                "WHERE l.unit_id = ? AND l.status = 'ACTIVE'";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, unitId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return null;
    }

    public List<Lease> findActiveLeases() {
        List<Lease> list = new ArrayList<>();
        String sql = "SELECT l.*, r.full_name AS renter_name, u.unit_number FROM leases l " +
                "JOIN renters r ON l.renter_id = r.renter_id " +
                "JOIN units u ON l.unit_id = u.unit_id " +
                "WHERE l.status = 'ACTIVE' ORDER BY l.created_at DESC";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return list;
    }

    public Lease findById(int leaseId) {
        String sql = "SELECT l.*, r.full_name AS renter_name, u.unit_number FROM leases l " +
                "JOIN renters r ON l.renter_id = r.renter_id " +
                "JOIN units u ON l.unit_id = u.unit_id WHERE l.lease_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, leaseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return null;
    }

    private Lease mapRow(ResultSet rs) throws SQLException {
        Lease l = new Lease();
        l.setLeaseId(rs.getInt("lease_id"));
        l.setRenterId(rs.getInt("renter_id"));
        l.setUnitId(rs.getInt("unit_id"));
        l.setStartDate(rs.getDate("start_date"));
        l.setEndDate(rs.getDate("end_date"));
        l.setDepositAmount(rs.getBigDecimal("deposit_amount"));
        l.setMonthlyRent(rs.getBigDecimal("monthly_rent"));
        l.setStatus(rs.getString("status"));
        l.setCreatedAt(rs.getTimestamp("created_at"));
        try { l.setRenterName(rs.getString("renter_name")); } catch (SQLException ignored) {}
        try { l.setUnitNumber(rs.getString("unit_number")); } catch (SQLException ignored) {}
        return l;
    }
}
