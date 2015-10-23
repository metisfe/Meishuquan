package com.metis.meishuquan.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.metis.base.ActivityDispatcher;
import com.metis.base.activity.TitleBarActivity;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.manager.WeChatPayManager;
import com.metis.base.module.SimpleProvince;
import com.metis.base.module.User;
import com.metis.meishuquan.R;
import com.metis.meishuquan.manager.DiscoveryManager;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.List;

public class DiscoveryExamActivity extends TitleBarActivity {

    private RecyclerView mExamRv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_exam);

        mExamRv = (RecyclerView)findViewById(R.id.exam_recycler_view);
        mExamRv.setLayoutManager(new LinearLayoutManager(this));
        mExamRv.addItemDecoration(new ExamDecoration());
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        /*UserManager.getInstance(this).getProvince(new RequestCallback<List<SimpleProvince>>() {
            @Override
            public void callback(ReturnInfo<List<SimpleProvince>> returnInfo, String callbackId) {
                if (returnInfo.isSuccess()) {
                    mExamRv.setAdapter(new ExamAdapter(DiscoveryExamActivity.this, returnInfo.getData()));
                } else {

                }
            }
        });*/

        DiscoveryManager.getInstance(this).getAvailableAreaList(new RequestCallback<List<SimpleProvince>>() {
            @Override
            public void callback(ReturnInfo<List<SimpleProvince>> returnInfo, String callbackId) {
                if (returnInfo.isSuccess()) {
                    mExamRv.setAdapter(new ExamAdapter(DiscoveryExamActivity.this, returnInfo.getData()));
                } else {

                }
            }
        });

        WeChatPayManager.getInstance(this).registerToWechat();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        WeChatPayManager.getInstance(this).unregisterFromWechat();
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    public CharSequence getTitleCenter() {
        return getString(R.string.text_exam);
    }

    private class ExamHolder extends RecyclerView.ViewHolder {

        public TextView areaTv;
        public FrameLayout notifyTv, joinTv, getScoreTv, highScoresTv;

        public ExamHolder(View itemView) {
            super(itemView);
            areaTv = (TextView)itemView.findViewById(R.id.exam_area);
            notifyTv = (FrameLayout)itemView.findViewById(R.id.exam_notify);
            joinTv = (FrameLayout)itemView.findViewById(R.id.exam_join);
            getScoreTv = (FrameLayout)itemView.findViewById(R.id.exam_get_score);
            highScoresTv = (FrameLayout)itemView.findViewById(R.id.exam_high_scores);

        }
    }

    private class ExamAdapter extends RecyclerView.Adapter<ExamHolder> {

        private Context mContext = null;
        private List<SimpleProvince> mProvinceList = null;

        public ExamAdapter (Context context, List<SimpleProvince> list) {
            mContext = context;
            mProvinceList = list;
        }

        @Override
        public ExamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ExamHolder(LayoutInflater.from(DiscoveryExamActivity.this).inflate(R.layout.layout_exam_item, null));
        }

        @Override
        public void onBindViewHolder(ExamHolder holder, int position) {
            final SimpleProvince province = mProvinceList.get(position);
            holder.areaTv.setText(province.name);

            holder.joinTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User me = AccountManager.getInstance(DiscoveryExamActivity.this).getMe();
                    if (me != null) {
                        ActivityDispatcher.innerBrowserActivity(
                                DiscoveryExamActivity.this,
                                "http://www.meishuquan.net/mobile/mockexams/signup.aspx?aid=" + province.provinceId + "&uid=" + me.userId);
                    }
                }
            });
            holder.notifyTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User me = AccountManager.getInstance(DiscoveryExamActivity.this).getMe();
                    if (me != null) {
                        ActivityDispatcher.innerBrowserActivity(
                                DiscoveryExamActivity.this,
                                "http://www.meishuquan.net/mobile/mockexams/notificationcenter.aspx?uid=" + me.userId);
                    }
                }
            });
            holder.highScoresTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                }
            });
            holder.getScoreTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                }
            });
        }

        @Override
        public int getItemCount() {
            return mProvinceList.size();
        }
    }

    private class ExamDecoration extends RecyclerView.ItemDecoration {
        private Paint mPaint = new Paint();
        public ExamDecoration () {
            mPaint.setAntiAlias(true);
            mPaint.setColor(getResources().getColor(android.R.color.darker_gray));
            mPaint.setStyle(Paint.Style.STROKE);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
            final int count = parent.getChildCount();
            for (int i = 0; i < count - 1; i++) {
                View child = parent.getChildAt(i);
                c.drawLine(0, child.getBottom(), child.getRight(), child.getBottom(), mPaint);
            }
        }
    }
}
