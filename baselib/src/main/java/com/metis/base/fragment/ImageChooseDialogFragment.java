package com.metis.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.metis.base.R;

/**
 * Created by Beak on 2015/7/29.
 */
public class ImageChooseDialogFragment extends DialogFragment implements View.OnClickListener{

    private TextView mGalleryBtn, mCameraBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_image_choose, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("选择图片");

        mGalleryBtn = (TextView)view.findViewById(R.id.image_choose_gallery);
        mCameraBtn = (TextView)view.findViewById(R.id.image_choose_camera);

        mGalleryBtn.setOnClickListener(this);
        mCameraBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == mGalleryBtn.getId()) {

        } else if (id == mCameraBtn.getId()) {

        }
    }
}
