package org.jdta.growapp.Enums;

public enum GrowStages {
    START_PLANTING("Just planted", 2),
    GERMINATED("Seed germinated",4),
    VEGETATION("On vegetation",30),
    PRE_FLOWERING("Starting flowering",5),
    FLOWERING("Flowering progress" , 40),
    CLEANING("Flowering ended", 2),
    HARVEST("Cutting", 1),
    DRYING("Drying process", 7),
    YIELD("Dry weight", 0);

    private final String showName;
    private int daysOfStage;
    private int defaultDays;


    GrowStages(String showName, int defaultDays) {
        this.showName = showName;
        this.defaultDays = defaultDays;
        this.daysOfStage = defaultDays;
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

    public int getDefaultDays() {
        return defaultDays;
    }
}
