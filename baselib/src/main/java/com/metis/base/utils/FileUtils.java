package com.metis.base.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Beak on 2015/7/16.
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    public static boolean copyFileTo (File from, File to) throws IOException {
        //Log.v(TAG, "copyFileTo from=" + from.getAbsolutePath() + " from.exist=" + from.exists() + " to=" + to.getAbsolutePath() + " to.exist=" + to.exists());
        FileInputStream fileInputStream = new FileInputStream(from);
        byte[] buffer = new byte[1024 * 4];
        FileOutputStream fileOutputStream = new FileOutputStream(to);
        while (fileInputStream.read(buffer) != -1) {
            fileOutputStream.write(buffer);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        fileInputStream.close();
        return to.exists();
    }

    public static String getNameFromUrl (String url) {
        final int index = url.lastIndexOf("/");
        if (index >= 0 && url.length() > 1) {
            return url.substring(index + 1, url.length());
        }
        return url;
    }
}
