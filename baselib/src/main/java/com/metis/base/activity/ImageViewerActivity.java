package com.metis.base.activity;

import android.os.Parcelable;
import android.os.Bundle;
import android.view.View;

import com.metis.base.ActivityDispatcher;
import com.metis.base.R;
import com.metis.base.fragment.MultiImagePreviewFragment;
import com.metis.base.widget.ImagePreviewable;

import java.util.ArrayList;
import java.util.List;

public class ImageViewerActivity extends TitleBarActivity {

    private MultiImagePreviewFragment mPreviewFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        mPreviewFragment = (MultiImagePreviewFragment)getSupportFragmentManager().findFragmentById(R.id.preview_fragment);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Parcelable[] parcelables = getIntent().getParcelableArrayExtra(ActivityDispatcher.KEY_IMAGES);
        final int length = parcelables.length;
        List<ImagePreviewable> imagePreviewables = new ArrayList<ImagePreviewable>();
        for (int i = 0; i < length; i++) {
            Parcelable parcelable = parcelables[i];
            if (parcelable instanceof ImagePreviewable) {
                imagePreviewables.add((ImagePreviewable)parcelable);
            }
        }
        mPreviewFragment.setImages(imagePreviewables);
    }

    @Override
    public int getTitleVisibility() {
        return View.GONE;
    }
}
