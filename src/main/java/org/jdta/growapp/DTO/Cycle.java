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

    private int yieldGrams;
    private boolean finished;

    // Расширенные поля стадий
    private int totalGrowDays;
    private int germinationDays;
    private int firstLeafDays;
    private int vegetationDays;
    private int preFloweringDays;
    private int floweringDays;
    private int dryingDays;

    // Getters & Setters

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

    public int getLightDayHours() {
        return lightDayHours;
    }
    public void setLightDayHours(int lightDayHours) {
        this.lightDayHours = lightDayHours;
    }

    public int getLightNightHours() {
        return lightNightHours;
    }
    public void setLightNightHours(int lightNightHours) {
        this.lightNightHours = lightNightHours;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSortType() {
        return sortType;
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

    public int getYieldGrams() {
        return yieldGrams;
    }
    public void setYieldGrams(int yieldGrams) {
        this.yieldGrams = yieldGrams;
    }

    public boolean isFinished() {
        return finished;
    }
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getTotalGrowDays() {
        return totalGrowDays;
    }
    public void setTotalGrowDays(int totalGrowDays) {
        this.totalGrowDays = totalGrowDays;
    }

    public int getGerminationDays() {
        return germinationDays;
    }
    public void setGerminationDays(int germinationDays) {
        this.germinationDays = germinationDays;
    }

    public int getFirstLeafDays() {
        return firstLeafDays;
    }
    public void setFirstLeafDays(int firstLeafDays) {
        this.firstLeafDays = firstLeafDays;
    }

    public int getVegetationDays() {
        return vegetationDays;
    }
    public void setVegetationDays(int vegetationDays) {
        this.vegetationDays = vegetationDays;
    }

    public int getPreFloweringDays() {
        return preFloweringDays;
    }
    public void setPreFloweringDays(int preFloweringDays) {
        this.preFloweringDays = preFloweringDays;
    }

    public int getFloweringDays() {
        return floweringDays;
    }
    public void setFloweringDays(int floweringDays) {
        this.floweringDays = floweringDays;
    }

    public int getDryingDays() {
        return dryingDays;
    }
    public void setDryingDays(int dryingDays) {
        this.dryingDays = dryingDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cycle cycle = (Cycle) o;
        return id == cycle.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

}
