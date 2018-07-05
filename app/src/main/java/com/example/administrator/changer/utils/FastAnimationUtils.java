package com.example.administrator.changer.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

/**
 * Created by Tianluhua on 2018\7\5 0005.
 */
public class FastAnimationUtils {

    /**
     * 每一帧动画抽象
     */
    private class AnimationFrame {
        private int mResourceId;
        private int mDuration;

        public AnimationFrame(int mResourceId, int mDuration) {
            this.mResourceId = mResourceId;
            this.mDuration = mDuration;
        }

        public int getmResourceId() {
            return mResourceId;
        }

        public void setmResourceId(int mResourceId) {
            this.mResourceId = mResourceId;
        }

        public int getmDuration() {
            return mDuration;
        }

        public void setmDuration(int mDuration) {
            this.mDuration = mDuration;
        }
    }

    /**
     * 动画停止监听
     */
    public interface OnAnimationStoppedListener {
        public void onAnimationStopped();
    }

    /**
     * 动画播放到对应位置的监听
     */
    public interface OnAnimationFrameChangedListener {
        public void onAnimationFrameChanged(int index);
    }

    private OnAnimationStoppedListener mOnAnimationStoppedListener;

    public void setOnAnimationStoppedListener(OnAnimationStoppedListener listener) {
        mOnAnimationStoppedListener = listener;
    }

    private OnAnimationFrameChangedListener mOnAnimationFrameChangedListener;

    public void setOnAnimationFrameChangedListener(OnAnimationFrameChangedListener listener) {
        mOnAnimationFrameChangedListener = listener;
    }


    private ArrayList<AnimationFrame> mAnimationFrames;
    private int mIndex;
    private boolean mShounldRun;
    private boolean mIsRuning;
    private SoftReference<ImageView> mSoftReferenceImageView;
    private Handler mHandler;
    private Bitmap mRecycleBitmap;

    private FastAnimationUtils(ImageView mImageView) {
        init(mImageView);
    }

    //单例
    private static FastAnimationUtils fastAnimationUtilsInstance;

    public static FastAnimationUtils getInstance(ImageView mImageView) {
        if (fastAnimationUtilsInstance == null)
            fastAnimationUtilsInstance = new FastAnimationUtils(mImageView);
        fastAnimationUtilsInstance.mRecycleBitmap = null;
        return fastAnimationUtilsInstance;
    }

    //初始化参数
    public void init(ImageView mImageView) {
        mAnimationFrames = new ArrayList<>();
        mSoftReferenceImageView = new SoftReference<>(mImageView);
        mHandler = new Handler(Looper.getMainLooper());

        if (mIsRuning == true)
            stopAnimation();

        mShounldRun = false;
        mIsRuning = false;
        mIndex = -1;
    }

    public void addAllFrames(int[] resIds, int interval) {
        for (int resId : resIds) {
            mAnimationFrames.add(new AnimationFrame(resId, interval));
        }

    }


    /**
     * 开始动画
     */
    public synchronized void startAnimation() {
        mShounldRun = true;
        if (mIsRuning)
            return;
        mHandler.post(new FramesSequenceAnimation());

    }

    /**
     * 停止动画
     */
    public synchronized void stopAnimation() {
        mShounldRun = false;
        mIsRuning = false;
    }

    private AnimationFrame getNext() {
        mIndex++;
        if (mIndex >= mAnimationFrames.size())
            mIndex = 0;
        return mAnimationFrames.get(mIndex);
    }


    private class FramesSequenceAnimation implements Runnable {

        @Override
        public void run() {
            ImageView imageView = mSoftReferenceImageView.get();
            if (!mShounldRun || imageView == null) {
                if (mOnAnimationStoppedListener != null)
                    mOnAnimationStoppedListener.onAnimationStopped();
                return;
            }
            mIsRuning = true;
            if (imageView.isShown()) {
                AnimationFrame frame = getNext();
                GetAnimaDrawableTask task = new GetAnimaDrawableTask(imageView);
                task.execute(frame.getmResourceId());
                mHandler.postDelayed(this, frame.getmDuration());
            }
        }
    }

    private class GetAnimaDrawableTask extends AsyncTask<Integer, Void, Drawable> {

        private ImageView mImageView;

        public GetAnimaDrawableTask(ImageView mImageView) {
            this.mImageView = mImageView;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Drawable doInBackground(Integer... integers) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            if (mRecycleBitmap != null)
                options.inBitmap = mRecycleBitmap;
            mRecycleBitmap = BitmapFactory.decodeResource(mImageView.getResources(), integers[0], options);
            BitmapDrawable drawable = new BitmapDrawable(mImageView.getResources(), mRecycleBitmap);
            return drawable;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            if (drawable != null&&mIsRuning) mImageView.setImageDrawable(drawable);
            if (mOnAnimationFrameChangedListener != null)
                mOnAnimationFrameChangedListener.onAnimationFrameChanged(mIndex);
        }
    }

}
