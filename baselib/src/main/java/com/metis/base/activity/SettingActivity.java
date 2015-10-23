package com.metis.base.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Toast;

import com.metis.base.R;
import com.metis.base.manager.DisplayManager;
import com.metis.base.utils.FileUtils;
import com.metis.base.widget.KeyValueLayout;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import java.io.File;

public class SettingActivity extends TitleBarActivity implements View.OnClickListener {

    private static final String TAG = SettingActivity.class.getSimpleName();

    private KeyValueLayout mCacheKvl, mCheck4UpdateKvl, mAboutKvl;
    private File mCacheDir = null;
    private long mCacheSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mCacheKvl = (KeyValueLayout)findViewById(R.id.setting_clear_cache);
        mCheck4UpdateKvl = (KeyValueLayout)findViewById(R.id.setting_check_for_update);
        mAboutKvl = (KeyValueLayout)findViewById(R.id.setting_about_us);

        mCacheKvl.setOnClickListener(this);
        mCheck4UpdateKvl.setOnClickListener(this);
        mAboutKvl.setOnClickListener(this);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            mCheck4UpdateKvl.setValue(getString(R.string.text_about_current_version, info.versionName));
            //Toast.makeText(this, getString(R.string.about_current_version) + ":" + info.versionName, Toast.LENGTH_SHORT).show();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mCacheDir = DisplayManager.getInstance(this).getImageLoader().getDiskCache().getDirectory();

        if (mCacheDir != null) {
            mCacheSize = FileUtils.getDirectorySpace(mCacheDir);
            if (mCacheSize > 0) {
                mCacheKvl.setValue(Formatter.formatFileSize(this, mCacheSize));
            }
        }

    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    public CharSequence getTitleCenter() {
        return getString(R.string.title_activity_setting);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == mAboutKvl.getId()) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == mCacheKvl.getId()) {
            if (mCacheDir != null && mCacheSize > 0) {
                final int count = FileUtils.clearFile(mCacheDir);
                mCacheSize = FileUtils.getDirectorySpace(mCacheDir);
                if (mCacheSize == 0) {
                    mCacheKvl.setValue("");
                } else {
                    mCacheKvl.setValue(Formatter.formatFileSize(this, mCacheSize));
                }
                Toast.makeText(this, getString(R.string.text_clear_cache_delete_file_count, count), Toast.LENGTH_SHORT).show();
            }
        } else if (id == mCheck4UpdateKvl.getId()) {
            UmengUpdateAgent.forceUpdate(this);
            Toast.makeText(this, R.string.text_checking_for_update, Toast.LENGTH_SHORT).show();
        }
    }
}
