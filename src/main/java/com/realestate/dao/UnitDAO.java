package realestate.dao;

import realestate.util.DBconnection;
import realestate.model.Unit;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UnitDAO {

    public boolean insert(Unit unit) {
        String sql = "INSERT INTO units (property_id, unit_number, rent_amount, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, unit.getPropertyId());
            ps.setString(2, unit.getUnitNumber());
            ps.setBigDecimal(3, unit.getRentAmount());
            ps.setString(4, unit.getStatus());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) unit.setUnitId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return false;
    }

    public boolean update(Unit unit) {
        String sql = "UPDATE units SET unit_number = ?, rent_amount = ?, status = ? WHERE unit_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, unit.getUnitNumber());
            ps.setBigDecimal(2, unit.getRentAmount());
            ps.setString(3, unit.getStatus());
            ps.setInt(4, unit.getUnitId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return false;
    }

    public boolean updateStatus(int unitId, String status) {
        String sql = "UPDATE units SET status = ? WHERE unit_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, unitId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return false;
    }

    public List<Unit> findByProperty(int propertyId) {
        List<Unit> list = new ArrayList<>();
        String sql = "SELECT u.*, p.property_name FROM units u " +
                "JOIN properties p ON u.property_id = p.property_id WHERE u.property_id = ? ORDER BY u.unit_number";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, propertyId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Unit> findAvailable() {
        List<Unit> list = new ArrayList<>();
        String sql = "SELECT u.*, p.property_name FROM units u " +
                "JOIN properties p ON u.property_id = p.property_id WHERE u.status = 'AVAILABLE' ORDER BY p.property_name, u.unit_number";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return list;
    }

    public List<Unit> findAll() {
        List<Unit> list = new ArrayList<>();
        String sql = "SELECT u.*, p.property_name FROM units u " +
                "JOIN properties p ON u.property_id = p.property_id ORDER BY p.property_name, u.unit_number";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return list;
    }

    public boolean existsByPropertyAndNumber(int propertyId, String unitNumber) {
        String sql = "SELECT 1 FROM units WHERE property_id = ? AND unit_number = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, propertyId);
            ps.setString(2, unitNumber);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return false;
    }

    public Unit findById(int unitId) {
        String sql = "SELECT u.*, p.property_name FROM units u " +
                "JOIN properties p ON u.property_id = p.property_id WHERE u.unit_id = ?";
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

    private Unit mapRow(ResultSet rs) throws SQLException {
        Unit u = new Unit();
        u.setUnitId(rs.getInt("unit_id"));
        u.setPropertyId(rs.getInt("property_id"));
        u.setUnitNumber(rs.getString("unit_number"));
        u.setRentAmount(rs.getBigDecimal("rent_amount"));
        u.setStatus(rs.getString("status"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        try { u.setPropertyName(rs.getString("property_name")); } catch (SQLException ignored) {}
        return u;
    }
}
