package com.metis.coursepart.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.metis.base.manager.DownloadManager;
import com.metis.coursepart.R;

/**
 * Created by Beak on 2015/7/6.
 */
public class CourseGalleryFragment extends Fragment {

    private static CourseGalleryFragment sFragment = null;

    public static CourseGalleryFragment getInstance () {
        if (sFragment == null) {
            sFragment = new CourseGalleryFragment();
        }
        return sFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_gallery, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView iv = (ImageView)view.findViewById(R.id.test);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager.getInstance(getActivity())
                        .addTask(new DownloadManager.Task(
                                "ABC.MP4",
                                "https://metisfile.blob.core.chinacloudapi.cn/asset-932c435d-1500-80c3-edd0-f1e51bce6acc/201506260241372669.mp4?sv=2012-02-12&sr=c&si=213f6a65-d37c-40a2-9f96-d224c92659b7&sig=cBulNhcFiBx8Dfb99fUgBWoKKodrKd1mQd9ft0eyUnY%3D&se=2016-06-25T06%3A41%3A50Z"));
            }
        });
    }
}
