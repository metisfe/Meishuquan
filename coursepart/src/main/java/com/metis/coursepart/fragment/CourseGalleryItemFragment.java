package com.metis.coursepart.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.metis.base.manager.CacheDirManager;
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
    private TextView mDetailTv = null;
    private TextView mReadCountTv = null;

    private File mCacheFile = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_gallery_item, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPhotoView = (PhotoView)view.findViewById(R.id.item_detail_photo);
        mDetailTv = (TextView)view.findViewById(R.id.item_detail_info);
        mReadCountTv = (TextView)view.findViewById(R.id.item_detail_read_count);

        setGalleryItem(mItem);
    }

    public void setGalleryItem (final GalleryItem item) {
        mItem = item;
        if (mPhotoView != null && item != null) {
            fillContent(mItem);
            mCacheFile = new File(CacheDirManager.getInstance(getActivity()).getCacheFolder("file").getAbsolutePath() + File.separator + item.picId + ".jpg");
            if (mCacheFile.exists()) {
                fillImage(mCacheFile.getAbsolutePath(), item);
            } else {
                new HttpUtils ().download(
                        item.originalUrl,
                        mCacheFile.getAbsolutePath(),
                        new RequestCallBack<File>() {
                            @Override
                            public void onSuccess(ResponseInfo<File> responseInfo) {
                                fillImage(responseInfo.result.getAbsolutePath(), item);
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {

                            }
                        });
            }
        }
    }
    private void fillContent (GalleryItem item) {
        mDetailTv.setText(item.descripiton);
        mReadCountTv.setText(getString(R.string.gallery_read_count, item.viewCount));
    }
    private void fillImage (String path, GalleryItem item) {
        mPhotoView.setImageURI(Uri.parse(path));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCacheFile != null) {
            mCacheFile.deleteOnExit();
        }
    }
}
