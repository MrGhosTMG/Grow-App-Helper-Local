package org.jdta.growapp.DAO;

import org.jdta.growapp.DTO.Note;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NoteDAO {
    private final Connection conn;

    public NoteDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(Note note) throws SQLException {
        String sql = "INSERT INTO notes (cycle_id, content, created_at) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, note.getCycleId());
            stmt.setString(2, note.getContent());
            stmt.setString(3, note.getCreatedAt().toString()); // Предполагается формат ISO или CURRENT_TIMESTAMP
            stmt.executeUpdate();
        }
    }

    public void update(Note note) throws SQLException {
        String sql = "UPDATE notes SET content = ?, created_at = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, note.getContent());
            stmt.setString(2, note.getCreatedAt().toString());
            stmt.setInt(3, note.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM notes WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Note findById(int id) throws SQLException {
        String sql = "SELECT * FROM notes WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Note note = new Note();
                note.setId(rs.getInt("id"));
                note.setCycleId(rs.getInt("cycle_id"));
                note.setContent(rs.getString("content"));
                note.setCreatedAt(LocalDate.parse(rs.getString("created_at"))); // String → LocalDate
                return note;
            }
        }

        return null;
    }

    public List<Note> findAllByCycleId(int cycleId) throws SQLException {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT * FROM notes WHERE cycle_id = ? ORDER BY created_at DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cycleId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Note note = new Note();

                note.setId(rs.getInt("id"));
                note.setCycleId(rs.getInt("cycle_id"));
                note.setContent(rs.getString("content"));
                note.setCreatedAt(LocalDate.parse(rs.getString("created_at")));
                notes.add(note);
            }
        }

        return notes;
    }
}
