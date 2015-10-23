package com.metis.meishuquan.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.metis.base.activity.TitleBarActivity;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.Footer;
import com.metis.base.utils.SystemUtils;
import com.metis.base.widget.adapter.delegate.FooterDelegate;
import com.metis.base.widget.callback.OnScrollBottomListener;
import com.metis.meishuquan.R;
import com.metis.meishuquan.manager.DiscoveryManager;
import com.metis.meishuquan.module.College;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.ArrayList;
import java.util.List;

public class ExamJoinSearchActivity extends TitleBarActivity {

    private View mSearchView = null;
    private EditText mSearchContent = null;
    private ImageView mCloseBtn = null;

    private RecyclerView mResultRv = null;
    private ExamJoinActivity.CollegeAdapter mCollegeAdapter = null;

    private int mPage = 0;

    private Footer mFooter = new Footer();
    private FooterDelegate mFooterDelegate =  new FooterDelegate(mFooter);

    private boolean isLoading = false, noMore = false;
    private String mRequestId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_join_search);

        mResultRv = (RecyclerView)findViewById(R.id.exam_join_search_result_recycler_view);
        mResultRv.setLayoutManager(new LinearLayoutManager(this));

        mCollegeAdapter = new ExamJoinActivity.CollegeAdapter(this);
        mResultRv.setAdapter(mCollegeAdapter);

        mResultRv.addOnScrollListener(new OnScrollBottomListener() {
            @Override
            public void onScrollBottom(RecyclerView recyclerView, int newState) {
                String name = mSearchContent.getText().toString();
                if (!isLoading && !noMore && !TextUtils.isEmpty(name)) {
                    loadCollege(name);
                }
            }
        });

        mSearchView = LayoutInflater.from(this).inflate(R.layout.layout_exam_join_search_editable, null);
        getTitleBar().setCenterView(mSearchView);

        mSearchContent = (EditText)mSearchView.findViewById(R.id.search_content);
        mCloseBtn = (ImageView)mSearchView.findViewById(R.id.search_close_flag);
        mSearchContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String name = mSearchContent.getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(ExamJoinSearchActivity.this, R.string.toast_empty_search_content, Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    loadCollege(name);
                    return true;
                }
                return false;
            }
        });
        mSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPage = 0;
                if (mCollegeAdapter.getItemCount() > 0) {
                    mCollegeAdapter.clearDataList();
                    mCollegeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchContent.requestFocus();
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchContent.setText("");
            }
        });

        SystemUtils.showIME(this, mSearchView);
    }

    private void loadCollege (String name) {
        if (mPage == 0 && mCollegeAdapter.getItemCount() > 0) {
            mCollegeAdapter.clearDataList();
            mCollegeAdapter.notifyDataSetChanged();
        }
        mFooter.setState(Footer.STATE_WAITTING);
        if (!mCollegeAdapter.endWith(mFooterDelegate)) {
            mCollegeAdapter.addDataItem(mFooterDelegate);
        }
        mCollegeAdapter.notifyDataSetChanged();
        mRequestId = DiscoveryManager.getInstance(this).getCollegeList(0, mPage, name, new RequestCallback<List<College>>() {
            @Override
            public void callback(ReturnInfo<List<College>> returnInfo, String callbackId) {
                if (!mRequestId.equals(callbackId)) {
                    return;
                }
                if (returnInfo != null && returnInfo.isSuccess()) {
                    mPage++;
                    List<ExamJoinActivity.CollegeDelegate> delegates = new ArrayList<ExamJoinActivity.CollegeDelegate>();
                    List<College> colleges = returnInfo.getData();
                    final int length = colleges.size();
                    if (length > 0) {
                        for (int i = 0; i < length; i++) {
                            delegates.add(new ExamJoinActivity.CollegeDelegate(colleges.get(i)));
                        }
                        mFooter.setState(Footer.STATE_SUCCESS);
                        if (mCollegeAdapter.endWith(mFooterDelegate)) {
                            mCollegeAdapter.addDataList(mCollegeAdapter.getItemCount() - 1, delegates);
                        } else {
                            mCollegeAdapter.addDataList(delegates);
                            mCollegeAdapter.addDataItem(mFooterDelegate);
                        }
                    } else {
                        mFooter.setState(Footer.STATE_NO_MORE);
                        if (!mCollegeAdapter.endWith(mFooterDelegate)) {
                            mCollegeAdapter.addDataItem(mFooterDelegate);
                        }
                    }
                } else {
                    mFooter.setState(Footer.STATE_FAILED);
                    if (!mCollegeAdapter.endWith(mFooterDelegate)) {
                        mCollegeAdapter.addDataItem(mFooterDelegate);
                    }
                }
                mCollegeAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }
}
