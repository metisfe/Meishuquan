package com.metis.base.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.base.ActivityDispatcher;
import com.metis.base.R;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.User;
import com.metis.base.utils.Log;
import com.metis.base.widget.adapter.delegate.RoleDelegate;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.HashMap;
import java.util.Map;

public class RoleChooseActivity extends TitleBarActivity {

    private static final String TAG = RoleChooseActivity.class.getSimpleName();

    private AbsoluteLayout mRoleLayout = null;
    private StarDrawable mDrawable;

    private View mSelectedView = null;
    private RoleDelegate mSelectedRole = null;

    private User mUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_choose);

        mRoleLayout = (AbsoluteLayout)findViewById(R.id.role_container);
        final int length = getResources().getDimensionPixelSize(R.dimen.role_choose_length);
        mDrawable = new StarDrawable(length);
        mRoleLayout.setBackground(mDrawable);

        mUser = (User)getIntent().getSerializableExtra(ActivityDispatcher.KEY_USER);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        showProgressDialog(R.string.text_please_wait, true);
        mRoleLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog();
                Point[] points = mDrawable.getPoints();
                final int size = points.length;
                final int profileSize = getResources().getDimensionPixelSize(R.dimen.role_profile_size);
                LayoutInflater inflater = LayoutInflater.from(RoleChooseActivity.this);
                mRoleLayout.removeAllViews();
                for (int i = 0; i < size; i++) {
                    final View view = inflater.inflate(R.layout.layout_role, null);
                    ImageView iv = (ImageView) view.findViewById(R.id.role_profile);
                    TextView tv = (TextView) view.findViewById(R.id.role_name);
                    final RoleDelegate roleDelegate = RoleDelegate.values()[i];
                    iv.setImageResource(roleDelegate.getDrawableId());
                    tv.setText(roleDelegate.getNameId());
                    Point p = points[i];
                    Log.v(TAG, "p.x=" + p.x + " p.y=" + p.y);
                    AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, p.x - profileSize / 2, p.y - profileSize / 2);
                    mRoleLayout.addView(view, params);

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mSelectedView != null) {
                                mSelectedView.setSelected(false);
                            }
                            view.setSelected(true);
                            mSelectedRole = roleDelegate;
                            mSelectedView = view;
                        }
                    });
                }
                getTitleBar().setOnRightBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mSelectedRole == null) {
                            Toast.makeText(RoleChooseActivity.this, R.string.toast_role_at_least_choose_one, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (mUser != null) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("userRole", mSelectedRole.getRoleId() + "");
                            AccountManager.getInstance(RoleChooseActivity.this).updateUserInfo(map, new RequestCallback() {
                                @Override
                                public void callback(ReturnInfo returnInfo, String callbackId) {
                                    if (returnInfo.isSuccess()) {
                                        ActivityDispatcher.mainActivity(RoleChooseActivity.this);
                                    }
                                }
                            });
                        }
                        //TODO
                    }
                });
            }
        }, 100);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    public CharSequence getTitleRight() {
        return getString(R.string.text_go_on);
    }

    private class StarDrawable extends Drawable {

        private final String TAG = StarDrawable.class.getSimpleName();

        private int mRadius = 0;

        private Point[] mPoints = new Point[5];

        private Path mPath = null;

        private Paint mPaint = null;

        public StarDrawable(int radius) {
            if (radius <= 0) {
                throw new IllegalArgumentException("radius can not <= 0");
            }
            mRadius = radius;
            mPoints[0] = new Point();
            mPoints[1] = new Point();
            mPoints[2] = new Point();
            mPoints[3] = new Point();
            mPoints[4] = new Point();


            mPath = new Path();

            mPaint = new Paint();
            mPaint.setColor(Color.WHITE);
            mPaint.setStrokeWidth(2);
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        }

        @Override
        public void draw(Canvas canvas) {
            final int width = canvas.getWidth();
            final int height = canvas.getHeight();
            final int centerX = width / 2;
            final int centerY = height / 2;

            mPoints[0].x = centerX;
            mPoints[0].y = centerY - mRadius;

            mPoints[1].x = centerX + (int)(mRadius * Math.cos(Math.PI / 10));
            mPoints[1].y = centerY - (int)(mRadius * Math.sin(Math.PI / 10));

            mPoints[2].x = centerX + (int)(mRadius * Math.sin(Math.PI / 5));
            mPoints[2].y = centerY + (int)(mRadius * Math.cos(Math.PI / 5));

            mPoints[3].x = centerX - (int)(mRadius * Math.sin(Math.PI / 5));
            mPoints[3].y = centerY + (int)(mRadius * Math.cos(Math.PI / 5));

            mPoints[4].x = centerX - (int)(mRadius * Math.cos(Math.PI / 10));
            mPoints[4].y = centerY - (int)(mRadius * Math.sin(Math.PI / 10));

            mPath.moveTo(mPoints[0].x, mPoints[0].y);
            mPath.lineTo(mPoints[1].x, mPoints[1].y);
            mPath.lineTo(mPoints[2].x, mPoints[2].y);
            mPath.lineTo(mPoints[3].x, mPoints[3].y);
            mPath.lineTo(mPoints[4].x, mPoints[4].y);
            mPath.close();

            canvas.drawPath(mPath, mPaint);

            Log.v(TAG, "p.x canvasDraw");
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter cf) {

        }

        @Override
        public int getOpacity() {
            return 0;
        }

        public Point[] getPoints() {
            return mPoints;
        }
    }

}
