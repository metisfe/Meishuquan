package com.metis.base.manager;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

/**
 * Created by Beak on 2015/7/10.
 */
public class DisplayManager extends AbsManager {

    private static DisplayManager sManager = null;
    public synchronized static DisplayManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new DisplayManager(context.getApplicationContext());
        }
        return sManager;
    }

    private static final int CACHE_SIZE = 8 * 1024 * 1024;

    private ImageLoader mImageLoader = ImageLoader.getInstance();
    private ImageLoaderConfiguration mConfiguration = null;
    private DisplayImageOptions mDefaultOptions = null;

    private DisplayManager(Context context) {
        super(context);
        if (!mImageLoader.isInited()) {
            mDefaultOptions = new DisplayImageOptions.Builder()
                    /*.show*/
                    .cacheOnDisk(true)
                    .cacheInMemory(true)
                    .build();
            mConfiguration = new ImageLoaderConfiguration.Builder(context)
                    .defaultDisplayImageOptions(mDefaultOptions)
                    .diskCacheSize(CACHE_SIZE)
                    .memoryCacheSize(CACHE_SIZE)
                    .threadPoolSize(5)
                    .diskCacheFileCount(100)
                    .build();
            mImageLoader.init(mConfiguration);
        }
    }

    public ImageLoader getImageLoader () {
        return mImageLoader;
    }

    public void display (String uri, ImageView iv, DisplayImageOptions options,
                         ImageLoadingListener loadingListener, ImageLoadingProgressListener progressListener) {
        mImageLoader.displayImage(uri, iv, options, loadingListener, progressListener);
    }

    public void display (String uri, ImageView iv, DisplayImageOptions options,
                         ImageLoadingListener loadingListener) {
        mImageLoader.displayImage(uri, iv, options, loadingListener);
    }

    public void display (String uri, ImageView iv, DisplayImageOptions options) {
        mImageLoader.displayImage(uri, iv, options);
    }

    public void display (String uri, ImageView iv) {
        mImageLoader.displayImage(uri, iv);
    }
}
