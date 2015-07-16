package com.metis.coursepart.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.metis.base.TitleBarActivity;
import com.metis.base.manager.CacheDirManager;
import com.metis.base.utils.FileUtils;
import com.metis.base.utils.Log;
import com.metis.base.widget.TitleBar;
import com.metis.coursepart.R;
import com.metis.coursepart.module.GalleryItem;
import com.metis.coursepart.module.KeyWord;

import java.io.File;
import java.io.IOException;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Beak on 2015/7/9.
 */
public class CourseGalleryItemFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = CourseGalleryItemFragment.class.getSimpleName();

    private GalleryItem mItem = null;

    private PhotoView mPhotoView = null;
    private LinearLayout mDetailLayout = null;
    private TextView mDetailTv = null;
    private TextView mReadCountTv = null;
    private TextView mSaveBtn = null;
    private LinearLayout mTagsLayout = null;

    private File mCacheFile = null;

    private boolean isDetailVisible = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_gallery_item, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPhotoView = (PhotoView)view.findViewById(R.id.item_detail_photo);
        mDetailLayout = (LinearLayout)view.findViewById(R.id.item_detail_panel);
        mDetailTv = (TextView)view.findViewById(R.id.item_detail_info);
        mReadCountTv = (TextView)view.findViewById(R.id.item_detail_read_count);
        mSaveBtn = (TextView)view.findViewById(R.id.item_detail_save_btn);
        mTagsLayout = (LinearLayout)view.findViewById(R.id.item_detail_tag_container);

        mPhotoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {

                if (isDetailVisible) {
                    hideDetail();
                } else {
                    showDetail();
                }
            }
        });
        mSaveBtn.setOnClickListener(this);
        setGalleryItem(mItem);
    }

    private void hideDetail () {
        TitleBarActivity activity = (TitleBarActivity)getActivity();
        TitleBar titleBar = activity.getTitleBar();
        if (titleBar.getTranslationY() == 0) {
            ObjectAnimator titleBarAnimator = ObjectAnimator.ofFloat(titleBar, "translationY", 0, -titleBar.getHeight());
            titleBarAnimator.start();
        }

        ObjectAnimator animator = ObjectAnimator.ofFloat(mDetailLayout, "translationY", 0, mDetailLayout.getHeight());
        animator.start();
        isDetailVisible = false;
    }

    private void showDetail () {
        TitleBarActivity activity = (TitleBarActivity)getActivity();
        TitleBar titleBar = activity.getTitleBar();

        if (titleBar.getTranslationY() == -titleBar.getHeight()) {
            ObjectAnimator titleBarAnimator = ObjectAnimator.ofFloat(titleBar, "translationY", -titleBar.getHeight(), 0);
            titleBarAnimator.start();
        }

        ObjectAnimator animator = ObjectAnimator.ofFloat(mDetailLayout, "translationY", mDetailLayout.getHeight(), 0);
        animator.start();
        isDetailVisible = true;
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
        List<KeyWord> keyWordList = item.keyWordList;
        if (keyWordList != null && !keyWordList.isEmpty()) {
            final int length = keyWordList.size();
            mTagsLayout.removeAllViews();
            for (int i = 0; i < length; i++) {
                View child = LayoutInflater.from(getActivity()).inflate(R.layout.layout_tag_item, null);
                TextView tagTv = (TextView)child.findViewById(R.id.tag_name);
                tagTv.setText(keyWordList.get(i).keyWordName);
                mTagsLayout.addView(child);
            }
        }
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.item_detail_save_btn) {
            if (mCacheFile != null && mCacheFile.exists()) {
                File dest = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), mCacheFile.getName());
                if (dest != null) {
                    try {
                        boolean success = FileUtils.copyFileTo(mCacheFile, dest);
                        if (success) {
                            Toast.makeText(getActivity(), getString(R.string.gallery_save_to, dest.getAbsolutePath()), Toast.LENGTH_SHORT).show();
                            DownloadManager downloadManager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                            downloadManager.addCompletedDownload(dest.getName(), dest.getAbsolutePath(), true, "image/*", dest.getAbsolutePath(), dest.length(), true);
                        } else {
                            Toast.makeText(getActivity(), R.string.gallery_save_failed, Toast.LENGTH_SHORT).show();
                            Log.v(TAG, "save ");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.v(TAG, "save IOException");
                        Toast.makeText(getActivity(), R.string.gallery_save_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }
}
