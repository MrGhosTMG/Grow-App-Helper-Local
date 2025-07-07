package org.jdta.growapp.DTO;

import java.time.LocalDateTime;

public class Cycle {

    private int id;
    private int userId;
    private String name;
    private double potCapacity;
    private LocalDateTime startDateTime;
    private LocalDateTime etaDate;
    private String imagePath;
    private int lightDayHours;
    private int lightNightHours;
    private String notes;
    private String sortType;
    private String indoorOutdoor;

    public LocalDateTime getEtaDate() {
        return etaDate;
    }

    public int getLightDayHours() {
        return lightDayHours;
    }

    public int getLightNightHours() {
        return lightNightHours;
    }

    public String getNotes() {
        return notes;
    }

    public String getSortType() {
        return sortType;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public double getPotCapacity() {
        return potCapacity;
    }
    public void setPotCapacity(double potCapacity) {
        this.potCapacity = potCapacity;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEtaDateTime() {
        return etaDate;
    }
    public void setEtaDateTime(LocalDateTime etaDate) {
        this.etaDate = etaDate;
    }

    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setLightDayHours(int lightDayHours) {
        this.lightDayHours = lightDayHours;
    }

    public void setLightNightHours(int lightNightHours) {
        this.lightNightHours = lightNightHours;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getIndoorOutdoor() {
        return indoorOutdoor;
    }

    public void setIndoorOutdoor(String indoorOutdoor) {
        this.indoorOutdoor = indoorOutdoor;
    }
}
