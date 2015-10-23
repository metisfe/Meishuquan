package com.metis.base.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.metis.base.R;
import com.metis.base.widget.KeyValueLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AboutActivity extends TitleBarActivity implements View.OnClickListener {

    private KeyValueLayout mStarKvl, mAboutKvl, mDontBlameKvl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mStarKvl = (KeyValueLayout)findViewById(R.id.about_star_me);
        mAboutKvl = (KeyValueLayout)findViewById(R.id.about_meishuquan);
        mDontBlameKvl = (KeyValueLayout)findViewById(R.id.about_do_not_blame_me);

        mStarKvl.setOnClickListener(this);
        mAboutKvl.setOnClickListener(this);
        mDontBlameKvl.setOnClickListener(this);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    public CharSequence getTitleCenter() {
        return getString(R.string.text_about_us);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == mStarKvl.getId()) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + /*"com.android.chrome"*/this.getPackageName()));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "no activity found to handle this intent", Toast.LENGTH_SHORT).show();
            }
        } else if (id == mAboutKvl.getId()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(readStringFromAssets("about"));
            builder.setTitle(R.string.text_about_meishuquan);
            builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else if (id == mDontBlameKvl.getId()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(readStringFromAssets("statement"));
            builder.setTitle(R.string.text_do_not_blame_me);
            builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    private String readStringFromAssets (String path) {
        try {
            InputStream is = getAssets().open(path);
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(reader);
            String content = null;
            StringBuilder sb = new StringBuilder();
            while ((content = br.readLine()) != null) {
                sb.append(content + "\n");
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
