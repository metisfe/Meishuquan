package com.metis.coursepart.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.RadioGroup;

import com.metis.base.TitleBarActivity;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.AlbumAdapter;
import com.metis.coursepart.fragment.FilterFragment;

public class FilterActivity extends TitleBarActivity {

    private FilterFragment mFilterFragment = null;
    private RecyclerView mDataRecyclerView = null;

    private AlbumAdapter mAlbumAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        RadioGroup switchView = (RadioGroup) LayoutInflater.from(this).inflate(R.layout.layout_tab_switch, null);
        getTitleBar().setCenterView(switchView);

        mFilterFragment = (FilterFragment)getSupportFragmentManager().findFragmentById(R.id.filter_fragment);
        mDataRecyclerView = (RecyclerView)findViewById(R.id.filter_data_recycler_view);
        mDataRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAlbumAdapter = new AlbumAdapter(this);
        mDataRecyclerView.setAdapter(mAlbumAdapter);

    }

}
