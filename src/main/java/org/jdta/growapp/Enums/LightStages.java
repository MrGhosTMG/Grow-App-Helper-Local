package org.jdta.growapp.Enums;

public enum LightStages {
    LIGHT_24_0(24, 0),
    LIGHT_20_4(20, 4),
    LIGHT_18_6(18, 6),
    LIGHT_12_12(12, 12),
    MANUAL(-1, -1);

    private final int dayHours;
    private final int nightHours;

    LightStages(int day, int night) {
        this.dayHours = day;
        this.nightHours = night;
    }

    public int getDayHours() {
        return dayHours;
    }

    public int getNightHours() {
        return nightHours;
    }

    @Override
    public String toString() {
        return dayHours + " / " + nightHours;
    }
}
