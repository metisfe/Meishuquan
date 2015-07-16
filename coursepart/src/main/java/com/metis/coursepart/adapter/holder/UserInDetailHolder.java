package com.metis.coursepart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.metis.base.manager.DisplayManager;
import com.metis.base.module.User;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.delegate.UserInDetailDelegate;
import com.metis.coursepart.module.ContentItem;
import com.metis.coursepart.module.CourseAlbum;

import java.util.List;

/**
 * Created by Beak on 2015/7/15.
 */
public class UserInDetailHolder extends AbsViewHolder<UserInDetailDelegate>{

    public TextView titleTv, subTitleTv;
    public ImageView profileIv;
    public TextView nameTv;
    public WebView contentWv = null;
    public LinearLayout contentContainer = null;

    public UserInDetailHolder(View itemView) {
        super(itemView);
        titleTv = (TextView)itemView.findViewById(R.id.title_text);
        subTitleTv = (TextView)itemView.findViewById(R.id.title_sub_text);
        profileIv = (ImageView)itemView.findViewById(R.id.user_profile);
        nameTv = (TextView)itemView.findViewById(R.id.user_name);
        contentContainer = (LinearLayout)itemView.findViewById(R.id.content_item_container);
        contentWv = (WebView)itemView.findViewById(R.id.web_content);
        contentWv.getSettings().setDefaultTextEncodingName("UTF-8");
    }

    @Override
    public void bindData(Context context, UserInDetailDelegate userInDetailDelegate, RecyclerView.Adapter adapter, int position) {
        CourseAlbum album = userInDetailDelegate.getSource();
        titleTv.setText(album.title);
        subTitleTv.setText(context.getString(R.string.course_play_count_history, album.viewCount));
        User user = album.studio;
        if (user != null) {
            DisplayManager.getInstance(context).display(user.avatar, profileIv);
            nameTv.setText(user.name);
        }
        List<ContentItem> items = userInDetailDelegate.getContentItemList();
        contentContainer.removeAllViews();
        if (items != null && !items.isEmpty()) {
            final int length = items.size();
            for (int i = 0; i < length; i++) {
                ContentItem item = items.get(i);
                if (item.isTxt()) {
                    TextView tv = new TextView(context);
                    tv.setText(item.data.Content);
                    contentContainer.addView(tv);
                } else {
                    ImageView iv = new ImageView(context);
                    DisplayManager.getInstance(context).display(item.data.ThumbnailsURL, iv);
                    contentContainer.addView(iv);
                }
            }
        }
        //String data = "<h1>shumiashumiao 速度发货胜多负少豆腐<img src=\\\"https://metisfile.blob.core.chinacloudapi.cn/201507/201507151104513716_378*539_T.jpg\" alt=\"\" /></h1><h1>Title</h1><p>This is HTML text<br /><i>Formatted in italics</i><br />Anothor Line</p>";
        //contentWv.loadUrl("http://www.qq.com/");
        /*String data1 = "<p>shumiashumiao 速度发货胜多负少豆腐<img src=\"https://metisfile.blob.core.chinacloudapi.cn/201507/201507151104513716_378*539_T.jpg\" alt=\"\" /></p>";
        contentWv.loadData(*//*userInDetailDelegate.getWebContent()*//*data1, "text/html", "UTF-8");*/
    }
}