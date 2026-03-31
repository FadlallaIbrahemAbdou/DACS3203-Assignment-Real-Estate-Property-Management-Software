package realestate.dao;

import realestate.model.Contractor;
import realestate.util.DBconnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContractorDAO {

    public boolean insert(Contractor contractor) {
        String sql = "INSERT INTO contractors (company_name, contact_person, phone, service_type) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, contractor.getCompanyName());
            ps.setString(2, contractor.getContactPerson());
            ps.setString(3, contractor.getPhone());
            ps.setString(4, contractor.getServiceType());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) contractor.setContractorId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return false;
    }

    public boolean update(Contractor contractor) {
        String sql = "UPDATE contractors SET company_name = ?, contact_person = ?, phone = ?, service_type = ? WHERE contractor_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, contractor.getCompanyName());
            ps.setString(2, contractor.getContactPerson());
            ps.setString(3, contractor.getPhone());
            ps.setString(4, contractor.getServiceType());
            ps.setInt(5, contractor.getContractorId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return false;
    }

    public List<Contractor> findAll() {
        List<Contractor> list = new ArrayList<>();
        String sql = "SELECT * FROM contractors ORDER BY created_at DESC";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return list;
    }

    public Contractor findById(int contractorId) {
        String sql = "SELECT * FROM contractors WHERE contractor_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, contractorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.out.println("Database Error: "+ e.getMessage());
        }
        return null;
    }

    private Contractor mapRow(ResultSet rs) throws SQLException {
        Contractor c = new Contractor();
        c.setContractorId(rs.getInt("contractor_id"));
        c.setCompanyName(rs.getString("company_name"));
        c.setContactPerson(rs.getString("contact_person"));
        c.setPhone(rs.getString("phone"));
        c.setServiceType(rs.getString("service_type"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    }

}
