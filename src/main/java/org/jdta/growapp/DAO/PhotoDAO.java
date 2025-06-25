package org.jdta.growapp.DAO;

import org.jdta.growapp.DTO.Photo;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PhotoDAO {

    private final Connection connection;

    public PhotoDAO(Connection connection) {
        this.connection = connection;
    }

    // Insert new photo
    public void insertNewPhoto(Photo photo) throws SQLException {
        String sql = "INSERT INTO photos (cycle_id, path, timestamp) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, photo.getCycleId());
            statement.setString(2, photo.getPath());
            statement.setString(3, photo.getTimestampAsString());
            statement.executeUpdate();
        }
    }

    // Delete photo by ID
    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM photos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Find photo by ID
    public Photo findById(int id) throws SQLException {
        String sql = "SELECT * FROM photos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Photo photo = new Photo();
                photo.setId(rs.getInt("id"));
                photo.setCycleId(rs.getInt("cycle_id"));
                photo.setPath(rs.getString("path"));
                photo.setTimestampFromString(rs.getString("timestamp"));
                return photo;
            }
        }

        return null;
    }

    // Get all photos by cycle_id
    public List<Photo> findAllByCycleId(int cycleId) throws SQLException {
        List<Photo> photos = new ArrayList<>();
        String sql = "SELECT * FROM photos WHERE cycle_id = ? ORDER BY timestamp DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cycleId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Photo photo = new Photo();
                photo.setId(rs.getInt("id"));
                photo.setCycleId(rs.getInt("cycle_id"));
                photo.setPath(rs.getString("path"));
                photo.setTimestampFromString(rs.getString("timestamp"));
                photos.add(photo);
            }
        }

        return photos;
    }
}
