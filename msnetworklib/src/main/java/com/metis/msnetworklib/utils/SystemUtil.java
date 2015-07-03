package com.metis.msnetworklib.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wudi on 3/15/2015.
 */
public class SystemUtil
{
    public static final String CACHE_FOLDER = "caches";
    public static final String FILE_CACHE_DIR = "files";

    public static String readDataFromInputStream(BufferedInputStream is)
    {
        ByteArrayOutputStream content = new ByteArrayOutputStream();

        int readBytes = 0;
        byte[] sBuffer = new byte[512];
        try
        {
            while ((readBytes = is.read(sBuffer)) != -1)
            {
                content.write(sBuffer, 0, readBytes);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

        return new String(content.toByteArray());
    }

    public static boolean hasFroyo()
    {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed
        // behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            return true;
        }
        return false;
    }

    /**
     * Check if there is any connectivity to a mobile network
     * @param context
     * @return
     */
    public static boolean isConnectedMobile(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    public static boolean isWiFiConnected(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi != null && wifi.isConnected())
        {
            return true;
        }
        return false;
    }

    @TargetApi(9)
    public static boolean isConnectedFast(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null)
        {
            int type = info.getType();
            int subType = info.getSubtype();
            if (type == ConnectivityManager.TYPE_WIFI)
            {
                return true;
            }
            else if (type == ConnectivityManager.TYPE_MOBILE)
            {
                switch (subType)
                {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        return false; // ~ 100 kbps
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                        return false; // ~ 50-100 kbps
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        return false; // ~ 14-64 kbps
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        return false; // ~ 50-100 kbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        return true; // ~ 400-1000 kbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        return true; // ~ 600-1400 kbps
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        return true; // ~ 2-14 Mbps
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        return true; // ~ 700-1700 kbps
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                        return true; // ~ 1-23 Mbps
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        return true; // ~ 400-7000 kbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                        return true; // ~ 5 Mbps
                    case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                        return false; // ~25 kbps
                    // case TelephonyManager.NETWORK_TYPE_LTE: // API level
                    // 11
                    // return true; // ~ 10+ Mbps
                    // case TelephonyManager.NETWORK_TYPE_EHRPD: // API
                    // level 11
                    // return true; // ~ 1-2 Mbps
                    // case TelephonyManager.NETWORK_TYPE_HSPAP: // API
                    // level 13
                    // return true; // ~ 10-20 Mbps
                    // Unknown
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    default:
                        return false;
                }
            }
            else
            {
                return false;
            }

        }
        return false;
    }

    public static DisplayMetrics getDisplayMetrics(Context context)
    {
        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics m = new DisplayMetrics();
        d.getMetrics(m);

        return m;
    }

    public static int getDisplayWidth(Context context)
    {
        return getDisplayMetrics(context).widthPixels;
    }

    public static int getDisplayHeight(Context context)
    {
        return getDisplayMetrics(context).heightPixels;
    }

    public static String truncateString(String origin, int maxLength, String stringToInsertAtCutPoint)
    {
        String result;
        if (origin == null || origin.length() <= maxLength)
        {
            result = origin;
        }
        else
        {
            result = origin.substring(0, maxLength);
            if (!TextUtils.isEmpty(stringToInsertAtCutPoint))
            {
                result = result + stringToInsertAtCutPoint;
            }
        }

        return result;
    }

    /**
     * Get the external app cache directory.
     *
     * @param context
     *            The context to use
     * @return The external cache dir
     */
    @TargetApi(8)
    public static File getExternalCacheDir(Context context)
    {
        if (hasFroyo())
        {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * @param context
     *            The context to use
     * @param uniqueName
     *            A unique directory name to append to the cache dir
     * @return The cache dir
     */
    public static File getDiskCacheDir(Context context, String uniqueName)
    {
        // Check if media is mounted or storage is built-in, if so, try and use
        // external cache dir
        // otherwise use internal cache dir
        boolean shouldUseExternalCache = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !isExternalStorageRemovable();

        File getExternalCacheDir = shouldUseExternalCache ? getExternalCacheDir(context) : null;

        final String cachePath = getExternalCacheDir != null ? getExternalCacheDir.getPath() : context.getCacheDir().getPath();

        String dir = cachePath + File.separator + CACHE_FOLDER;
        File dirFile = new File(dir);
        if (!dirFile.exists())
        {
            dirFile.mkdirs();
        }
        return new File(dir + File.separator + uniqueName);
    }

    public static long getDiskCacheSize(Context context)
    {
        final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !isExternalStorageRemovable() ? getExternalCacheDir(
                context).getPath()
                : context.getCacheDir().getPath();
        String cacheDir = cachePath + File.separator + CACHE_FOLDER;
        File cacheFolder = new File(cacheDir);
        return getFileSize(cacheFolder);
    }

    private static long getFileSize(File file)
    {
        long size = 0;
        if (file.isFile())
        {
            return file.length();
        }

        File[] files = file.listFiles();
        for (File f : files)
        {
            if (file.isFile())
            {
                size += file.length();
            }
            else
            {
                size += getFileSize(f);
            }
        }
        return size;
    }

    /**
     * Check if external storage is built-in or removable.
     *
     * @return True if external storage is removable (like an SD card), false
     *         otherwise.
     */
    @TargetApi(9)
    public static boolean isExternalStorageRemovable()
    {
        if (hasGingerbread())
        {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    /**
     * Check how much usable space is available at a given path.
     *
     * @param path
     *            The path to check
     * @return The space available in bytes
     */
    @SuppressWarnings("deprecation")
    @TargetApi(9)
    public static long getUsableSpace(File path)
    {
        if (hasGingerbread())
        {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }

    /**
     * A hashing method that changes a string (like a URL) into a hash suitable
     * for using as a disk filename.
     */
    public static String hashKeyForDisk(String key)
    {
        String cacheKey;
        try
        {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes)
    {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++)
        {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1)
            {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static float convertDpToPixel(Context context, float dp)
    {
        DisplayMetrics dm = getDisplayMetrics(context);
        return dp * (dm.densityDpi / 160f);
    }

    public static float convertPixelsToDp(Context context, float px)
    {
        DisplayMetrics dm = getDisplayMetrics(context);
        return (float) ((double) px / (dm.densityDpi <= 0 ? 1 : ((double) dm.densityDpi / 160f)));
    }

    public static void makeDirs(String path)
    {
        if (TextUtils.isEmpty(path))
        {
            return;
        }

        int index = path.lastIndexOf("/");
        if (index == -1)
        {
            index = path.lastIndexOf("\\");
        }

        if (index != -1)
        {
            String folder = path.substring(0, index);
            File folderFile = new File(folder);
            if (!folderFile.exists())
            {
                folderFile.mkdirs();
            }
        }
    }

}
