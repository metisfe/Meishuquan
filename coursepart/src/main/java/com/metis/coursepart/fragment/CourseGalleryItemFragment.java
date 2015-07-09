package com.metis.coursepart.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.metis.base.manager.CacheDirManager;
import com.metis.base.manager.DownloadManager;
import com.metis.coursepart.R;
import com.metis.coursepart.module.GalleryItem;

import java.io.File;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Beak on 2015/7/9.
 */
public class CourseGalleryItemFragment extends Fragment {

    private GalleryItem mItem = null;

    private PhotoView mPhotoView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_gallery_item, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPhotoView = (PhotoView)view.findViewById(R.id.item_detail_photo);

        setGalleryItem(mItem);
    }

    public void setGalleryItem (GalleryItem item) {
        mItem = item;
        if (mPhotoView != null && item != null) {
            new HttpUtils ().download(
                    item.url,
                    CacheDirManager.getInstance(getActivity()).getCacheFolder("file").getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg",
                    new RequestCallBack<File>() {
                        @Override
                        public void onSuccess(ResponseInfo<File> responseInfo) {
                            mPhotoView.setImageURI(Uri.parse(responseInfo.result.getAbsolutePath()));
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {

                        }
                    });
        }
    }
}
