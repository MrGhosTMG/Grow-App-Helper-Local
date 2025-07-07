package org.jdta.growapp.Enums;

public enum GrowStages {
    START_PLANTING("Just planted"),
    GERMINATED("Seed germinated"),
    VEGETATION("On vegetation"),
    PRE_FLOWERING("Starting flowering"),
    FLOWERING("Flowering progress"),
    CLEANING("Flowering ended"),
    HARVEST("Cutting"),
    DRYING("Drying process"),
    YIELD("Dry weight");

    private final String showName;
    private int daysOfStage;


    GrowStages(String showName) {
        this.showName = showName;

    }

    @Override
    public String toString() {
        return showName ;
    }

    public int getDaysOfStage() {// если не к месту можно перенести
        return daysOfStage;
    }

    public void setDaysOfStage(int daysOfStage) {// если не к месту можно перенести
        this.daysOfStage = daysOfStage;
    }

    public void setWeightOfYIELD(int weight) {// если не к месту можно перенести
    }
}
