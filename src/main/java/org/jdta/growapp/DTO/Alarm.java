package org.jdta.growapp.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Alarm {
    private int id;
    private int cycleId;
    private LocalDate alarmDate;
    private LocalDateTime createdAt;
    private String alarmType;
    private String note;

//    public Alarm(int id, int cycleId, String alarmType ,LocalDate alarmDate, String note, LocalDateTime createdAt) {
//        this.id = id;
//        this.cycleId = cycleId;
//        this.alarmType = alarmType;
//        this.alarmDate = alarmDate;
//        this.note = note;
//        this.createdAt = createdAt;
//    }

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

    public LocalDate getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(LocalDate alarmDate) {
        this.alarmDate = alarmDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);// TO DO
    }

    @Override
    public int hashCode() {
       return Integer.hashCode(id);
    }
}
