package com.metis.commentpart.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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
import com.metis.base.module.ImageInfo;
import com.metis.base.module.Thumbnail;
import com.metis.base.module.User;
import com.metis.base.utils.FileUtils;
import com.metis.base.utils.Log;
import com.metis.base.utils.SystemUtils;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.TeacherSelectedAdapter;
import com.metis.commentpart.adapter.delegate.TeacherCbDelegate;
import com.metis.commentpart.manager.StatusManager;
import com.metis.commentpart.manager.TeacherManager;
import com.metis.commentpart.module.AssessChannel;
import com.metis.commentpart.module.ChannelItem;
import com.metis.commentpart.module.Teacher;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.io.FileNotFoundException;
import java.util.ArrayList;
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

    private GridLayout mCategoryLayout = null;
    private RecyclerView mTeacherLayout;

    private Bitmap mBitmap = null;
    private TeacherSelectedAdapter mTeacherAdapter = null;
    private TeacherManager mTeacherManager = null;

    private ChannelItem mCurrentChannel = null;
    private int mCurrentChannelIndex = -1;

    private ImageChooseDialogFragment mImageChooseDialogFragment = null;

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
        mTeacherLayout = (RecyclerView)findViewById(R.id.status_teacher_contaner);

        mImageAdd.setOnClickListener(this);
        mImageDelIv.setOnClickListener(this);
        mCategoryBtn.setOnClickListener(this);
        mInviteBtn.setOnClickListener(this);
        mInviteBtn.setText(getString(R.string.publish_btn_invite, mTeacherManager.getSelectedCount() + "/" + mTeacherManager.getMaxCount()));

        mTeacherLayout.setLayoutManager(new GridLayoutManager(this, 3));
        mTeacherAdapter = new TeacherSelectedAdapter(this);
        mTeacherLayout.setAdapter(mTeacherAdapter);

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
                int remain = MAX_COUNT - s.length();
                mRemainCountTv.setText(remain + "");
                if (remain < 0) {
                    mRemainCountTv.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                } else {
                    mRemainCountTv.setTextColor(getResources().getColor(R.color.color_c2));
                }
                Log.v(TAG, "afterTextChanged " + s);
            }
        });

        getTitleBar().setDrawableResourceRight(R.drawable.ic_send);
        getTitleBar().setOnRightBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = mInputEt.getText().toString();
                if (mBitmap == null && TextUtils.isEmpty(content)) {
                    Toast.makeText(PublishStatusActivity.this, R.string.publish_toast_empty_content, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (content.length() > MAX_COUNT) {
                    Toast.makeText(PublishStatusActivity.this, getString(R.string.publish_toast_beyond_max, MAX_COUNT), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mCurrentChannel == null) {
                    Toast.makeText(PublishStatusActivity.this, R.string.publish_toast_choose_category, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mBitmap != null) {
                    final Bitmap compressedBmp = FileUtils.compressBitmap(mBitmap, 1000);
                    Log.v(TAG, "compressedBmp.width=" + compressedBmp.getWidth() + " compressedBmp.height=" + compressedBmp.getHeight());
                    UploadManager.getInstance(PublishStatusActivity.this).uploadBitmap(
                            compressedBmp,
                            AccountManager.getInstance(PublishStatusActivity.this).getMe().getCookie(),
                            new RequestCallback<List<Thumbnail>>() {
                                @Override
                                public void callback(ReturnInfo<List<Thumbnail>> returnInfo, String callbackId) {
                                    if (!returnInfo.isSuccess()) {
                                        return;
                                    }
                                    compressedBmp.recycle();
                                    Thumbnail thumbnail = returnInfo.getData().get(0);
                                    Log.v(TAG, "publish access with " + thumbnail.getOriginalImage());
                                    publishContent(content, thumbnail.toImageInfo());
                                }
                            });
                } else {
                    publishContent(content, null);
                }
                finish();

            }
        });
        AssessChannel channel = StatusManager.getInstance(this).getAssessChannel();
        if (channel != null) {
            fillCategoryGrid(channel.assessChannel);
        } else {
            StatusManager.getInstance(this).getChannelList(new RequestCallback<AssessChannel>() {
                @Override
                public void callback(ReturnInfo<AssessChannel> returnInfo, String callbackId) {
                    if (returnInfo.isSuccess()) {
                        fillCategoryGrid(returnInfo.getData().assessChannel);
                    }
                }
            });
        }

    }

    private void publishContent (String content, ImageInfo thumbnail) {

        if (content == null) {
            content = "";
        }
        List<Long> teachersId = new ArrayList<Long>();
        final int length = mTeacherManager.getSelectedCount();
        List<TeacherCbDelegate> delegates = mTeacherManager.getSelectedTeachers();
        for (int i = 0; i < length; i++) {
            teachersId.add(delegates.get(i).getSource().user.userId);
        }
        User me = AccountManager.getInstance(this).getMe();
        if (me == null) {
            Toast.makeText(this, R.string.publish_toast_offline, Toast.LENGTH_SHORT).show();
            return;
        }
        StatusManager.getInstance(PublishStatusActivity.this).publishAssess(content, mCurrentChannel.channelId, teachersId, thumbnail, me.getCookie(), new RequestCallback() {
            @Override
            public void callback(ReturnInfo returnInfo, String callbackId) {
                Toast.makeText(PublishStatusActivity.this, returnInfo.isSuccess() ? R.string.publish_toast_publish_success : R.string.publish_toast_publish_failed, Toast.LENGTH_SHORT).show();
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
            mImageChooseDialogFragment = new ImageChooseDialogFragment();
            mImageChooseDialogFragment.show(getSupportFragmentManager(), TAG);
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
                    Log.v(TAG, "PATH=" + mImageChooseDialogFragment.getLastCapturePath());
                    mBitmap = BitmapFactory.decodeFile(mImageChooseDialogFragment.getLastCapturePath());
                    mImageAdd.setImageBitmap(mBitmap);
                    mImageDelIv.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void fillCategoryGrid (List<ChannelItem> itemList) {
        mCategoryLayout.removeAllViews();
        final int length = itemList.size();
        for (int i = 0; i < length; i++) {
            final int index = i;
            final ChannelItem item = itemList.get(i);
            if (item != null) {
                final View view = LayoutInflater.from(this).inflate(R.layout.layout_category_item, null);
                final TextView tv = (TextView)view.findViewById(R.id.category_name);
                tv.setText(item.name);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCurrentChannelIndex >= 0) {
                            mCategoryLayout.getChildAt(mCurrentChannelIndex).setSelected(false);
                        }
                        view.setSelected(true);
                        mCurrentChannelIndex = index;
                        mCurrentChannel = item;
                        SystemUtils.hideIME(PublishStatusActivity.this, mInputEt);
                    }
                });
                mCategoryLayout.addView(view);
            }
        }
    }

    /*private void pushContent (String content, int channelId,
                              List<Long> teacherIds, Thumbnail thumbnail) {
        StatusManager.getInstance(this).publishAssess(content, channelId, teacherIds, thumbnail, new RequestCallback() {
            @Override
            public void callback(ReturnInfo returnInfo, String callbackId) {

            }
        });
    }*/

    private void recycleBitmap () {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.isRecycled();
            mBitmap = null;
        }
    }

    @Override
    public void onConfirmed(List<Teacher> teachers) {
        //mInviteBtn.setText(getString(R.string.publish_btn_invite, mTeacherManager.getSelectedCount() + "/" + mTeacherManager.getMaxCount()));
    }

    @Override
    public void onSelected(TeacherManager manager, Teacher teacher) {
        mTeacherAdapter.notifyDataSetChanged();
        mTeacherLayout.setVisibility(manager.getSelectedCount() > 0 ? View.VISIBLE : View.GONE);
        mInviteBtn.setText(getString(R.string.publish_btn_invite, mTeacherManager.getSelectedCount() + "/" + mTeacherManager.getMaxCount()));
    }

    @Override
    public void onUnSelected(TeacherManager manager, Teacher teacher) {
        mTeacherAdapter.notifyDataSetChanged();
        mTeacherLayout.setVisibility(manager.getSelectedCount() > 0 ? View.VISIBLE : View.GONE);
        mInviteBtn.setText(getString(R.string.publish_btn_invite, mTeacherManager.getSelectedCount() + "/" + mTeacherManager.getMaxCount()));
    }

}
