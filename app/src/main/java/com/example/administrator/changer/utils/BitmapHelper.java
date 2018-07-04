package com.example.administrator.changer.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by tianluhua on 2018/7/4.
 */

public class BitmapHelper {
    private Bitmap bmp;
    private Bitmap currBitmap;
    private int bmpW, bmpH;
    private static final int UPDATE_IMAGE = 0x001;
    private int getCount = 1;
    private ImageView imageView;

    public BitmapHelper(Bitmap bmp, ImageView img) {
        this.imageView = img;
        this.bmp = bmp;
        bmpW = bmp.getWidth();
        bmpH = bmp.getHeight();
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (getCount <= bmpH) {
                int[] pixels = new int[bmpW * bmpH];
                int[] currPixels = new int[bmpW * bmpH];
                bmp.getPixels(pixels, 0, bmpW, 0, bmpH - getCount, bmpW, getCount);
                System.arraycopy(pixels, 0, currPixels, (currPixels.length - bmpW * getCount), bmpW * getCount);
                if (currBitmap != null && currBitmap != bmp) {
                    currBitmap.recycle();
                }
                if (getCount < bmpH) {
                    currBitmap = Bitmap.createBitmap(currPixels, 0, bmpW, bmpW, bmpH,
                            Bitmap.Config.ARGB_4444);
                } else {
                    currBitmap = bmp;
                }
                imageView.setImageBitmap(currBitmap);
                mHandler.sendEmptyMessageDelayed(UPDATE_IMAGE, 10);
                getCount++;
            }
        }
    };

    public void startAnim() {
        getCount = 1;
        mHandler.obtainMessage(UPDATE_IMAGE).sendToTarget();
    }
}
