package com.metis.coursepart.activity;

import android.animation.ObjectAnimator;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.metis.base.activity.TitleBarActivity;
import com.metis.base.fragment.MultiImagePreviewFragment;
import com.metis.base.manager.CacheDirManager;
import com.metis.base.manager.DisplayManager;
import com.metis.base.module.User;
import com.metis.base.utils.FileUtils;
import com.metis.base.utils.Log;
import com.metis.base.widget.ImagePreviewable;
import com.metis.base.widget.ProfileNameView;
import com.metis.base.widget.TitleBar;
import com.metis.coursepart.ActivityDispatcher;
import com.metis.coursepart.R;
import com.metis.coursepart.fragment.CourseGalleryItemFragment;
import com.metis.coursepart.module.GalleryItem;
import com.metis.coursepart.module.KeyWord;
import com.metis.coursepart.module.StudioInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GalleryItemDetailActivity extends TitleBarActivity
        implements MultiImagePreviewFragment.OnImageOperateListener, View.OnClickListener{

    private static final String TAG = GalleryItemDetailActivity.class.getSimpleName();

    private ProfileNameView mProfileNameView = null;

    private MultiImagePreviewFragment mPreviewFragment = null;
    private LinearLayout mDetailLayout, mDetailTagContainer;
    private TextView mDetailInfoTv, mDetailSaveBtn, mViewCountTv;

    private GalleryItem mCurrentItem = null;

    private HttpHandler<File> mDownloadHandler = null;

    private boolean isDetailVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_item_detail);

        mPreviewFragment = (MultiImagePreviewFragment)getSupportFragmentManager().findFragmentById(R.id.detail_multi_image_fragment);
        mDetailLayout = (LinearLayout)findViewById(R.id.item_detail_panel);
        mDetailTagContainer = (LinearLayout)findViewById(R.id.item_detail_tag_container);
        mDetailInfoTv = (TextView)findViewById(R.id.item_detail_info);
        mDetailSaveBtn = (TextView)findViewById(R.id.item_detail_save_btn);
        mViewCountTv = (TextView)findViewById(R.id.item_detail_read_count);

        mDetailSaveBtn.setOnClickListener(this);

        mProfileNameView = new ProfileNameView(this);
        getTitleBar().setCenterView(mProfileNameView);

        mPreviewFragment.setOnOperateListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean isTitleBarOverlay() {
        return true;
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    public void onPageChange(int position, ImagePreviewable image) {
        if (image instanceof GalleryItem) {
            mCurrentItem = (GalleryItem)image;

            User info = mCurrentItem.studio;
            if (info != null) {
                mProfileNameView.setProfile(info.avatar, DisplayManager.getInstance(this).makeRoundDisplayImageOptions(getResources().getDimensionPixelSize(R.dimen.gallery_title_profile_size)));
                mProfileNameView.setName(info.name);
            } else {
                mProfileNameView.setProfile(null);
                mProfileNameView.setName("");
                if (!TextUtils.isEmpty(mCurrentItem.source)) {
                    mProfileNameView.setName(mCurrentItem.source);
                }
            }

            mDetailInfoTv.setText(mCurrentItem.descripiton);
            mViewCountTv.setText(getString(R.string.gallery_read_count, mCurrentItem.viewCount));
            KeyWord[] keyWordList = mCurrentItem.keyWordList;

            if (keyWordList != null/* && !keyWordList.isEmpty()*/) {
                final int length = keyWordList.length;
                mDetailTagContainer.removeAllViews();
                for (int i = 0; i < length; i++) {
                    View child = LayoutInflater.from(this).inflate(R.layout.layout_tag_item, null);
                    TextView tagTv = (TextView)child.findViewById(R.id.tag_name);
                    KeyWord keyWord = keyWordList[i];
                    if (keyWord != null) {
                        tagTv.setText(keyWord.keyWordName);
                        mDetailTagContainer.addView(child);
                    }
                }
            }
        }
    }

    @Override
    public void onPageTab(int position, ImagePreviewable image) {
        if (isDetailVisible) {
            hideDetail();
        } else {
            showDetail();
        }
    }

    private void hideDetail () {
        TitleBar titleBar = getTitleBar();
        if (titleBar.getTranslationY() == 0) {
            ObjectAnimator titleBarAnimator = ObjectAnimator.ofFloat(titleBar, "translationY", 0, -titleBar.getHeight());
            titleBarAnimator.start();
        }

        ObjectAnimator animator = ObjectAnimator.ofFloat(mDetailLayout, "translationY", 0, mDetailLayout.getHeight());
        animator.start();
        isDetailVisible = false;
    }

    private void showDetail () {
        TitleBar titleBar = getTitleBar();

        if (titleBar.getTranslationY() == -titleBar.getHeight()) {
            ObjectAnimator titleBarAnimator = ObjectAnimator.ofFloat(titleBar, "translationY", -titleBar.getHeight(), 0);
            titleBarAnimator.start();
        }

        ObjectAnimator animator = ObjectAnimator.ofFloat(mDetailLayout, "translationY", mDetailLayout.getHeight(), 0);
        animator.start();
        isDetailVisible = true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mDetailSaveBtn.getId()) {
            save(mCurrentItem);
        }
    }

    private void save (GalleryItem item) {

        File mCacheFile = mPreviewFragment.getCurrentImageFile();
        File dest = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), mCacheFile.getName());
        /*new File(CacheDirManager.getInstance(this).getCacheFolder("file").getAbsolutePath() + File.separator + FileUtils.getNameFromUrl(item.getUrl()));*/
        Log.v(TAG, "save mCacheFile=" + mCacheFile.getAbsolutePath());
        if (mCacheFile != null && mCacheFile.exists()) {

            if (dest != null) {
                try {
                    boolean success = FileUtils.copyFileTo(mCacheFile, dest);
                    if (success) {
                        onSaveSuccess(dest);
                    } else {
                        onSaveFailed();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    onSaveFailed();
                }
            }
        } else {
            HttpUtils httpUtils = new HttpUtils(10 * 1000);
            mDownloadHandler = httpUtils.download(item.getUrl(), dest.getAbsolutePath(), true, true, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    onSaveSuccess(responseInfo.result);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    onSaveFailed();
                }
            });
        }
    }

    private void onSaveSuccess (File dest) {
        Toast.makeText(this, getString(R.string.gallery_save_to, dest.getAbsolutePath()), Toast.LENGTH_SHORT).show();
        DownloadManager downloadManager = (DownloadManager)this.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.addCompletedDownload(dest.getName(), dest.getAbsolutePath(), true, "image/*", dest.getAbsolutePath(), dest.length(), true);
    }

    private void onSaveFailed () {
        Toast.makeText(this, R.string.gallery_save_failed, Toast.LENGTH_SHORT).show();
    }

    /*@Override
    public void onPageSelected(int position) {
        GalleryItem galleryItem = GalleryCacheManager.getInstance(this).getGalleryItem(mTag, position);
        ((CourseGalleryItemFragment) mAdapter.getItem(position)).setGalleryItem(galleryItem);

        final StudioInfo studioInfo = galleryItem.studio;
        if (studioInfo != null) {
            mProfileNameView.setName(studioInfo.name);
            mProfileNameView.setProfile(studioInfo.avatar,
                    DisplayManager.getInstance(this).makeRoundDisplayImageOptions(getResources().getDimensionPixelSize(R.dimen.gallery_title_profile_size)));
            mProfileNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    com.metis.base.ActivityDispatcher.userActivity(GalleryItemDetailActivity.this, studioInfo.userId);
                }
            });
        } else {
            mProfileNameView.setName(null);
            mProfileNameView.setProfile(null);
            mProfileNameView.setOnClickListener(null);
        }
    }*/

}
