package com.metis.base.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Pair;

import com.google.gson.reflect.TypeToken;
import com.metis.base.framework.NetProxy;
import com.metis.base.module.Thumbnail;
import com.metis.base.utils.FileUtils;
import com.metis.base.utils.Log;
import com.metis.msnetworklib.contract.ReturnInfo;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;

import org.apache.http.client.methods.HttpPost;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Beak on 2015/7/30.
 */
public class UploadManager extends AbsManager {

    private static final String TAG = UploadManager.class.getSimpleName();

    private static UploadManager sManager = null;

    public synchronized static UploadManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new UploadManager(context.getApplicationContext());
        }
        return sManager;
    }

    private UploadManager(Context context) {
        super(context);
    }

    public void uploadBitmap (Bitmap bitmap, String session, final RequestCallback<List<Thumbnail>> callback) {
        List<Bitmap> list = new ArrayList<Bitmap>();
        list.add(bitmap);
        uploadBitmap(list, session, callback);
    }

    public void uploadBitmap (List<Bitmap> bitmaps, String session, final RequestCallback<List<Thumbnail>> callback) {
        //int totalLength = 0;
        final int length = bitmaps.size();
        /*for (int i = 0; i < length; i++) {
            totalLength += bitmaps.get(0).getByteCount();
        }*/
        byte[][] totalData = new byte[length][];
        for (int i = 0; i < length; i++) {
            totalData[i] = FileUtils.bitmapToByteArray(Bitmap.CompressFormat.JPEG, bitmaps.get(i));
        }
        NetProxy.getInstance(getContext()).upload(NetProxy.TYPE_IMAGE, totalData, session, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo<List<Thumbnail>> returnInfo = getGson().fromJson(result,
                        new TypeToken<ReturnInfo<List<Thumbnail>>>() {
                        }.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }

    public void uploadImage (File file, String session, final RequestCallback<List<Thumbnail>> callback) {
        uploadFile(file, NetProxy.TYPE_IMAGE, session, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo<List<Thumbnail>> returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo<List<Thumbnail>>>(){}.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }

    /**
     * @param file
     * @param type NetProxy.TYPE_IMAGE, TYPE_VOICE, TYPE_AMR;
     * @param session
     * @param listener
     */
    public void uploadFile (File file, int type, String session, NetProxy.OnResponseListener listener) {
        List<File> fileList = new ArrayList<File>();
        fileList.add(file);
        uploadFile(fileList, type, session, listener);
    }

    public void uploadFile (List<File> fileList, int type, String session, NetProxy.OnResponseListener listener) {
        final int length = fileList.size();
        byte[][] totalData = new byte[length][];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i < length; i++) {
            File file = fileList.get(i);
            try {
                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int result = 0;
                while ((result = fis.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, result);
                }
                fis.close();
                byteArrayOutputStream.close();
                totalData[i] = byteArrayOutputStream.toByteArray();
                Log.v(TAG, "uploadFile[" + i + "]=" + totalData[i].length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        NetProxy.getInstance(getContext()).upload(type, totalData, session, listener);

    }

}
