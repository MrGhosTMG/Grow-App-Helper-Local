package org.jdta.growapp.DTO;

import java.time.LocalDate;

public class Cycle {

    private int id;
    private int userId;
    private String name;
    private boolean isIndoor;
    private double potCapacity;
    private LocalDate startDate;
    private LocalDate etaDate;
    private String imagePath;

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

    public boolean isIndoor() {
        return isIndoor;
    }
    public void setIndoor(boolean indoor) {
        isIndoor = indoor;
    }

    public double getPotCapacity() {
        return potCapacity;
    }
    public void setPotCapacity(double potCapacity) {
        this.potCapacity = potCapacity;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEtaDate() {
        return etaDate;
    }
    public void setEtaDate(LocalDate etaDate) {
        this.etaDate = etaDate;
    }

    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
