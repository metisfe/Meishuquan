package com.metis.meishuquan.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.metis.base.widget.dock.DockBar;
import com.metis.coursepart.fragment.CourseTabFragment;
import com.metis.meishuquan.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements DockBar.OnDockItemClickListener{

    private CourseTabFragment mCourseFragment = new CourseTabFragment();

    private Fragment mCurrentFragment = null;

    @InjectView(R.id.main_dock_bar)
    DockBar mMainDockBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mMainDockBar.addDock(mCourseFragment.getDock(this));

        mMainDockBar.setOnDockItemClickListener(this);
        mMainDockBar.selectDock(mCourseFragment.getDock(this));
    }

    @Override
    public void onDockClick(View view, DockBar.Dock dock) {
        hideFragment(mCurrentFragment);
        showFragment(dock.target);
    }

    private void showFragment (Fragment fragment) {
        if (fragment == null) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment.isAdded()) {
            ft.show(fragment);
        } else {
            ft.add(R.id.main_fragment_container, fragment);
        }
        ft.commit();
        mCurrentFragment = fragment;
    }

    private void hideFragment (Fragment fragment) {
        if (fragment == null) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(fragment);
        ft.commit();
        mCurrentFragment = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
