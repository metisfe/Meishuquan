package com.metis.commentpart.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.base.activity.TitleBarActivity;
import com.metis.base.fragment.ImageChooseDialogFragment;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.manager.UploadManager;
import com.metis.base.module.Thumbnail;
import com.metis.base.utils.Log;
import com.metis.commentpart.R;
import com.metis.commentpart.manager.StatusManager;
import com.metis.commentpart.manager.TeacherManager;
import com.metis.commentpart.module.Teacher;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.io.FileNotFoundException;
import java.util.List;

public class PublishStatusActivity extends TitleBarActivity implements View.OnClickListener, TeacherManager.OnTeachersListener {

    private static final String TAG = PublishStatusActivity.class.getSimpleName();

    private static final int REQUEST_CODE_INVITE = 0;

    private static final int MAX_COUNT = 200;

    private ImageView mImageAdd = null, mImageDelIv = null;
    private EditText mInputEt = null;
    private TextView mRemainCountTv = null;
    private TextView mInviteBtn;
    private RelativeLayout mCategoryBtn;
    private ImageView mCategoryFlagIv;

    private GridLayout mCategoryLayout = null, mTeacherLayout;

    private Bitmap mBitmap = null;

    private TeacherManager mTeacherManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_status);

        mTeacherManager = TeacherManager.getInstance(this);
        mTeacherManager.registerOnTeachersListener(this);

        mImageAdd = (ImageView)findViewById(R.id.status_image);
        mImageDelIv = (ImageView)findViewById(R.id.status_image_delete);
        mInputEt = (EditText)findViewById(R.id.status_input);
        mRemainCountTv = (TextView)findViewById(R.id.status_remain_words);
        mCategoryBtn = (RelativeLayout)findViewById(R.id.status_category_btn);
        mCategoryFlagIv = (ImageView)mCategoryBtn.findViewById(R.id.status_category_switch_flag);
        mInviteBtn = (TextView)findViewById(R.id.status_invite_teacher_btn);
        mCategoryLayout = (GridLayout)findViewById(R.id.status_category_layout);
        mTeacherLayout = (GridLayout)findViewById(R.id.status_teacher_contaner);

        mImageAdd.setOnClickListener(this);
        mImageDelIv.setOnClickListener(this);
        mCategoryBtn.setOnClickListener(this);
        mInviteBtn.setOnClickListener(this);
        mInviteBtn.setText(getString(R.string.publish_btn_invite, mTeacherManager.getSelectedCount() + "/" + mTeacherManager.getMaxCount()));

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
                if (mBitmap == null && TextUtils.isEmpty(content)) {
                    Toast.makeText(PublishStatusActivity.this, R.string.publish_toast_empty_content, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mBitmap != null) {
                    UploadManager.getInstance(PublishStatusActivity.this).uploadBitmap(
                            mBitmap,
                            AccountManager.getInstance(PublishStatusActivity.this).getMe().getCookie(),
                            new RequestCallback<List<Thumbnail>>() {
                                @Override
                                public void callback(ReturnInfo<List<Thumbnail>> returnInfo, String callbackId) {

                                }
                            });
                }

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTeacherManager.clearSelected();
        mTeacherManager.unregisterOnTeachersListener(this);
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
                mCategoryFlagIv.setImageResource(R.drawable.ic_chevron_down);
            } else {
                mCategoryLayout.setVisibility(View.VISIBLE);
                mCategoryFlagIv.setImageResource(R.drawable.ic_chevron_up);
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

    private void pushContent (String content, int channelId,
                              List<Long> teacherIds, Thumbnail thumbnail) {
        StatusManager.getInstance(this).publishAssess(content, channelId, teacherIds, thumbnail, new RequestCallback() {
            @Override
            public void callback(ReturnInfo returnInfo, String callbackId) {

            }
        });
    }

    private void recycleBitmap () {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.isRecycled();
            mBitmap = null;
        }
    }

    @Override
    public void onConfirmed(List<Teacher> teachers) {
        mInviteBtn.setText(getString(R.string.publish_btn_invite, mTeacherManager.getSelectedCount() + "/" + mTeacherManager.getMaxCount()));
    }

    @Override
    public void onSelected(TeacherManager manager, Teacher teacher) {

    }

    @Override
    public void onUnSelected(TeacherManager manager, Teacher teacher) {

    }

}
