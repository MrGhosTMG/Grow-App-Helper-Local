package org.jdta.growapp.DAO;

import org.jdta.growapp.DTO.Cycle;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CycleDAO {
    private final Connection conn;

    public CycleDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(Cycle cycle) throws SQLException {
        String sql = "INSERT INTO cycles (user_id, name, is_indoor, pot_capacity, start_date, eta_date, image_path) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cycle.getUserId());
            stmt.setString(2, cycle.getName());
            stmt.setBoolean(3, cycle.isIndoor());
            stmt.setDouble(4, cycle.getPotCapacity());
            stmt.setString(5, cycle.getStartDateTime().toString());
            stmt.setString(6, cycle.getEtaDateTime().toString());
            stmt.setString(7, cycle.getImagePath());
            stmt.executeUpdate();
        }
    }

    public List<Cycle> findAll() throws SQLException {
        List<Cycle> cycles = new ArrayList<>();
        String sql = "SELECT * FROM cycles";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cycle cycle = new Cycle();
                cycle.setId(rs.getInt("id"));
                cycle.setUserId(rs.getInt("user_id"));
                cycle.setName(rs.getString("name"));
                cycle.setIndoor(rs.getInt("is_indoor") == 1);
                cycle.setPotCapacity(rs.getDouble("pot_capacity"));
                cycle.setStartDateTime(LocalDateTime.parse(rs.getString("start_date")));
                cycle.setEtaDateTime(LocalDateTime.parse(rs.getString("eta_date")));
                cycle.setImagePath(rs.getString("image_path"));
                cycles.add(cycle);
            }
        }

        return cycles;
    }

    public Cycle findById(int id) throws SQLException {
        String sql = "SELECT * FROM cycles WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cycle cycle = new Cycle();
                    cycle.setId(rs.getInt("id"));
                    cycle.setUserId(rs.getInt("user_id"));
                    cycle.setName(rs.getString("name"));
                    cycle.setIndoor(rs.getInt("is_indoor") == 1);
                    cycle.setPotCapacity(rs.getDouble("pot_capacity"));
                    cycle.setStartDateTime(LocalDateTime.parse(rs.getString("start_date")));
                    cycle.setEtaDateTime(LocalDateTime.parse(rs.getString("eta_date")));
                    cycle.setImagePath(rs.getString("image_path"));
                    return cycle;
                }
            }
        }
        return null;
    }

    public boolean deleteById(int id) throws SQLException {
        String sql = "DELETE FROM cycles WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean update(Cycle cycle) throws SQLException {
        String sql = "UPDATE cycles SET user_id = ?, name = ?, is_indoor = ?, pot_capacity = ?, start_date = ?, eta_date = ?, image_path = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cycle.getUserId());
            stmt.setString(2, cycle.getName());
            stmt.setBoolean(3, cycle.isIndoor());
            stmt.setDouble(4, cycle.getPotCapacity());
            stmt.setString(5, cycle.getStartDateTime().toString());
            stmt.setString(6, cycle.getEtaDateTime().toString());
            stmt.setString(7, cycle.getImagePath());
            stmt.setInt(8, cycle.getId());
            return stmt.executeUpdate() > 0;
        }
    }
    /*
отображения всех циклов конкретного пользователя после авторизации;

фильтрации на экране Dashboard;

привязки к пользовательским заметкам, фото и т.д.
*/
    public List<Cycle> findAllByUserId(int userId) throws SQLException {
        List<Cycle> cycles = new ArrayList<>();
        String sql = "SELECT * FROM cycles WHERE user_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cycle cycle = new Cycle();
                cycle.setId(rs.getInt("id"));
                cycle.setUserId(rs.getInt("user_id"));
                cycle.setName(rs.getString("name"));
                cycle.setIndoor(rs.getInt("is_indoor") == 1);
                cycle.setPotCapacity(rs.getDouble("pot_capacity"));
                cycle.setStartDateTime(LocalDateTime.parse(rs.getString("start_date")));
                cycle.setEtaDateTime(LocalDateTime.parse(rs.getString("eta_date")));
                cycle.setImagePath(rs.getString("image_path"));
                cycles.add(cycle);
            }
        }

        return cycles;
    }

}
