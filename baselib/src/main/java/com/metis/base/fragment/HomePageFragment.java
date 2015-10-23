package com.metis.base.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.metis.base.ActivityDispatcher;
import com.metis.base.R;
import com.metis.base.activity.BaseActivity;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.CacheManager;
import com.metis.base.manager.DisplayManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.manager.UploadManager;
import com.metis.base.manager.UserManager;
import com.metis.base.module.SimpleProvince;
import com.metis.base.module.Thumbnail;
import com.metis.base.module.User;
import com.metis.base.utils.Log;
import com.metis.base.widget.EditTextLayout;
import com.metis.base.widget.KeyValueLayout;
import com.metis.base.widget.adapter.delegate.RoleDelegate;
import com.metis.msnetworklib.contract.ReturnInfo;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by Beak on 2015/10/8.
 */
public class HomePageFragment extends BaseFragment
        implements View.OnClickListener, BaseActivity.ActivityResultListener, BaseActivity.OnBackPressListener{

    private static final String TAG = HomePageFragment.class.getSimpleName();

    private KeyValueLayout mProfileLayout, mNickNameLayout, mGenderLayout, mRoleLayout, mProvinceLayout, mDepartLayout;
    private View mQuitView = null;

    private User mUser = null;

    private ImageView mProfileIv = null;
    private EditTextLayout mNickNameEt = null;

    private File mCameraFile = null, mCroppedFile = null;

    private boolean isEditable = false;

    private List<SimpleProvince> mSimpleProvinceList = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_page, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProfileLayout = (KeyValueLayout)view.findViewById(R.id.home_page_profile);
        mNickNameLayout = (KeyValueLayout)view.findViewById(R.id.home_page_nickname);
        mGenderLayout = (KeyValueLayout)view.findViewById(R.id.home_page_gender);
        mRoleLayout = (KeyValueLayout)view.findViewById(R.id.home_page_role);
        mProvinceLayout = (KeyValueLayout)view.findViewById(R.id.home_page_province);
        mDepartLayout = (KeyValueLayout)view.findViewById(R.id.home_page_department);

        mQuitView = view.findViewById(R.id.home_page_quit);

        if (mUser != null) {
            setUser(mUser);
        }

    }

    @Override
    public boolean onBackPressedReceived() {
        if (mNickNameLayout.hasValueView(mNickNameEt)) {
            mNickNameLayout.removeValueView(mNickNameEt);
            mNickNameLayout.setEditable(isEditable);
            mNickNameLayout.setValue(mUser.name);
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).registerOnBackPressListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onResume();
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).unregisterOnBackPressListener(this);
        }
    }

    public void setUser (User user) {
        Log.v(TAG, "setUser " + user + " " + isAlive());
        mUser = user;
        if (user == null) {
            return;
        }
        if (!isAlive()) {
            return;
        }
        User me = AccountManager.getInstance(getActivity()).getMe();
        isEditable = me != null && me.equals(user);

        mProfileIv = new ImageView(getActivity());
        Resources resources = getActivity().getResources();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(resources.getDimensionPixelSize(R.dimen.profile_size_middle), resources.getDimensionPixelSize(R.dimen.profile_size_middle));
        params.gravity = Gravity.RIGHT;
        mProfileIv.setLayoutParams(params);
        mProfileLayout.addValueView(mProfileIv);
        mProfileLayout.setEditable(isEditable);
        DisplayManager.getInstance(getActivity()).displayProfile(
                user.getAvailableAvatar(),
                mProfileIv);
        mProfileLayout.setOnClickListener(this);

        mNickNameLayout.setValue(user.name);
        mNickNameLayout.setEditable(isEditable);
        mNickNameLayout.setOnClickListener(this);

        mGenderLayout.setValue(user.getGender());
        mGenderLayout.setEditable(isEditable);
        mGenderLayout.setOnClickListener(this);

        mRoleLayout.setValue(RoleDelegate.getNameIdByRoleId(user.userRole));
        mRoleLayout.setEditable(false);

        mProvinceLayout.setValue(user.province);
        mProvinceLayout.setEditable(isEditable);
        mProvinceLayout.setOnClickListener(this);

        mDepartLayout.setValue(user.location);
        mDepartLayout.setEditable(isEditable);

        mQuitView.setVisibility(isEditable ? View.VISIBLE : View.GONE);
        mQuitView.setOnClickListener(this);
        //Toast.makeText(getActivity(), "setUser " + user.name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (!isEditable) {
            return;
        }
        final int id = v.getId();
        if (id == mProfileLayout.getId()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(
                    new String[]{getString(R.string.text_camera), getString(R.string.text_gallery)},
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                mCameraFile = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + "m_" + System.currentTimeMillis() + ".jpg");
                                Log.v(TAG, "local path=" + mCameraFile.getAbsolutePath());
                                getImageFromCamera(Uri.fromFile(mCameraFile));
                            } else if (which == 1) {
                                getImageFromGallery();
                            }
                        }
                    });
            builder.create().show();
        } else if (id == mNickNameLayout.getId()) {
            if (mNickNameEt == null) {
                mNickNameEt = new EditTextLayout(getActivity());
                mNickNameEt.setSingleLine();
                mNickNameEt.setContentGravity(Gravity.RIGHT);
                mNickNameEt.setOnOkListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence newName = mNickNameEt.getText();
                        if (TextUtils.isEmpty(newName)) {
                            Toast.makeText(getActivity(), R.string.toast_user_name_empty, Toast.LENGTH_SHORT).show();
                            return;
                        } else if (newName.length() > 25) {
                            Toast.makeText(getActivity(), getString(R.string.toast_user_name_to_long, 25), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mNickNameLayout.removeValueView(mNickNameEt);
                        mNickNameLayout.setValue(mNickNameEt.getText());
                        mNickNameLayout.setEditable(isEditable);
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("userName", newName.toString());
                        AccountManager.getInstance(getActivity()).updateUserInfoPost(map, new RequestCallback() {
                            @Override
                            public void callback(ReturnInfo returnInfo, String callbackId) {
                                showTipToast(returnInfo);
                            }
                        });
                    }
                });
            }
            if (mNickNameLayout.hasValueView(mNickNameEt)) {
                mNickNameEt.requestFocus();
                /*mNickNameLayout.removeValueView(mNickNameEt);
                mNickNameLayout.setValue(mNickNameEt.getText());*/
            } else {
                mNickNameEt.setText(mNickNameLayout.getValue());
                mNickNameLayout.setValue("");
                mNickNameLayout.setEditable(false);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.RIGHT;
                mNickNameEt.setLayoutParams(params);
                mNickNameLayout.addValueView(mNickNameEt);
                mNickNameEt.requestFocus();
            }
        } else if (id == mGenderLayout.getId()) {
            UserManager.getInstance(getContext()).getUserInfo(mUser.userId, new RequestCallback<User>() {
                @Override
                public void callback(ReturnInfo<User> returnInfo, String callbackId) {

                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final String[] items = new String[]{getString(R.string.text_gender_male), getString(R.string.text_gender_female)};
            int index = -1;
            for (int i = 0; i < items.length; i++) {
                if (items[i].equals(mGenderLayout.getValue())) {
                    index = i;
                    break;
                }
            }
            builder.setSingleChoiceItems(items, index, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mGenderLayout.setValue(items[which]);
                    mUser.setGender(items[which]);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("gender", items[which]);
                    AccountManager.getInstance(getContext()).updateUserInfoPost(map, new RequestCallback() {
                        @Override
                        public void callback(ReturnInfo returnInfo, String callbackId) {
                            showTipToast(returnInfo);
                        }
                    });
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else if (id == mProvinceLayout.getId()) {
            UserManager.getInstance(getContext()).getProvince(new RequestCallback<List<SimpleProvince>>() {
                @Override
                public void callback(ReturnInfo<List<SimpleProvince>> returnInfo, String callbackId) {
                    if (returnInfo.isSuccess()) {
                        mSimpleProvinceList = returnInfo.getData();
                        final int length = mSimpleProvinceList.size();
                        CharSequence[] items = new CharSequence[length];
                        int index = -1;
                        for (int i = 0; i < length; i++) {
                            SimpleProvince province = mSimpleProvinceList.get(i);
                            items[i] = province.name;
                            if (province.name.equals(mProvinceLayout.getValue())) {
                                index = i;
                            }
                        }
                        //ArrayAdapter<SimpleProvince> adapter = new ArrayAdapter<SimpleProvince>(getContext(), android.R.layout.select_dialog_item, android.R.id.text1, mSimpleProvinceList);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setSingleChoiceItems(items, index, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mProvinceLayout.setValue(mSimpleProvinceList.get(which).name);
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("province", mSimpleProvinceList.get(which).provinceId + "");
                                AccountManager.getInstance(getContext()).updateUserInfoPost(map, new RequestCallback() {
                                    @Override
                                    public void callback(ReturnInfo returnInfo, String callbackId) {
                                        showTipToast(returnInfo);
                                    }
                                });
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                }
            });
            /*if (mSimpleProvinceList == null) {

            } else {

            }*/

            /*AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setAdapter()*/
        } else if (id == mQuitView.getId()) {
            AccountManager.getInstance(getContext()).logout();
            //Toast.makeText(getContext(), "logout onComplete", Toast.LENGTH_SHORT).show();
            ActivityDispatcher.loginActivity(getContext());
            getActivity().finish();
        }
    }

    public void getImageFromGallery () {
        Intent it = new Intent(Intent.ACTION_GET_CONTENT);
        it.setType("image/*");
        it.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(it, 2, this);
    }

    public void getImageFromCamera (Uri at) {
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, at);
        startActivityForResult(it, 3, this);
    }

    @Override
    public void onActivityResultReceived(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResultReceived requestCode=" + requestCode + " resultCode=" + resultCode + " data=" + data);
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                String outPath = CacheManager.getInstance(getActivity()).getMyImageCacheDir().getAbsolutePath() + File.separator + "cropped.jpg";
                mCroppedFile = new File(outPath);
                Uri dataUri = data.getData();
                if (!mCroppedFile.getParentFile().exists()) {
                    mCroppedFile.getParentFile().mkdirs();
                }
                Log.v(TAG, "onActivityResultReceived " + outPath);

                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).setActivityResultListener(this);
                }
                Crop.of(dataUri, Uri.fromFile(mCroppedFile)).asSquare().start(getActivity());
            }
        } else if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                mCroppedFile = new File(CacheManager.getInstance(getActivity()).getMyImageCacheDir().getAbsolutePath() + File.separator + mCameraFile.getName());
                if (!mCroppedFile.getParentFile().exists()) {
                    mCroppedFile.getParentFile().mkdirs();
                }
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).setActivityResultListener(this);
                }
                Crop.of(Uri.fromFile(mCameraFile), Uri.fromFile(mCroppedFile)).asSquare().start(getActivity());
               /* Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                FileUtils.saveBitmap(bitmap, outPath, new SavingTask.Callback() {
                    @Override
                    public void onSuccess(List<String> pathList) {
                        Uri uri = Uri.fromFile(outFile);
                        Crop.of(uri, uri).asSquare().start(getActivity());
                    }

                    @Override
                    public void onProgress(int progress, int total) {

                    }

                    @Override
                    public void onException(Exception e) {

                    }
                });
                Log.v(TAG, "onActivityResultReceived bitmap=" + bitmap);*/
            }
        } else if (requestCode == Crop.REQUEST_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                if (mProfileIv != null) {
                    Uri uri = Crop.getOutput(data);
                    DisplayManager.getInstance(getActivity()).displayProfile(uri.toString(), mProfileIv);
                    final AccountManager accountManager = AccountManager.getInstance(getActivity());
                    UploadManager.getInstance(getActivity()).uploadImage(mCroppedFile, accountManager.getCookies(), new RequestCallback<List<Thumbnail>>() {
                        @Override
                        public void callback(ReturnInfo<List<Thumbnail>> returnInfo, String callbackId) {
                            if (returnInfo.isSuccess()) {
                                Thumbnail thumbnail = returnInfo.getData().get(0);
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("userAvatar", thumbnail.getOriginalImage());
                                accountManager.updateUserInfoPost(map, new RequestCallback() {
                                    @Override
                                    public void callback(ReturnInfo returnInfo, String callbackId) {
                                        showTipToast(returnInfo);
                                    }
                                });
                            }
                        }
                    });
                    /*AccountManager.getInstance(getActivity()).updateUserInfoPost();
                    AccountManager*/
                }
            }
        }
    }

    private void showTipToast (ReturnInfo returnInfo) {
        if (!isAlive()) {
            return;
        }
        String msg = returnInfo.isSuccess() ? getString(R.string.toast_update_success) : getString(R.string.toast_update_failed, returnInfo.getMessage());
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

}
