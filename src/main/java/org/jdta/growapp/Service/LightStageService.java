package org.jdta.growapp.Service;

import java.time.Duration;
import java.time.LocalDateTime;

public class LightStageService {

    // Current phase check
    public static boolean isLightOn(LocalDateTime setTime, int dayHours, int nightHours) {

        if (setTime == null) return true;

        Duration sinceSet = Duration.between(setTime, LocalDateTime.now());
        long total = sinceSet.toMinutes();
        long minutesCycleLength = (dayHours + nightHours) * 60L;
        long mod = total % minutesCycleLength;

        return mod < (dayHours * 60L);
    }

    // Phase switch time left
    public static Duration getNextPhaseChangeMinutes(LocalDateTime setTime, int dayHours, int nightHours) {
        if (setTime == null) return Duration.ZERO;
        Duration sinceSet = Duration.between(setTime, LocalDateTime.now());
        long total = sinceSet.toMinutes();
        long minutesCycleLength = (dayHours + nightHours) * 60L;
        long mod = total % minutesCycleLength;

        long rem;
        if (mod < dayHours * 60L) {
            rem = (dayHours * 60L) - mod;
        }else {
            rem = minutesCycleLength - mod;
        }
        return Duration.ofMinutes(rem);
    }

    //Formater
    public static String formaterDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return String.format("%02dh %02dm", hours, minutes);
    }
}
