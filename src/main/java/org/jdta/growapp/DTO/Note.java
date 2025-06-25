package org.jdta.growapp.DTO;

import java.time.LocalDate;

public class Note {

    private int id;
    private int cycleId;
    private String content;
    private LocalDate createdAt;

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

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    //парсить строку в LocalDate или LocalDateTime (можно через DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
