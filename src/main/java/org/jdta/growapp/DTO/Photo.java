package org.jdta.growapp.DTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Photo {

    private int id;
    private int cycleId;
    private String path;
    private LocalDateTime timestamp;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getCycleId() {
        return cycleId;
    }
    public void setCycleId(int cycleId) {
        this.cycleId = cycleId;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // Парсинг из строки (из БД)
    public void setTimestampFromString(String timestampStr) {
        this.timestamp = LocalDateTime.parse(timestampStr, FORMATTER);
    }

    // Получение строки (вставка в БД)
    public String getTimestampAsString() {
        return timestamp.format(FORMATTER);
    }
}
