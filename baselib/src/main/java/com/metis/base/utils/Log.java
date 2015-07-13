package com.metis.base.utils;

import com.metis.base.Debug;

/**
 * Created by Beak on 2015/7/13.
 */
public class Log {
    public static void i (String tag, String msg) {
        if (Debug.DEBUG) {
            android.util.Log.i(tag, msg);
        }
    }

    public static void i (String tag, String msg, Throwable throwable) {
        if (Debug.DEBUG) {
            android.util.Log.i(tag, msg, throwable);
        }
    }

    public static void v (String tag, String msg) {
        if (Debug.DEBUG) {
            android.util.Log.v(tag, msg);
        }
    }

    public static void v (String tag, String msg, Throwable throwable) {
        if (Debug.DEBUG) {
            android.util.Log.v(tag, msg, throwable);
        }
    }

    public static void d (String tag, String msg) {
        if (Debug.DEBUG) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void d (String tag, String msg, Throwable throwable) {
        if (Debug.DEBUG) {
            android.util.Log.d(tag, msg, throwable);
        }
    }

    public static void w (String tag, String msg) {
        if (Debug.DEBUG) {
            android.util.Log.w(tag, msg);
        }
    }

    public static void w (String tag, String msg, Throwable throwable) {
        if (Debug.DEBUG) {
            android.util.Log.w(tag, msg, throwable);
        }
    }

    public static void e (String tag, String msg) {
        if (Debug.DEBUG) {
            android.util.Log.e(tag, msg);
        }
    }

    public static void e (String tag, String msg, Throwable throwable) {
        if (Debug.DEBUG) {
            android.util.Log.e(tag, msg, throwable);
        }
    }
}
