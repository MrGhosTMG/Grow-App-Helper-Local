package org.jdta.growapp.Service;

import java.time.Duration;
import java.time.LocalDateTime;

public class LightStageService {

    boolean isLightOn(LocalDateTime setTime, int dayHours, int nightHours) {
        Duration sinceSet = Duration.between(setTime, LocalDateTime.now());
        long total = sinceSet.toHours();
        long hourLength = dayHours + nightHours;
        long mod = total % hourLength;
        return mod < dayHours;
    }
}
