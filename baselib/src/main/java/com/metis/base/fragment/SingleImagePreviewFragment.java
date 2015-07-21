package com.metis.base.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.metis.base.R;
import com.metis.base.manager.CacheDirManager;
import com.metis.base.manager.DisplayManager;
import com.metis.base.utils.FileUtils;
import com.metis.base.widget.ImagePreviewable;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Beak on 2015/7/20.
 */
public class SingleImagePreviewFragment extends BaseFragment {

    private PhotoView mPhotoView = null;
    private ProgressBar mProgressBar = null;

    private ImagePreviewable mPreviewable = null;

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
    }

    public void setImagePreviewable (ImagePreviewable previewable) {
        mPreviewable = previewable;
        if (mPhotoView != null && previewable != null) {
            File file = new File(CacheDirManager.getInstance(getActivity()).getCacheFolder("temp"), FileUtils.getNameFromUrl(previewable.getUrl()));
            if (file.exists()) {
                Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
                mPhotoView.setImageBitmap(bmp);
            } else {
                DisplayManager.getInstance(getActivity()).display(
                        previewable.getThumbnail(),
                        mPhotoView);
                HttpUtils httpUtils = new HttpUtils(1000 * 10);
                mProgressBar.setVisibility(View.VISIBLE);
                httpUtils.download(
                        previewable.getUrl(),
                        file.getAbsolutePath(),
                        true, true, new RequestCallBack<File>() {
                            @Override
                            public void onSuccess(ResponseInfo<File> responseInfo) {
                                mProgressBar.setVisibility(View.GONE);
                                Bitmap bmp = BitmapFactory.decodeFile(responseInfo.result.getAbsolutePath());
                                mPhotoView.setImageBitmap(bmp);
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                mProgressBar.setVisibility(View.GONE);
                            }
                        });
            }
        }
    }
}
