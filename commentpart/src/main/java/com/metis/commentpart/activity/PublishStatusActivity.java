package com.metis.commentpart.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.base.ActivityDispatcher;
import com.metis.base.activity.TitleBarActivity;
import com.metis.base.fragment.ImageChooseDialogFragment;
import com.metis.base.manager.DisplayManager;
import com.metis.base.utils.Log;
import com.metis.commentpart.R;

import java.io.FileNotFoundException;

public class PublishStatusActivity extends TitleBarActivity implements View.OnClickListener{

    private static final String TAG = PublishStatusActivity.class.getSimpleName();

    private static final int REQUEST_CODE_INVITE = 0;

    private static final int MAX_COUNT = 200;

    private ImageView mImageAdd = null, mImageDelIv = null;
    private EditText mInputEt = null;
    private TextView mRemainCountTv = null;
    private Button mCategoryBtn, mInviteBtn;

    private GridLayout mCategoryLayout = null, mTeacherLayout;

    private Bitmap mBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_status);

        mImageAdd = (ImageView)findViewById(R.id.status_image);
        mImageDelIv = (ImageView)findViewById(R.id.status_image_delete);
        mInputEt = (EditText)findViewById(R.id.status_input);
        mRemainCountTv = (TextView)findViewById(R.id.status_remain_words);
        mCategoryBtn = (Button)findViewById(R.id.status_category_btn);
        mInviteBtn = (Button)findViewById(R.id.status_invite_teacher_btn);
        mCategoryLayout = (GridLayout)findViewById(R.id.status_category_layout);
        mTeacherLayout = (GridLayout)findViewById(R.id.status_teacher_contaner);

        mImageAdd.setOnClickListener(this);
        mImageDelIv.setOnClickListener(this);
        mCategoryBtn.setOnClickListener(this);
        mInviteBtn.setOnClickListener(this);

        mRemainCountTv.setText(MAX_COUNT + "");

        mInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.v(TAG, "beforeTextChanged " + s + " start=" + start + " count=" + count + " after=" + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.v(TAG, "onTextChanged " + s + " start=" + start + " count=" + count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mRemainCountTv.setText((MAX_COUNT - s.length()) + "");
                Log.v(TAG, "afterTextChanged " + s);
            }
        });

        getTitleBar().setDrawableResourceRight(R.drawable.ic_send);
        getTitleBar().setOnRightBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mInputEt.getText().toString();
                if (mBitmap == null || TextUtils.isEmpty(content)) {
                    Toast.makeText(PublishStatusActivity.this, R.string.publish_toast_empty_content, Toast.LENGTH_SHORT).show();
                    return;
                }
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
        }  else if (id == mImageDelIv.getId()) {
            mImageAdd.setImageDrawable(null);
            recycleBitmap();
            mImageAdd.setImageResource(R.drawable.ic_image_add);
            mImageDelIv.setVisibility(View.GONE);
        }else if (id == mCategoryBtn.getId()) {
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
            case ImageChooseDialogFragment.REQUEST_CODE_GET_IMAGE_GALLERY:
                if (resultCode == RESULT_OK) {
                    recycleBitmap();
                    ContentResolver cr = getContentResolver();
                    try {
                        mBitmap = BitmapFactory.decodeStream(cr.openInputStream(data.getData()));
                        mImageAdd.setImageBitmap(mBitmap);
                        mImageDelIv.setVisibility(View.VISIBLE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    //DisplayManager.getInstance(this).display(data.getData().toString(), mImageAdd);
                    Log.v(TAG, "onActivityResult=" + data);
                }
                break;
            case ImageChooseDialogFragment.REQUEST_CODE_GET_IMAGE_CAMERA:
                if (resultCode == RESULT_OK) {
                    recycleBitmap();
                    Bundle bundle = data.getExtras();
                    mBitmap = (Bitmap)bundle.get("data");
                    mImageAdd.setImageBitmap(mBitmap);
                    mImageDelIv.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void recycleBitmap () {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.isRecycled();
            mBitmap = null;
        }
    }
}
