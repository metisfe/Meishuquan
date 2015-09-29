package com.metis.base.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Parcelable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.metis.base.ActivityDispatcher;
import com.metis.base.R;
import com.metis.base.fragment.MultiImagePreviewFragment;
import com.metis.base.manager.DisplayManager;
import com.metis.base.utils.FileUtils;
import com.metis.base.widget.ImagePreviewable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageViewerActivity extends TitleBarActivity {

    private MultiImagePreviewFragment mPreviewFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        mPreviewFragment = (MultiImagePreviewFragment)getSupportFragmentManager().findFragmentById(R.id.preview_fragment);
        mPreviewFragment.setOnOperateListener(new MultiImagePreviewFragment.OnImageOperateListener() {
            @Override
            public void onPageChange(int position, ImagePreviewable image) {

            }

            @Override
            public void onPageTab(int position, ImagePreviewable image) {
                finish();
            }

            @Override
            public void onPageLongClick(int position, final ImagePreviewable image, final File cacheFile) {
                if (cacheFile == null) {
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(ImageViewerActivity.this);
                builder.setItems(
                        new String[]{getString(R.string.text_save_image)},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File targetFile = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), FileUtils.getNameFromUrl(cacheFile.getAbsolutePath()));
                                try {
                                    FileUtils.copyFileTo(cacheFile, targetFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (targetFile.exists()) {
                                    DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                                    downloadManager.addCompletedDownload(targetFile.getName(), targetFile.getAbsolutePath(), true, "image/*", targetFile.getAbsolutePath(), targetFile.length(), true);
                                }
                                //FileUtils.copyFileTo(DisplayManager.getInstance(c))
                            }
                        }
                );
                builder.create().show();
            }
        });
    }

    @Override
    public int getTitleVisibility() {
        return View.GONE;
    }
}
