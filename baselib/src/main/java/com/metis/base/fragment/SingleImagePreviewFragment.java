package com.metis.base.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.metis.base.R;
import com.metis.base.manager.CacheManager;
import com.metis.base.manager.DisplayManager;
import com.metis.base.utils.FileUtils;
import com.metis.base.widget.ImagePreviewable;

import java.io.File;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Beak on 2015/7/20.
 */
public class SingleImagePreviewFragment extends BaseFragment implements PhotoViewAttacher.OnViewTapListener, View.OnLongClickListener {

    private PhotoView mPhotoView = null;
    private ProgressBar mProgressBar = null;

    private ImagePreviewable mPreviewable = null;

    private OnImageTabListener mTabListener = null;

    private File mImageFile = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate (R.layout.fragment_single_image_preview, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPhotoView = (PhotoView)view.findViewById(R.id.preview_photo_view);
        mProgressBar = (ProgressBar)view.findViewById(R.id.preview_progress_bar);

        setImagePreviewable(mPreviewable);
        mPhotoView.setOnViewTapListener(this);
        mPhotoView.setOnLongClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPhotoView.setOnViewTapListener(null);
        mPhotoView.setOnLongClickListener(null);
    }

    public void setOnImageTabListener (OnImageTabListener listener) {
        mTabListener = listener;
    }

    public ImagePreviewable getPreviewable () {
        return mPreviewable;
    }

    public File getImageFile () {
        return mImageFile;
    }

    public void setImagePreviewable (ImagePreviewable previewable) {
        mPreviewable = previewable;
        if (mPhotoView != null && previewable != null) {
            mImageFile = new File(CacheManager.getInstance(getActivity()).getCacheFolder("temp"), FileUtils.getNameFromUrl(previewable.getUrl()));
            if (mImageFile.exists()) {
                Bitmap bmp = BitmapFactory.decodeFile(mImageFile.getAbsolutePath());
                mPhotoView.setImageBitmap(bmp);
            } else {
                DisplayManager.getInstance(getActivity()).display(
                        previewable.getThumbnail(),
                        mPhotoView);
                HttpUtils httpUtils = new HttpUtils(1000 * 10);
                mProgressBar.setVisibility(View.VISIBLE);
                mPhotoView.setZoomable(false);
                httpUtils.download(
                        previewable.getUrl(),
                        mImageFile.getAbsolutePath(),
                        true, true, new RequestCallBack<File>() {
                            @Override
                            public void onSuccess(ResponseInfo<File> responseInfo) {
                                if (!isAlive()) {
                                    return;
                                }
                                mProgressBar.setVisibility(View.GONE);
                                mPhotoView.setZoomable(true);
                                Bitmap bmp = BitmapFactory.decodeFile(responseInfo.result.getAbsolutePath());
                                mPhotoView.setImageBitmap(bmp);
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                if (!isAlive()) {
                                    return;
                                }
                                mProgressBar.setVisibility(View.GONE);
                                mPhotoView.setImageResource(R.drawable.image_broken);
                            }
                        });
            }
        }
    }

    @Override
    public void onViewTap(View view, float x, float y) {
        if (mTabListener != null) {
            mTabListener.onImageTab(mPreviewable);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mTabListener != null) {
            mTabListener.onImageLongClick(mPreviewable, mImageFile);
        }
        return false;
    }

    public static interface OnImageTabListener {
        public void onImageTab (ImagePreviewable previewable);
        public void onImageLongClick (ImagePreviewable previewable, File cacheFile);
    }
}
