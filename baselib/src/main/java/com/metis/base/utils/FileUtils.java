package com.metis.base.utils;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Beak on 2015/7/16.
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * @param from
     * @param to
     * @return
     * @throws IOException
     */
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

    /**
     * @param url
     * @return
     */
    public static String getNameFromUrl (String url) {
        final int index = url.lastIndexOf("/");
        if (index >= 0 && url.length() > 1) {
            return url.substring(index + 1, url.length());
        }
        return url;
    }

    /**
     * @param file the file to delete,may be a directory
     * @return the count of deleted files,not include directory count;
     */
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
            file.delete();
        }
        return count;
    }

    /**
     * @param file the file you want to know the length,may be directory, this may cost much time
     *             ,you may need to use this whit async ways.
     * @return the length of the file or directory
     */
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

    public static byte[] bitmapToByteArray (Bitmap.CompressFormat format, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(format, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap compressBitmap (Bitmap bmp, int maxSize) {
        final int width = bmp.getWidth();
        final int height = bmp.getHeight();
        //Log.v(TAG, "compressBitmap width=" + width + " height" + height);
        int size = Math.max(width, height);
        if (size > maxSize) {
            float scale = (float)maxSize / size;
            final int targetWidth = (int)(width * scale);
            final int targetHeight = (int)(height * scale);
            //Log.v(TAG, "compressBitmap targetWidth=" + targetWidth + " targetHeight" + targetHeight + " scale=" + scale);
            return ThumbnailUtils.extractThumbnail(bmp, targetWidth, targetHeight, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        } else {
            return bmp;
        }
    }
}
