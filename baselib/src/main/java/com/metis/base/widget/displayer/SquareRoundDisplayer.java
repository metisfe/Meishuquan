package com.metis.base.widget.displayer;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

public class SquareRoundDisplayer implements BitmapDisplayer {

    private static final String TAG = SquareRoundDisplayer.class.getSimpleName();
	
	private int mSize, mRound = 0;

    public SquareRoundDisplayer(int size) {
        this (size, size / 2);
    }

	public SquareRoundDisplayer(int size, int round) {
		mSize = size;
		mRound = round;
	}

	@Override
	public void display(Bitmap bmp, ImageAware aware, LoadedFrom from) {
		final int wid = bmp.getWidth();
		final int hei = bmp.getHeight();
		
		float scale = Math.min((float) wid / mSize, (float) hei / mSize);
		final int scaleSize = (int)(mSize * scale);
		Bitmap square = Bitmap.createBitmap(bmp, (wid - scaleSize) / 2, (hei - scaleSize) / 2, scaleSize, scaleSize);
        //Log.e(TAG, "display " + mSize + " ori bmp = " + square);
        Bitmap scaledBmp = Bitmap.createScaledBitmap(square, mSize, mSize, true);
        //square.recycle();
		
		aware.setImageBitmap(toRoundCorner(scaledBmp, mRound));
	}
	
	public Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap roundCornerBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(roundCornerBitmap);
        int color = 0xff424242;//int color = 0xff424242;
        Paint paint = new Paint();
        paint.setColor(color);
        //防止锯齿
        paint.setAntiAlias(true);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float roundPx = pixels;
        //相当于清屏
        canvas.drawARGB(0, 0, 0, 0);      
        //先画了一个带圆角的矩形
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        //再把原来的bitmap画到现在的bitmap！！！注意这个理解
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return roundCornerBitmap;
    }

}
