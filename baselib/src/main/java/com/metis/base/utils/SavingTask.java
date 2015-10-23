package com.metis.base.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SavingTask extends AsyncTask<SavingTask.Savable, Integer, Boolean> {

    private Callback mCallback = null;
    private List<String> mPathList = new ArrayList<String>();

    private Exception mException = null;
    private int mTotal = 0;

    @Override
    protected Boolean doInBackground(Savable... params) {
        mTotal = params.length;
        for (int i = 0; i < mTotal; i++) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(params[i].getPath());
                fos.write(params[i].getData());
                fos.flush();
                mPathList.add(params[i].getPath());
                publishProgress(i);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                mException = e;
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                mException = e;
                return false;
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (mCallback != null) {
            mCallback.onProgress(values[0].intValue(), mTotal);
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (mCallback != null) {
            if (aBoolean) {
                mCallback.onSuccess(mPathList);
            } else {
                mCallback.onException(mException);
            }
        }
    }

    public void setCallback (Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        public void onSuccess (List<String> pathList);
        public void onProgress (int progress, int total);
        public void onException (Exception e);
    }

    public interface Savable {
        public byte[] getData ();
        public String getPath ();
    }

    public static class BitmapSave implements Savable {

        private Bitmap mBmp = null;
        private String mPath = null;

        public BitmapSave (Bitmap bitmap, String path) {
            mBmp = bitmap;
            mPath = path;
        }

        @Override
        public byte[] getData() {
            return FileUtils.bitmapToByteArray(Bitmap.CompressFormat.JPEG, mBmp);
        }

        @Override
        public String getPath() {
            return mPath;
        }
    }
}