package com.metis.base.manager;

import android.content.Context;
import android.os.Environment;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.metis.base.module.User;

import java.io.File;
import java.util.List;

/**
 * Created by Beak on 2015/7/7.
 */
public class CacheManager extends AbsManager {

    private static final String VISITOR = "visitor";

    private static CacheManager sManager = null;

    public synchronized static CacheManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new CacheManager(context.getApplicationContext());
        }
        return sManager;
    }

    private File mCacheRoot = null;

    private CacheManager(Context context) {
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

    private File getVideoCacheDir (String userId) {
        return getCacheFolder(userId, "video");
    }

    public File getCacheFolder (String folderName) {
        return getCacheFolder(VISITOR, folderName);
    }

    public File getCacheFolder (String userId, String folderName) {
        File folder = new File (mCacheRoot, userId + File.separator + folderName);
        return folder;
    }

    public File getMyCacheFolder (String folderName) {
        User me = AccountManager.getInstance(getContext()).getMe();
        if (me == null) {
            return getCacheFolder(VISITOR, folderName);
        }
        return getCacheFolder(me.userId + "", folderName);
    }

    public File getMyVideoCacheDir () {
        return getMyCacheFolder("video");
    }

    public File getMyVoiceCacheDir () {
        return getMyCacheFolder("voice");
    }

    public File getMyDataCacheDir () {
        return getMyCacheFolder("data");
    }

    public File getMyImageCacheDir () {
        return getMyCacheFolder("image");
    }

    public boolean saveUserDataAtDatabase (Object object, String dbName) {
        File myDataCache = getMyDataCacheDir();
        if (myDataCache == null) {
            return false;
        }
        DbUtils dbUtils = DbUtils.create(getContext(), myDataCache.getAbsolutePath(), dbName);
        try {
            dbUtils.save(object);
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        } finally {
            dbUtils.close();
        }
        return true;
    }

    public boolean saveAllUserDataAtDatabase (List<?> objects, String dbName, Class<?> clz, boolean clearBefore) {
        File myDataCache = getMyDataCacheDir();
        if (myDataCache == null) {
            return false;
        }
        DbUtils dbUtils = DbUtils.create(getContext(), myDataCache.getAbsolutePath(), dbName);
        try {
            if (clearBefore) {
                //List list = readUserDataAtDatabase(clz, dbName);
                dbUtils.deleteAll(clz);
            }
            dbUtils.saveAll(objects);

        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public <T> List<T> readUserDataAtDatabase (Class<T> clz, String dbName) {
        File myDataCache = getMyDataCacheDir();
        if (myDataCache == null) {
            return null;
        }
        DbUtils dbUtils = DbUtils.create(getContext(), myDataCache.getAbsolutePath(), dbName);
        try {
            return dbUtils.findAll(clz);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }
}
