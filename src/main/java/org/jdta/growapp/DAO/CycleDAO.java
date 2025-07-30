package org.jdta.growapp.DAO;

import org.jdta.growapp.DTO.Cycle;
import org.jdta.growapp.Enums.GrowStages;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CycleDAO {

    private final Connection conn;

    public CycleDAO(Connection conn) {
        this.conn = conn;
    }


    public int insert(Cycle cycle) throws SQLException {
        String sql = "INSERT INTO cycles (user_id, cycle_name, indoor_outdoor, sort_type, start_date, " +
                "estimated_end_date, pot_capacity, notes, light_day_hours, light_night_hours, image_path, light_set_time, grow_stage) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, cycle.getUserId());
            stmt.setString(2, cycle.getName());
            stmt.setString(3, cycle.getIndoorOutdoor());
            stmt.setString(4, cycle.getSortType());
            stmt.setString(5, cycle.getStartDateTime().toString());
            stmt.setString(6, cycle.getEtaDateTime().toString());
            stmt.setDouble(7, cycle.getPotCapacity());
            stmt.setString(8, cycle.getNotes());
            stmt.setInt(9, cycle.getLightDayHours());
            stmt.setInt(10, cycle.getLightNightHours());
            stmt.setString(11, cycle.getImagePath());
            stmt.setString(12, cycle.getLightSetTime() != null ? cycle.getLightSetTime().toString() : null);
            stmt.setString(13, cycle.getGrowStage().name());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public boolean update(Cycle cycle) throws SQLException {
        String sql = "UPDATE cycles SET " +
                "user_id = ?, cycle_name = ?, indoor_outdoor = ?, sort_type = ?, " +
                "start_date = ?, estimated_end_date = ?, pot_capacity = ?, notes = ?, " +
                "light_day_hours = ?, light_night_hours = ?, image_path = ?, light_set_time = ?, grow_stage = ? " +
                "WHERE id = ?";


        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cycle.getUserId());
            stmt.setString(2, cycle.getName());
            stmt.setString(3, cycle.getIndoorOutdoor());
            stmt.setString(4, cycle.getSortType());
            stmt.setString(5, cycle.getStartDateTime().toString());
            stmt.setString(6, cycle.getEtaDateTime().toString());
            stmt.setDouble(7, cycle.getPotCapacity());
            stmt.setString(8, cycle.getNotes());
            stmt.setInt(9, cycle.getLightDayHours());
            stmt.setInt(10, cycle.getLightNightHours());
            stmt.setString(11, cycle.getImagePath());
            stmt.setString(12, cycle.getLightSetTime() != null ? cycle.getLightSetTime().toString() : null);
            stmt.setString(13, cycle.getGrowStage().name());
            stmt.setInt(14, cycle.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public Cycle findById(int id) throws SQLException {
        String sql = "SELECT * FROM cycles WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return extractCycleFromResultSet(rs);
            }
        }
        return null;
    }

    public List<Cycle> findAll() throws SQLException {
        List<Cycle> cycles = new ArrayList<>();
        String sql = "SELECT * FROM cycles";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cycles.add(extractCycleFromResultSet(rs));
            }
        }
        return cycles;
    }


    public boolean deleteById(int id) throws SQLException {
        String sql = "DELETE FROM cycles WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }


    public List<Cycle> findAllByUserId(int userId) throws SQLException {
        List<Cycle> cycles = new ArrayList<>();
        String sql = "SELECT * FROM cycles WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cycles.add(extractCycleFromResultSet(rs));
            }
        }
        return cycles;
    }

    private Cycle extractCycleFromResultSet(ResultSet rs) throws SQLException {
        Cycle cycle = new Cycle();
        cycle.setId(rs.getInt("id"));
        cycle.setUserId(rs.getInt("user_id"));
        cycle.setName(rs.getString("cycle_name"));
        cycle.setIndoorOutdoor(rs.getString("indoor_outdoor"));
        cycle.setSortType(rs.getString("sort_type"));
        cycle.setStartDateTime(LocalDateTime.parse(rs.getString("start_date")));
        cycle.setEtaDateTime(LocalDateTime.parse(rs.getString("estimated_end_date")));
        cycle.setPotCapacity(rs.getDouble("pot_capacity"));
        cycle.setNotes(rs.getString("notes"));
        cycle.setLightDayHours(rs.getInt("light_day_hours"));
        cycle.setLightNightHours(rs.getInt("light_night_hours"));
        cycle.setImagePath(rs.getString("image_path"));

        String lightSet = rs.getString("light_set_time");
        if (lightSet != null) {
            cycle.setLightSetTime(LocalDateTime.parse(lightSet));
        }else {
            System.err.println("Warning light_set_time is null for cycle id = " + cycle.getId());
        }

        String growSet = rs.getString("grow_stage");
        if (growSet != null) {
            cycle.setGrowStage(GrowStages.valueOf(growSet));
        }else {
            System.err.println("Warning grow_stage is null for cycle id = " + cycle.getId());
        }
        return cycle;
    }
}
