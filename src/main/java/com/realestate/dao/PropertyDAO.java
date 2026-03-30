package realestate.dao;

import realestate.model.Property;
import realestate.util.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropertyDAO {

    public boolean insert(Property property) {
        String sql = "INSERT INTO properties (property_name, address, city, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, property.getPropertyName());
            ps.setString(2, property.getAddress());
            ps.setString(3, property.getCity());
            ps.setString(4, property.getDescription());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) property.setPropertyId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return false;
    }

    public boolean update(Property property) {
        String sql = "UPDATE properties SET property_name = ?, address = ?, city = ?, description = ? WHERE property_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, property.getPropertyName());
            ps.setString(2, property.getAddress());
            ps.setString(3, property.getCity());
            ps.setString(4, property.getDescription());
            ps.setInt(5, property.getPropertyId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return false;
    }

    public List<Property> findAll() {
        List<Property> list = new ArrayList<>();
        String sql = "SELECT * FROM properties ORDER BY created_at DESC";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return list;
    }

    public List<Property> search(String keyword) {
        List<Property> list = new ArrayList<>();
        String sql = "SELECT * FROM properties WHERE property_name LIKE ? OR address LIKE ? OR city LIKE ? ORDER BY created_at DESC";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return list;
    }

    public Property findById(int propertyId) {
        String sql = "SELECT * FROM properties WHERE property_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, propertyId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return null;
    }

    private Property mapRow(ResultSet rs) throws SQLException {
        Property p = new Property();
        p.setPropertyId(rs.getInt("property_id"));
        p.setPropertyName(rs.getString("property_name"));
        p.setAddress(rs.getString("address"));
        p.setCity(rs.getString("city"));
        p.setDescription(rs.getString("description"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        return p;
    }
}
