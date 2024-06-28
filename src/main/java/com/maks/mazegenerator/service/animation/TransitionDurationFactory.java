package com.maks.mazegenerator.service.animation;

import javafx.util.Duration;

public final class TransitionDurationFactory {

    public static Duration createTime(int pathSize) {
        System.out.println(pathSize);
        Duration duration = null;
        if (pathSize < 30) {
            duration = Duration.seconds(10);
        } else if (pathSize < 60) {
            duration = Duration.seconds(20);
        } else if (pathSize < 100) {
            duration = Duration.seconds(30);
        } else {
            duration = Duration.seconds(50);
        }
        return duration;
    }
}
