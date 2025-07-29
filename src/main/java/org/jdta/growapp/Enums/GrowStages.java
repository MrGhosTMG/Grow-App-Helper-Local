package org.jdta.growapp.Enums;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum GrowStages {
    START_PLANTING("Seed planted"),
    GERMINATED("Seed germinated"),
    VEGETATION("On vegetation"),
    PRE_FLOWERING("Starting flowering"),
    FLOWERING("Flowering progress" ),
    CLEANING("Flowering ended"),
    HARVEST("Cutting"),
    DRYING("Drying process"),
    YIELD("Dry weight");

    private final String showName;

    GrowStages(String showName) {
        this.showName = showName;
    }

    @Override
    public String toString() {
        return showName ;
    }

    public int calculateDaysSince(LocalDate stageStartDate) {
        return (int) ChronoUnit.DAYS.between(stageStartDate, LocalDate.now());
    }

    public static GrowStages getPreviousStage(GrowStages curr) {
        int index = curr.ordinal();
        GrowStages[] stages = values();
        return (index > 0 ) ? stages[index - 1] : curr;
    }

    public static GrowStages getNextStage(GrowStages curr) {
        int index = curr.ordinal();
        GrowStages[] stages = values();
        return (index + 1 < stages.length) ? stages[index + 1] : curr;
    }

    public static List<GrowStages> getAvailableStageAfter(GrowStages curr) {
        return Arrays.stream(values()).filter(s -> s.ordinal() >= curr.ordinal()).collect(Collectors.toList());
    }
}
