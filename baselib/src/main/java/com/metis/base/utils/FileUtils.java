package com.metis.base.utils;

import java.io.File;
import java.io.FileInputStream;
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

    public static int clearFile(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        int count = 0;
        if (file.isFile()) {
            boolean isDeleted = file.delete();
            if (isDeleted) {
                count++;
            }
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            final int length = files.length;
            for (int i = 0; i < length; i++) {
                count += clearFile(files[i]);
            }
        }
        return count;
    }

    public static long getDirectorySpace(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        long length = 0;
        if (file.isFile()) {
            length += file.length();
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            final int size = files.length;
            for (int i = 0; i < size; i++) {
                length += getDirectorySpace(files[i]);
            }
        }
        return length;
    }
}
