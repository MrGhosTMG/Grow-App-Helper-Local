package org.jdta.growapp.Utils;

import org.jdta.growapp.DTO.Cycle;
import org.jdta.growapp.Service.LightStageService;

import java.time.Duration;

public class LightStageUtils {

    public static boolean isCurrentlyDay(Cycle cycle) {
        return LightStageService.isLightOn(cycle.getLightSetTime(), cycle.getLightDayHours(), cycle.getLightNightHours());
    }

    public static Duration getRemainingPhaseTime(Cycle cycle) {
        return LightStageService.getNextPhaseChangeMinutes(cycle.getLightSetTime(), cycle.getLightDayHours(), cycle.getLightNightHours());
    }

    private static String getFormattedRemainingTime(Cycle cycle) {
        return LightStageService.formaterDuration(getRemainingPhaseTime(cycle));
    }

    public static String getLightStatusLabel(Cycle cycle) {
        boolean isDay = isCurrentlyDay(cycle);
        String emoji = isDay ? "🌞 DAY" : "🌙 NIGHT";
        String remaining = getFormattedRemainingTime(cycle);
        return emoji + " • time left: " + remaining;
    }

}
