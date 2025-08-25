package org.jdta.growapp.DTO;

import org.jdta.growapp.Enums.GrowStages;

import java.time.LocalDate;

public class StageTransition {

    private int id; // убрал из конструктора
    private int cycleId;
    private GrowStages growStages;
    private LocalDate startDate;
    private int durationsDays;

    public StageTransition(int cycleId, GrowStages growStages, LocalDate startDate, int durationsDays) {

        this.cycleId = cycleId;
        this.growStages = growStages;
        this.startDate = startDate;
        this.durationsDays = durationsDays;
    }

    public StageTransition() {
    }

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

    public GrowStages getGrowStages() {
        return growStages;
    }

    public void setGrowStages(GrowStages growStages) {
        this.growStages = growStages;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getDurationsDays() {
        return durationsDays;
    }

    public void setDurationsDays(int durationsDays) {
        this.durationsDays = durationsDays;
    }
}
