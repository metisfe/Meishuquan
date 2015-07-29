package com.metis.commentpart.activity;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.metis.base.ActivityDispatcher;
import com.metis.base.activity.TitleBarActivity;
import com.metis.base.fragment.ImageChooseDialogFragment;
import com.metis.base.utils.Log;
import com.metis.commentpart.R;

public class PublishStatusActivity extends TitleBarActivity implements View.OnClickListener{

    private static final String TAG = PublishStatusActivity.class.getSimpleName();

    private static final int REQUEST_CODE_INVITE = 0, REQUEST_CODE_GET_IMAGE = 1;

    private ImageView mImageAdd = null;
    private EditText mInputEt = null;
    private Button mCategoryBtn, mInviteBtn;

    private GridLayout mCategoryLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_status);

        mImageAdd = (ImageView)findViewById(R.id.status_image);
        mInputEt = (EditText)findViewById(R.id.status_input);
        mCategoryBtn = (Button)findViewById(R.id.status_category_btn);
        mInviteBtn = (Button)findViewById(R.id.status_invite_teacher_btn);
        mCategoryLayout = (GridLayout)findViewById(R.id.status_category_layout);

        mImageAdd.setOnClickListener(this);
        mCategoryBtn.setOnClickListener(this);
        mInviteBtn.setOnClickListener(this);

        getTitleBar().setDrawableResourceRight(R.drawable.ic_send);
        getTitleBar().setOnRightBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public CharSequence getTitleCenter() {
        return getString(R.string.tab_title_center);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == mImageAdd.getId()) {
            ImageChooseDialogFragment fragment = new ImageChooseDialogFragment();
            fragment.show(getSupportFragmentManager(), TAG);
            //ActivityDispatcher.getImage(this, REQUEST_CODE_GET_IMAGE);
        } else if (id == mCategoryBtn.getId()) {
            int visible = mCategoryLayout.getVisibility();
            if (visible == View.VISIBLE) {
                mCategoryLayout.setVisibility(View.GONE);
            } else {
                mCategoryLayout.setVisibility(View.VISIBLE);
            }
        } else if (id == mInviteBtn.getId()) {
            Intent it = new Intent(this, InviteActivity.class);
            startActivityForResult(it, REQUEST_CODE_INVITE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_INVITE:
                break;
            case REQUEST_CODE_GET_IMAGE:
                if (resultCode == RESULT_OK) {
                    Log.v(TAG, "onActivityResult=" + data);
                }
                break;
        }
    }
}
