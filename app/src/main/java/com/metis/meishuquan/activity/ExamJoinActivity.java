package com.metis.meishuquan.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metis.base.ActivityDispatcher;
import com.metis.base.activity.TitleBarActivity;
import com.metis.base.manager.DisplayManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.Footer;
import com.metis.base.utils.Log;
import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.base.widget.adapter.delegate.DelegateType;
import com.metis.base.widget.adapter.delegate.FooterDelegate;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.base.widget.adapter.holder.FooterHolder;
import com.metis.base.widget.callback.OnScrollBottomListener;
import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.DiscoveryDelegateType;
import com.metis.meishuquan.manager.DiscoveryManager;
import com.metis.meishuquan.module.Area;
import com.metis.meishuquan.module.College;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.ArrayList;
import java.util.List;

public class ExamJoinActivity extends TitleBarActivity {

    private static final String TAG = ExamJoinActivity.class.getSimpleName();

    private RecyclerView mAreaRv, mCollegeRv;
    private AreaAdapter mAreaAdapter;
    private CollegeAdapter mCollegeAdapter;

    private List<Area> mAreaList = new ArrayList<Area>();

    private View mSearchView = null;

    private String mRequest = null;

    private int mIndex = 0;
    private boolean noMore = false;
    private boolean isLoading = false;

    private Footer mFooter = new Footer();
    private FooterDelegate mFooterDelegate = null;

    private List<Area> mAreasFromServer = null;
    Area mAll = new Area();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_join);

        mAreaRv = (RecyclerView)findViewById(R.id.exam_join_area);
        mCollegeRv = (RecyclerView)findViewById(R.id.exam_join_college);

        mAreaRv.addItemDecoration(new AreaDecoration());
        mCollegeRv.addItemDecoration(new CollegeDecoration());

        mAreaRv.setLayoutManager(new LinearLayoutManager(this));
        mCollegeRv.setLayoutManager(new LinearLayoutManager(this));

        mAll.id = 0;
        mAll.name = getString(R.string.filter_all);
        mAreaList.add(mAll);
        mAreaAdapter = new AreaAdapter(mAreaList);
        mCollegeAdapter = new CollegeAdapter(this);

        mAreaRv.setAdapter(mAreaAdapter);
        mCollegeRv.setAdapter(mCollegeAdapter);

        mCollegeRv.addOnScrollListener(new OnScrollBottomListener() {
            @Override
            public void onScrollBottom(RecyclerView recyclerView, int newState) {
                if (!isLoading && !noMore) {
                    Area area = mAreaAdapter.getCurrentArea();
                    if (area != null) {
                        loadCollege(area.id);
                    }
                }
            }
        });

        mSearchView = LayoutInflater.from(this).inflate(R.layout.layout_exam_join_search, null);
        getTitleBar().setCenterView(mSearchView);
        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExamJoinActivity.this, ExamJoinSearchActivity.class));
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mFooterDelegate = new FooterDelegate(mFooter);

        showProgressDialog(R.string.text_please_wait, true);
        DiscoveryManager.getInstance(this).getAreaList(0, new RequestCallback<List<Area>>() {
            @Override
            public void callback(ReturnInfo<List<Area>> returnInfo, String callbackId) {
                dismissProgressDialog();
                if (returnInfo != null && returnInfo.isSuccess()) {
                    if (mAreasFromServer != null && mAreaAdapter.getCurrentArea() == null) {
                        mAreaList.removeAll(mAreasFromServer);
                    }
                    mAreasFromServer = returnInfo.getData();
                    mAreaList.addAll(mAreasFromServer);
                    mAreaAdapter.notifyDataSetChanged();
                    if (mAreaList.size() > 0) {
                        mAreaAdapter.setCurrent(0);
                        loadCollege(mAreaList.get(0).id);
                        mAreaAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v(TAG, "onSaveInstanceState");
        if (mAreasFromServer != null) {
            String current = new Gson().toJson(mAreasFromServer);
            Log.v(TAG, "onSaveInstanceState current=" + current);
            outState.putString("areas", current);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String current = savedInstanceState.getString("areas");
        Log.v(TAG, "onRestoreInstanceState current=" + current);
        if (!TextUtils.isEmpty(current)) {
            mAreasFromServer = new Gson().fromJson(current, new TypeToken<List<List<Area>>>(){}.getType());
            if (mAreaList.size() == 1 && mAreaList.contains(mAll)) {
                mAreaList.addAll(mAreasFromServer);
                mAreaAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    private void loadCollege (final long collegeId) {
        isLoading = true;
        mFooter.setState(Footer.STATE_WAITTING);
        if (!mCollegeAdapter.endWith(mFooterDelegate)) {
            mCollegeAdapter.addDataItem(mFooterDelegate);
        }
        mCollegeAdapter.notifyDataSetChanged();
        mRequest = DiscoveryManager.getInstance(ExamJoinActivity.this).getCollegeList(collegeId, mIndex, "", new RequestCallback<List<College>>() {
            @Override
            public void callback(ReturnInfo<List<College>> returnInfo, String callbackId) {
                isLoading = false;
                if (!callbackId.equals(mRequest)) {
                    return;
                }
                if (returnInfo != null && returnInfo.isSuccess()) {
                    mIndex++;
                    List<BaseDelegate> delegates = new ArrayList<BaseDelegate>();
                    List<College> colleges = returnInfo.getData();
                    final int length = colleges.size();
                    if (length == 0) {
                        noMore = true;
                        mFooter.setState(Footer.STATE_NO_MORE);
                        if (!mCollegeAdapter.endWith(mFooterDelegate)) {
                            mCollegeAdapter.addDataItem(mFooterDelegate);
                        }
                    } else {
                        for (int i = 0; i < length; i++) {
                            delegates.add(new CollegeDelegate(colleges.get(i)));
                        }
                        mFooter.setState(Footer.STATE_SUCCESS);
                        if (mCollegeAdapter.endWith(mFooterDelegate)) {
                            mCollegeAdapter.addDataList(mCollegeAdapter.getItemCount() - 1, delegates);
                        } else {
                            mCollegeAdapter.addDataList(delegates);
                            mCollegeAdapter.addDataItem(mFooterDelegate);
                        }
                    }
                } else {
                    mFooter.setState(Footer.STATE_FAILED);
                    if (mCollegeAdapter.endWith(mFooterDelegate)) {
                    } else {
                        mCollegeAdapter.addDataItem(mFooterDelegate);
                    }
                }
                mCollegeAdapter.notifyDataSetChanged();
            }
        });
    }

    private class AreaHolder extends RecyclerView.ViewHolder {

        public TextView nameTv = null;

        public AreaHolder(View itemView) {
            super(itemView);
            nameTv = (TextView)itemView.findViewById(R.id.area_name);
        }
    }

    private class AreaAdapter extends RecyclerView.Adapter<AreaHolder> {

        private List<Area> mAreaList = null;
        private int mCurrent = -1;

        public AreaAdapter (List<Area> collegeList) {
            mAreaList = collegeList;
        }

        @Override
        public AreaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AreaHolder(LayoutInflater.from(ExamJoinActivity.this).inflate(R.layout.layout_exam_area_item, null));
        }

        @Override
        public void onBindViewHolder(final AreaHolder holder, final int position) {
            final Area college = mAreaList.get(position);
            holder.nameTv.setText(college.name);
            holder.itemView.setSelected(mCurrent == position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCurrent == position) {
                        return;
                    }
                    mCurrent = position;
                    mAreaAdapter.notifyDataSetChanged();
                    mCollegeAdapter.clearDataList();
                    mCollegeAdapter.notifyDataSetChanged();

                    mIndex = 0;
                    noMore = false;
                    loadCollege(college.id);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mAreaList.size();
        }

        public Area getCurrentArea () {
            if (mAreaList == null || mCurrent < 0 || mCurrent >= mAreaList.size()) {
                return null;
            }
            return mAreaList.get(mCurrent);
        }

        public void setCurrent (int current) {
            mCurrent = current;
        }
    }

    public static class CollegeDelegate extends BaseDelegate<College> {

        public CollegeDelegate(College college) {
            super(college);
        }

        @Override
        public int getDelegateType() {
            return DiscoveryDelegateType.TYPE_DISCOVERY_COLLEGE.getType();
        }
    }

    public static class CollegeHolder extends AbsViewHolder<CollegeDelegate> {

        public ImageView profileIv;
        public TextView focusTv, nameTv, extrasTv;

        public CollegeHolder(View itemView) {
            super(itemView);

            profileIv = (ImageView)itemView.findViewById(R.id.college_profile);
            focusTv = (TextView)itemView.findViewById(R.id.college_focus);
            nameTv = (TextView)itemView.findViewById(R.id.college_name);
            extrasTv = (TextView)itemView.findViewById(R.id.college_extras);
        }

        @Override
        public void bindData(final Context context, CollegeDelegate coolegeDelegate, RecyclerView.Adapter adapter, int position) {
            final College college = coolegeDelegate.getSource();
            DisplayManager.getInstance(context).display(
                    college.avatar, profileIv,
                    DisplayManager.getInstance(context).makeRoundDisplayImageOptions(
                            context.getResources().getDimensionPixelSize(R.dimen.profile_size_middle)
                    ));
            nameTv.setText(college.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDispatcher.innerBrowserActivity(context, "http://www.meishuquan.net/mobile/institutions/index.aspx?id=" + college.id, true);
                }
            });
        }
    }

    public static class CollegeAdapter extends DelegateAdapter {

        public CollegeAdapter(Context context) {
            super(context);
        }

        @Override
        public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View view) {
            switch (viewType) {
                case DiscoveryDelegateType.ID.ID_DISCOVERY_COLLEGE:
                    return new CollegeHolder(view);
                case DelegateType.ID.ID_FOOTER:
                    return new FooterHolder(view);
            }
            return null;
        }

    }

    private class AreaDecoration extends RecyclerView.ItemDecoration {

        private Paint mPaint = null;

        public AreaDecoration () {
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(Color.WHITE);
            mPaint.setAntiAlias(true);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
            final int count = parent.getChildCount();
            for (int i = 0; i < count - 1; i++) {
                View child = parent.getChildAt(i);
                c.drawLine(child.getLeft(), child.getBottom(), child.getRight(), child.getBottom(), mPaint);
            }
        }
    }
    public class CollegeDecoration extends RecyclerView.ItemDecoration {

        private Paint mPaint = null;

        public CollegeDecoration () {
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(Color.DKGRAY);
            mPaint.setAntiAlias(true);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
            final int count = parent.getChildCount();
            for (int i = 1; i < count - 1; i++) {
                View child = parent.getChildAt(i);
                c.drawLine(child.getLeft(), child.getTop(), child.getRight(), child.getTop(), mPaint);
                /*final int position = parent.getChildAdapterPosition(child);
                RecyclerView.Adapter adapter = parent.getAdapter();
                if (adapter instanceof CollegeAdapter) {
                    if (((CollegeAdapter) adapter).getDataItem(position) instanceof CollegeDelegate) {
                        c.drawLine(child.getLeft(), child.getBottom(), child.getRight(), child.getBottom(), mPaint);
                    }
                }*/
            }
        }
    }
}
