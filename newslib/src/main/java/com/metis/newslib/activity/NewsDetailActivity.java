package com.metis.newslib.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.base.activity.TitleBarActivity;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.Footer;
import com.metis.base.module.User;
import com.metis.base.module.UserMark;
import com.metis.base.module.enums.SupportTypeEnum;
import com.metis.base.utils.Log;
import com.metis.base.widget.adapter.delegate.AbsDelegate;
import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.base.widget.adapter.delegate.FooterDelegate;
import com.metis.base.widget.callback.OnScrollBottomListener;
import com.metis.msnetworklib.contract.ReturnInfo;
import com.metis.newslib.ActivityDispatcher;
import com.metis.newslib.R;
import com.metis.newslib.adapter.NewsDetailAdapter;
import com.metis.newslib.adapter.NewsDetailDecoration;
import com.metis.newslib.adapter.delegate.NewsCommentDelegate;
import com.metis.newslib.adapter.delegate.NewsCommentFooterDelegate;
import com.metis.newslib.adapter.delegate.NewsDetailsImgDelegate;
import com.metis.newslib.adapter.delegate.NewsDetailsTitleDelegate;
import com.metis.newslib.adapter.delegate.NewsDetailsTxtDelegate;
import com.metis.newslib.adapter.delegate.NewsDetailsVdoDelegate;
import com.metis.newslib.adapter.delegate.NewsRelativeDelegate;
import com.metis.newslib.adapter.delegate.NewsCardHeaderDelegate;
import com.metis.newslib.manager.NewsManager;
import com.metis.newslib.module.NewsCommentItem;
import com.metis.newslib.module.NewsCommentList;
import com.metis.newslib.module.NewsDetails;
import com.metis.newslib.module.NewsItemRelated;

import java.util.ArrayList;
import java.util.List;

public class NewsDetailActivity extends TitleBarActivity implements View.OnClickListener{

    private static final String TAG = NewsDetailActivity.class.getSimpleName();

    private static final String SHARE_URL = "http://www.meishuquan.net/H5/ContentDetial.ASPX?ID=";

    private NewsDetails mDetails = null;

    private SwipeRefreshLayout mSrl = null;
    private RecyclerView mDetailRv = null;
    private TextView mInputEt = null;
    private ImageView mFavoriteIv = null, mShareIv = null;

    private NewsDetailAdapter mDetailsAdapter = null;

    private Footer mFooter = null;
    private FooterDelegate mFooterDelegate = null;

    private boolean isLoading = false;

    private long mLastIdNew = 0;
    private long mLastIdHot = 0;

    private NewsCommentFooterDelegate mCommentFooterDelegate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        mSrl = (SwipeRefreshLayout)findViewById(R.id.news_detail_swipe_refresh_layout);

        mInputEt = (TextView)findViewById(R.id.news_detail_input);
        mInputEt.setOnClickListener(this);
        mFavoriteIv = (ImageView)findViewById(R.id.news_detail_favorite);
        mFavoriteIv.setOnClickListener(this);
        mShareIv = (ImageView)findViewById(R.id.news_detail_share);
        mShareIv.setOnClickListener(this);
        /*mInputEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    User me = AccountManager.getInstance(NewsDetailActivity.this).getMe();
                    if (me == null) {
                        return false;
                    }
                    NewsManager.getInstance(NewsDetailActivity.this).pushComment(me.userId, mDetails.newsId, mInputEt.getText().toString(), 0, me.getCookie());
                }
                return false;
            }
        });*/

        mDetailRv = (RecyclerView)findViewById(R.id.news_detail_recycler_view);
        mDetailRv.setLayoutManager(new LinearLayoutManager(this));

        mDetailsAdapter = new NewsDetailAdapter(this);
        mDetailRv.setAdapter(mDetailsAdapter);
        mDetailRv.addItemDecoration(new NewsDetailDecoration());
        mDetailRv.addOnScrollListener(new OnScrollBottomListener() {
            @Override
            public void onScrollBottom(RecyclerView recyclerView, int newState) {
                if (!isLoading) {
                    loadNewComments(mDetails.newsId, mLastIdNew);
                    mFooter.setState(Footer.STATE_WAITTING);
                    mDetailsAdapter.notifyDataSetChanged();
                }
            }
        });

        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNewsDetails(mDetails.newsId);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*User me = AccountManager.getInstance(this).getMe();
        if (me == null) {
            mInputEt.setText(R.string.text_comment_must_login);
        } else {
            mInputEt.setText(R.string.text_comment_has_login);
        }*/
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        long newsId = getIntent().getLongExtra(ActivityDispatcher.KEY_NEWS_ID, 0);

        mFooter = new Footer();
        mFooterDelegate = new FooterDelegate(mFooter);

        mSrl.post(new Runnable() {
            @Override
            public void run() {
                mSrl.setRefreshing(true);
            }
        });
        mSrl.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light,
                android.R.color.holo_purple
        );
        loadNewsDetails(newsId);
    }

    private void loadNewsDetails (long newsId) {
        User me = AccountManager.getInstance(this).getMe();
        String session = "";
        if (me != null) {
            session = me.getCookie();
        }
        mLastIdNew = 0;
        mLastIdHot = 0;
        NewsManager.getInstance(this).getNewsInfoById(newsId, session, new RequestCallback<NewsDetails>() {
            @Override
            public void callback(ReturnInfo<NewsDetails> returnInfo, String callbackId) {
                if (mSrl.isRefreshing()) {
                    mSrl.setRefreshing(false);
                }
                if (returnInfo.isSuccess()) {
                    mDetails = returnInfo.getData();
                    if (mDetails.source != null) {
                        getTitleBar().setTitleCenter(mDetails.source.title);
                    } else {
                        getTitleBar().setTitleCenter(R.string.app_name);
                    }

                    mInputEt.setHint(mDetails.commentDefaultText);
                    parseNewsDetail(mDetails);
                    if (mDetails.userMark != null) {
                        mFavoriteIv.setSelected(mDetails.userMark.isFavorite);
                    }
                    Log.v(TAG, "onPostCreate " + returnInfo.getData().content + " size=" + mDetails.getItemList().size());
                }
            }
        });
    }

    private void loadNewComments(long newsId, final long lastId) {
        User me = AccountManager.getInstance(this).getMe();
        String session = "";
        if (me != null) {
            session = me.getCookie();
        }
        isLoading = true;
        NewsManager.getInstance(this).getCommentListByNewsId(newsId, lastId, NewsManager.COMMENT_TYPE_NEW, session, new RequestCallback<NewsCommentList>() {
            @Override
            public void callback(ReturnInfo<NewsCommentList> returnInfo, String callbackId) {
                isLoading = false;
                if (returnInfo.isSuccess()) {
                    List<NewsCommentItem> itemList = returnInfo.getData().newComments;
                    if (itemList != null && !itemList.isEmpty()) {
                        List<BaseDelegate> delegateList = new ArrayList<BaseDelegate>();
                        if (lastId == 0) {
                            delegateList.add(new NewsCardHeaderDelegate(getString(R.string.text_new_comment)));
                        }

                        final int length = itemList.size();
                        for (int i = 0; i < length; i++) {
                            NewsCommentItem item = itemList.get(i);
                            if (i == length - 1) {
                                mLastIdNew = item.id;
                            }
                            NewsCommentDelegate delegate = new NewsCommentDelegate(item);
                            delegate.setDetails(mDetails);
                            delegateList.add(delegate);
                        }
                        if (mDetailsAdapter.hasFooter()) {
                            mDetailsAdapter.addDataList(mDetailsAdapter.getItemCount() - 1, delegateList);
                        } else {
                            mDetailsAdapter.addDataList(delegateList);
                        }

                    }
                }
                mFooter.setState(returnInfo.isSuccess() ? Footer.STATE_SUCCESS : Footer.STATE_NO_MORE);
                if (!mDetailsAdapter.hasFooter()) {
                    mDetailsAdapter.addDataItem(mFooterDelegate);
                }
                mDetailsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadHotComments (long newsId, final long lastIdHot) {
        User me = AccountManager.getInstance(this).getMe();
        String session = "";
        if (me != null) {
            session = me.getCookie();
        }
        NewsManager.getInstance(this).getCommentListByNewsId(newsId, lastIdHot, NewsManager.COMMENT_TYPE_HOT, session, new RequestCallback<NewsCommentList>() {
            @Override
            public void callback(ReturnInfo<NewsCommentList> returnInfo, String callbackId) {
                if (returnInfo.isSuccess()) {
                    List<NewsCommentItem> hotComments = returnInfo.getData().hotComments;
                    if (hotComments != null && !hotComments.isEmpty()) {
                        final int length = hotComments.size();
                        List<BaseDelegate> delegates = new ArrayList<BaseDelegate>();
                        if (lastIdHot == 0) {
                            delegates.add(new NewsCardHeaderDelegate(getString(R.string.text_hot_comment)));
                        }

                        for (int i = 0; i < length; i++) {
                            NewsCommentDelegate delegate = new NewsCommentDelegate(hotComments.get(i));
                            delegate.setDetails(mDetails);
                            delegates.add(delegate);
                        }
                        if (lastIdHot == 0) {
                            mDetailsAdapter.addDataList(delegates);
                            if (mCommentFooterDelegate == null) {
                                mCommentFooterDelegate = new NewsCommentFooterDelegate(getString(R.string.text_load_more));
                            }

                            mDetailsAdapter.addDataItem(mCommentFooterDelegate);
                        } else {
                            int size = mDetailsAdapter.getItemCount();
                            for (int i = size - 1; i >= 0; i--) {
                                AbsDelegate delegate = mDetailsAdapter.getDataItem(i);
                                if (delegate instanceof NewsCommentFooterDelegate) {
                                    mDetailsAdapter.addDataList(i, delegates);
                                    break;
                                }
                            }
                        }
                        mCommentFooterDelegate.setAccessable(true);

                        mLastIdHot = hotComments.get(hotComments.size() - 1).id;
                        //mDetailsAdapter.addNewsCommentDelegateList(delegates);
                    } else {
                        if (mCommentFooterDelegate != null) {
                            mCommentFooterDelegate.setAccessable(false);
                            mCommentFooterDelegate.setSource(getString(R.string.text_no_more));
                        }
                    }
                    if (mCommentFooterDelegate != null) {
                        mCommentFooterDelegate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mCommentFooterDelegate.isAccessable()) {
                                    loadHotComments(mDetails.newsId, mLastIdHot);
                                }
                            }
                        });
                    }
                }
                loadNewComments(mDetails.newsId, mLastIdNew);
            }
        });
    }

    private void parseNewsDetail (NewsDetails details) {
        if (details == null) {
            return;
        }
        mDetailsAdapter.clearDataList();
        List<BaseDelegate> list = new ArrayList<BaseDelegate>();
        list.add(new NewsDetailsTitleDelegate(details));
        List<NewsDetails.Item> itemList = details.getItemList();
        if (itemList != null && !itemList.isEmpty()) {
            final int length = itemList.size();
            for (int i = 0; i < length; i++) {
                NewsDetails.Item item = itemList.get(i);
                if (item.isImg()) {
                    list.add(new NewsDetailsImgDelegate(item));
                } else if (item.isTxt()) {
                    NewsDetailsTxtDelegate txtDelegate = new NewsDetailsTxtDelegate(item);
                    txtDelegate.setDetails(mDetails);
                    list.add(txtDelegate);
                } else if (item.isVideo()) {
                    list.add(new NewsDetailsVdoDelegate(item));
                }
            }
        }
        List<NewsItemRelated> newsItemList = details.relatedNewsList;
        if (newsItemList != null && !newsItemList.isEmpty()) {
            list.add(new NewsCardHeaderDelegate(getString(R.string.text_related_title)));
            final int length = newsItemList.size();
            for (int i = 0; i < length; i++) {
                NewsRelativeDelegate delegate = new NewsRelativeDelegate(newsItemList.get(i));
                delegate.setIsFirst(i == 0);
                delegate.setIsLast(i == length - 1);
                list.add(delegate);
            }
        }
        mDetailsAdapter.clearDataList();
        mDetailsAdapter.addDataList(list);
        mDetailsAdapter.notifyDataSetChanged();
        //loadNewComments(details.newsId, 0);
        loadHotComments(details.newsId, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ActivityDispatcher.REQUEST_CODE_REPLY:
                if (resultCode == RESULT_OK) {

                    NewsCommentItem item = (NewsCommentItem)data.getSerializableExtra(ActivityDispatcher.KEY_NEWS_COMMENT_ITEM);
                    NewsCommentDelegate delegate = new NewsCommentDelegate(item);
                    delegate.setDetails(mDetails);
                    NewsCardHeaderDelegate headerDelegate = null;
                    if (mLastIdNew == 0) {
                        headerDelegate = new NewsCardHeaderDelegate(getString(R.string.text_new_comment));
                    }
                    mDetailsAdapter.addNewsCommentDelegate(delegate, headerDelegate);
                    if (mLastIdNew == 0) {
                        mLastIdNew = item.id;
                    }
                }
                break;
            case com.metis.base.ActivityDispatcher.REQUEST_CODE_LOGIN:
                if (resultCode == RESULT_OK) {
                    loadNewsDetails(mDetails.newsId);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == mInputEt.getId()) {
            if (mDetails != null) {
                User me = AccountManager.getInstance(this).getMe();
                if (me == null) {
                    com.metis.base.ActivityDispatcher.loginActivityWhenAlreadyIn(this);
                    return;
                }
                ActivityDispatcher.replyActivity(NewsDetailActivity.this, mDetails, null);
            }
        } else if (id == mFavoriteIv.getId()) {
            if (mDetails != null) {
                User me = AccountManager.getInstance(this).getMe();
                if (me == null) {
                    com.metis.base.ActivityDispatcher.loginActivityWhenAlreadyIn(this);
                    return;
                }
                final int action = mDetails.userMark != null && mDetails.userMark.isFavorite ? 2 : 1;
                NewsManager.getInstance(this).favorite(me.userId, mDetails.newsId, SupportTypeEnum.News, action, me.getCookie(), new RequestCallback() {
                    @Override
                    public void callback(ReturnInfo returnInfo, String callbackId) {
                        if (returnInfo.isSuccess()) {
                            if (action == 1) {
                                if (mDetails.userMark == null) {
                                    mDetails.userMark = new UserMark();
                                }
                                mDetails.userMark.isFavorite = true;
                                Toast.makeText(NewsDetailActivity.this, R.string.toast_favorite_success, Toast.LENGTH_SHORT).show();
                            } else {
                                mDetails.userMark.isFavorite = false;
                                Toast.makeText(NewsDetailActivity.this, R.string.toast_favorite_cancel_success, Toast.LENGTH_SHORT).show();
                            }
                            mFavoriteIv.setSelected(mDetails.userMark.isFavorite);
                        } else {
                            if (action == 1) {
                                Toast.makeText(NewsDetailActivity.this, R.string.toast_favorite_failed, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(NewsDetailActivity.this, R.string.toast_favorite_failed, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        } else if (id == mShareIv.getId()) {
            String title = mDetails.title;
            String text = TextUtils.isEmpty(mDetails.description) ? mDetails.title : mDetails.description;
            String imageUrl = mDetails.thumbnail;
            String url = SHARE_URL + mDetails.newsId;
            com.metis.base.ActivityDispatcher.shareActivity(this, mDetails.newsId, title, text, imageUrl, url);
        }
    }
}
