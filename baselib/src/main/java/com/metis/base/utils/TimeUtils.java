package com.metis.base.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Beak on 2015/8/11.
 */
public class TimeUtils {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("mm:ss");

    public static String format (long time) {
        return DATE_FORMAT.format(new Date(time));
    }
}
