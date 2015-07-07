package com.metis.base.manager;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Beak on 2015/7/7.
 */
public class CacheDirManager extends AbsManager {

    private static final String VISITOR = "visitor";

    private static CacheDirManager sManager = null;

    public synchronized static CacheDirManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new CacheDirManager(context.getApplicationContext());
        }
        return sManager;
    }

    private File mCacheRoot = null;

    private CacheDirManager(Context context) {
        super(context);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mCacheRoot = context.getExternalCacheDir();
        } else {
            mCacheRoot = context.getCacheDir();
        }
    }

    public File getVideoCacheDir () {
        return getVideoCacheDir(VISITOR);
    }

    public File getVideoCacheDir (String userId) {
        return getCacheFolder(userId, "video");
    }

    public File getCacheFolder (String folderName) {
        return getCacheFolder(VISITOR, folderName);
    }

    public File getCacheFolder (String userId, String folderName) {
        File folder = new File (mCacheRoot, userId + File.separator + folderName);
        return folder;
    }

}
