package com.metis.meishuquan.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.beak.courselib.fragment.CourseTabFragment;
import com.metis.base.widget.dock.DockBar;
import com.metis.meishuquan.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements DockBar.OnDockItemClickListener{

    private CourseTabFragment mCourseFragment = CourseTabFragment.getInstance();

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
    }

    @Override
    public void onDockClick(View view, DockBar.Dock dock) {

    }

    private void showFragment (Fragment fragment) {
        if (fragment == null) {
            return;
        }
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
