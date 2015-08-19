package com.metis.base.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.metis.base.ActivityDispatcher;
import com.metis.base.R;

import java.io.File;

/**
 * Created by Beak on 2015/7/29.
 */
public class ImageChooseDialogFragment extends DialogFragment implements View.OnClickListener{

    public static final int
            REQUEST_CODE_GET_IMAGE_GALLERY = 10010,
            REQUEST_CODE_GET_IMAGE_CAMERA = 10086;

    private View mGalleryBtn, mCameraBtn;

    private String mPath = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_image_choose, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle(R.string.image_choose_title);

        mGalleryBtn = view.findViewById(R.id.image_choose_gallery);
        mCameraBtn = view.findViewById(R.id.image_choose_camera);

        mGalleryBtn.setOnClickListener(this);
        mCameraBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == mGalleryBtn.getId()) {
            ActivityDispatcher.getImage(getActivity(), REQUEST_CODE_GET_IMAGE_GALLERY);
        } else if (id == mCameraBtn.getId()) {
            mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
            ActivityDispatcher.captureImage(getActivity(), REQUEST_CODE_GET_IMAGE_CAMERA, mPath);
        }
        dismiss();
    }

    public String getLastCapturePath () {
        return mPath;
    }
}
