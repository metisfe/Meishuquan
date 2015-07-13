package com.metis.playerlib;

import java.text.DecimalFormat;

/**
 * Created by Beak on 2015/7/13.
 */
public class TimerHelper {
    private static final DecimalFormat FORMATER = new DecimalFormat("00");
    public static String formatTime (long time) {
        int totalSenconds = (int)time/*(time / 1000)*/;
        int minutes = totalSenconds / 60;
        int seconds = totalSenconds % 60;
        return FORMATER.format(minutes) + ":" + FORMATER.format(seconds);
    }
}
