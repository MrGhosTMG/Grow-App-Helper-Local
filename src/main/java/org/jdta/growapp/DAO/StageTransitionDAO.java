package org.jdta.growapp.DAO;

import org.jdta.growapp.DTO.Alarm;
import org.jdta.growapp.DTO.Cycle;
import org.jdta.growapp.DTO.StageTransition;
import org.jdta.growapp.Enums.GrowStages;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StageTransitionDAO {

    private Connection conn;

    public StageTransitionDAO(Connection conn) {
        this.conn = conn;
    }

    public int insert(StageTransition transition) throws SQLException {
        String sql = "INSERT INTO stage_transitions (cycle_id, stage, start_date, duration_days) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, transition.getCycleId());
            stmt.setString(2, transition.getGrowStages().name());
            stmt.setString(3, transition.getStartDate().toString());
            stmt.setInt(4, transition.getDurationsDays());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public List<StageTransition> findAllByCycleId(int cycleId) {
        List<StageTransition> list = new ArrayList<>();
        String sql = "SELECT * FROM stage_transitions WHERE cycle_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cycleId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                StageTransition stageTransition = new StageTransition();

                stageTransition.setId(rs.getInt("id"));
                stageTransition.setCycleId(rs.getInt("cycle_id"));
                stageTransition.setGrowStages(GrowStages.valueOf( rs.getString("stage")));
                stageTransition.setStartDate(LocalDate.parse(rs.getString("start_date")));
                stageTransition.setDurationsDays(rs.getInt("duration_days"));

                list.add(stageTransition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deleteByCycleId(int cycleId) {
        String sql = "DELETE FROM stage_transitions WHERE cycle_id = ?";
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cycleId);
            pstmt.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
