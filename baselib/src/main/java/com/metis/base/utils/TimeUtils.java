package com.metis.base.utils;

import android.content.Context;
import android.text.TextUtils;

import com.metis.base.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Beak on 2015/8/11.
 */
public class TimeUtils {

    public static final long MINUTE_LONG = 1000 * 60, HOUR_LONG = MINUTE_LONG * 60, DAY_LONG = 24 * HOUR_LONG;

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("mm:ss");

    public static final SimpleDateFormat STD_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat HOUR_MINUTE_FORMAT = new SimpleDateFormat("HH:mm");

    public static final SimpleDateFormat MONTH_DAY_FORMAT = new SimpleDateFormat("MM-dd");

    public static String format (long time) {
        return (time / 1000) + "''";
    }

    public static String formatStdTime (Context context, String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        String after = time.replace("T", " ").replace("Z", "");
        try {
            Date date = STD_DATE_FORMAT.parse(after);
            final long now = System.currentTimeMillis();
            long diff = now - date.getTime();
            if (diff <  HOUR_LONG) {
                return context.getString(R.string.time_minutes_age, diff / (1000 * 60));
            } else if (diff < DAY_LONG) {
                return HOUR_MINUTE_FORMAT.format(date);
            } else {
                return MONTH_DAY_FORMAT.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return after;
    }
}
