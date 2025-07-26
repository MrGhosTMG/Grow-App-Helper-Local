package org.jdta.growapp.DAO;

import org.jdta.growapp.DTO.Alarm;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlarmDAO {
    private final Connection connection;

    public AlarmDAO(Connection con) {
        this.connection = con;
        //createTableIfNotExist();
    }

//    private void createTableIfNotExist() {
//        String sql = """
//            CREATE TABLE IF NOT EXISTS Alarm (
//                id INTEGER PRIMARY KEY AUTOINCREMENT,
//                cycle_id INTEGER NOT NULL,
//                alarm_type TEXT NOT NULL,
//                alarm_date TEXT NOT NULL,
//                note TEXT,
//                created_at TEXT DEFAULT CURRENT_TIMESTAMP,
//                FOREIGN KEY (cycle_id) REFERENCES Cycle(id) ON DELETE CASCADE
//            );
//        """;
//        try (var stmt = connection.createStatement()) {
//            stmt.execute(sql);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void insert(Alarm alarm) {
        String sql = """
        INSERT INTO Alarm (cycle_id, alarm_type, alarm_date, note)
        VALUES (?, ?, ?, ?)
    """;
        try (var pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, alarm.getCycleId());
            pstmt.setString(2, alarm.getAlarmType());
            pstmt.setString(3, alarm.getAlarmDate().toString());
            pstmt.setString(4, alarm.getNote());
            pstmt.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM Alarm WHERE id = ?";
        try (var pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Alarm> findAllByCycleId(int cycleId) {
        List<Alarm> list = new ArrayList<>();
        String sql = "SELECT * FROM Alarm WHERE cycle_id = ? ORDER BY alarm_date ASC";
        try (var pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cycleId);
            var rs = pstmt.executeQuery();
            while (rs.next()) {
                Alarm alarm = new Alarm();
                alarm.setId(rs.getInt("id"));
                alarm.setCycleId(rs.getInt("cycle_id"));
                alarm.setAlarmType(rs.getString("alarm_type"));
                alarm.setAlarmDate(LocalDate.parse(rs.getString("alarm_date")));
                alarm.setNote(rs.getString("note"));
                alarm.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));
                list.add(alarm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
