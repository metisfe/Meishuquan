package com.metis.commentpart.activity;

import android.os.Environment;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.base.ActivityDispatcher;
import com.metis.base.activity.BaseActivity;
import com.metis.base.activity.TitleBarActivity;
import com.metis.base.fragment.MultiImagePreviewFragment;
import com.metis.base.manager.DisplayManager;
import com.metis.base.module.ImageInfo;
import com.metis.base.module.User;
import com.metis.base.utils.FileUtils;
import com.metis.base.widget.ImagePreviewable;
import com.metis.base.widget.ProfileNameView;
import com.metis.commentpart.R;
import com.metis.commentpart.module.Status;

import java.io.File;
import java.io.IOException;

public class StatusImageActivity extends TitleBarActivity {

    private MultiImagePreviewFragment mPreviewFragment = null;

    private TextView mSaveBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stutus_image);

        mSaveBtn = (TextView)findViewById(R.id.image_save_btn);
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = mPreviewFragment.getCurrentImageFile();
                if (file != null) {
                    File targetFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), FileUtils.getNameFromUrl(file.getAbsolutePath()));
                    try {
                        FileUtils.copyFileTo(file, targetFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (targetFile.exists()) {
                        Toast.makeText(StatusImageActivity.this, getString(R.string.toast_saved_at, targetFile.getAbsolutePath()), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(StatusImageActivity.this, R.string.toast_save_failed, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        mPreviewFragment = (MultiImagePreviewFragment)getSupportFragmentManager().findFragmentById(R.id.image_fragment);
        mPreviewFragment.setOnOperateListener(new MultiImagePreviewFragment.OnImageOperateListener() {
            @Override
            public void onPageChange(int position, ImagePreviewable image) {

            }

            @Override
            public void onPageTab(int position, ImagePreviewable image) {
                finish();
            }
        });
        Parcelable[] parcelables = getIntent().getParcelableArrayExtra(ActivityDispatcher.KEY_IMAGES);
        if (parcelables != null && parcelables.length > 0) {
            ImageInfo status = (ImageInfo)parcelables[0];
            final User user = (User)getIntent().getSerializableExtra(com.metis.commentpart.ActivityDispatcher.KEY_USER);
            if (user != null) {
                ProfileNameView profileNameView = new ProfileNameView(this);
                profileNameView.setProfile(user.getAvailableAvatar(), DisplayManager.getInstance(this).makeRoundDisplayImageOptions(
                        getResources().getDimensionPixelSize(R.dimen.profile_size_small)
                ));
                profileNameView.setName(user.name);
                getTitleBar().setCenterView(profileNameView);
                profileNameView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityDispatcher.userActivity(StatusImageActivity.this, user.userId);
                    }
                });
            }
        }

    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }


}
