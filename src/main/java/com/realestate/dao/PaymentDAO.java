package realestate.dao;

import realestate.model.Payment;
import realestate.util.DBconnection;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class PaymentDAO {

    public boolean insert(Payment payment) {
        String sql = "INSERT INTO payments (lease_id, payment_date, payment_month, amount, payment_method, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, payment.getLeaseId());
            ps.setDate(2, payment.getPaymentDate());
            ps.setDate(3, payment.getPaymentMonth());
            ps.setBigDecimal(4, payment.getAmount());
            ps.setString(5, payment.getPaymentMethod());
            ps.setString(6, payment.getStatus());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) payment.setPaymentId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return false;
    }

    public List<Payment> findAll() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT p.*, r.full_name AS renter_name FROM payments p " +
                "JOIN leases l ON p.lease_id = l.lease_id " +
                "JOIN renters r ON l.renter_id = r.renter_id ORDER BY p.payment_date DESC";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return list;
    }

    public List<Payment> findByLease(int leaseId) {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT p.*, r.full_name AS renter_name FROM payments p " +
                "JOIN leases l ON p.lease_id = l.lease_id " +
                "JOIN renters r ON l.renter_id = r.renter_id " +
                "WHERE p.lease_id = ? ORDER BY p.payment_date DESC";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, leaseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return list;
    }

    private Payment mapRow(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setPaymentId(rs.getInt("payment_id"));
        p.setLeaseId(rs.getInt("lease_id"));
        p.setPaymentDate(rs.getDate("payment_date"));
        p.setPaymentMonth(rs.getDate("payment_month"));
        p.setAmount(rs.getBigDecimal("amount"));
        p.setPaymentMethod(rs.getString("payment_method"));
        p.setStatus(rs.getString("status"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        try { p.setRenterName(rs.getString("renter_name")); }
        catch (SQLException ignored) {}
        return p;
    }
}
