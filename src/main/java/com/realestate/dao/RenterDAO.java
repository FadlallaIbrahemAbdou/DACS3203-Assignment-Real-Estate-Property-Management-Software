package realestate.dao;

import realestate.model.Renter;
import realestate.util.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RenterDAO {

    public boolean insert(Renter renter){
        String sql= "INSERT INTO renters (full_name, phone, email) VALUES (?, ?, ?)";
        try (Connection conn = DBconnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, renter.getFullName());
            ps.setString(2, renter.getPhone());
            ps.setString(3, renter.getEmail());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) renter.setRenterId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e){
                System.out.println("Database Error: " + e.getMessage());
        }
        return false;
    }

    public boolean update(Renter renter) {
        String sql = "UPDATE renters SET full_name = ?, phone = ?, email = ? WHERE renter_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, renter.getFullName());
            ps.setString(2, renter.getPhone());
            ps.setString(3, renter.getEmail());
            ps.setInt(4, renter.getRenterId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
        return false;
    }

    public List<Renter> findAll() {
        List<Renter> list = new ArrayList<>();
        String sql = "SELECT * FROM renters ORDER BY created_at DESC";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
        return list;
    }

    public Renter findById(int renterId) {
        String sql = "SELECT * FROM renters WHERE renter_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, renterId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
        return null;
    }

    private Renter mapRow(ResultSet rs) throws SQLException {
        Renter r = new Renter();
        r.setRenterId(rs.getInt("renter_id"));
        r.setFullName(rs.getString("full_name"));
        r.setPhone(rs.getString("phone"));
        r.setEmail(rs.getString("email"));
        r.setCreatedAt(rs.getTimestamp("created_at"));
        return r;
    }
}
