package com.metis.base.widget.adapter.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.metis.base.R;
import com.metis.base.manager.CacheManager;
import com.metis.base.manager.DisplayManager;
import com.metis.base.module.User;
import com.metis.base.utils.FileUtils;
import com.metis.base.utils.Log;
import com.metis.base.widget.TitleBar;
import com.metis.base.widget.adapter.delegate.MeHeaderDelegate;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.File;

import android.support.v8.renderscript.*;

/**
 * Created by Beak on 2015/8/24.
 */
public class MeHeaderHolder extends AbsViewHolder<MeHeaderDelegate> {

    private static final String TAG = MeHeaderHolder.class.getSimpleName();

    public ImageView meBg = null;
    public TitleBar meTb = null;
    public ImageView meProfileIv = null, meMoreIv = null;
    public TextView meNameTv = null;
    public TextView meFocusCountTv, meFollowCountTv, meChargeTv;

    public MeHeaderHolder(View itemView) {
        super(itemView);

        meBg = (ImageView)itemView.findViewById(R.id.me_card_bg);
        meTb = (TitleBar)itemView.findViewById(R.id.me_card_title_bar);
        meProfileIv = (ImageView)itemView.findViewById(R.id.me_card_profile);
        meMoreIv = (ImageView)itemView.findViewById(R.id.me_card_profile_more);
        meNameTv = (TextView)itemView.findViewById(R.id.me_name);
        meFocusCountTv = (TextView)itemView.findViewById(R.id.me_focus_count);
        meFollowCountTv = (TextView)itemView.findViewById(R.id.me_follows_count);
        meChargeTv = (TextView)itemView.findViewById(R.id.me_charge_count);
    }

    @Override
    public void bindData(final Context context, MeHeaderDelegate meHeaderDelegate, RecyclerView.Adapter adapter, int position) {
        User me = meHeaderDelegate.getSource();
        if (me == null) {
            return;
        }
        final String avatar = me.getAvailableAvatar();
        Log.v(TAG, "avatar=" + avatar);
        File tempFile = new File (CacheManager.getInstance(context).getMyImageCacheDir(), FileUtils.getNameFromUrl(avatar));
        if (tempFile.exists()) {
            manageProfile(context, tempFile);
        } else {
            HttpUtils httpUtils = new HttpUtils(10 * 1000);
            httpUtils.download(avatar, tempFile.getAbsolutePath(), new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    File file = responseInfo.result;
                    manageProfile(context, file);
                }

                @Override
                public void onFailure(HttpException e, String s) {

                }
            });
        }

        meNameTv.setText(me.name);
        meFocusCountTv.setText(me.focusNum + "");
        meFollowCountTv.setText(me.fansNum + "");
        meChargeTv.setText(0 + "");
    }

    private void manageProfile (Context context, File file) {
        if (file != null && file.exists()) {
            Log.v(TAG, "cache dir=" + file.getAbsolutePath());
            final int profileSize = context.getResources().getDimensionPixelSize(R.dimen.profile_size_big);
            DisplayManager.getInstance(context).display(
                    ImageDownloader.Scheme.FILE.wrap(file.getAbsolutePath()),
                    meProfileIv,
                    DisplayManager.getInstance(context).makeRoundDisplayImageOptions(profileSize));
            Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
            Bitmap temp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
            RenderScript rs = RenderScript.create(context);
            Allocation input = Allocation.createFromBitmap(rs, bmp);
            Allocation output = Allocation.createFromBitmap(rs, temp);
            ScriptIntrinsicBlur scriptBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            scriptBlur.setInput(input);
            scriptBlur.setRadius(10);
            scriptBlur.forEach(output);
            output.copyTo(temp);
            rs.destroy();
            meBg.setBackground(new BitmapDrawable(temp));
        }
    }
}
