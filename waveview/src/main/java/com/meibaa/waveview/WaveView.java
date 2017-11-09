package com.meibaa.waveview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 昝奥博 on 2017/8/14
 * Email:18772833900@163.com
 * Explain：波纹扩散
 */
public class WaveView extends View {
    private float mInitialRadius;   // 初始波纹半径
    private float mMaxRadius;   // 最大波纹半径
    private long mDuration; // 界面停留时间
    private int mSpeed;   // 创建间隔时间
    private float mMaxRadiusRate;
    public static final int DEFAULT_TXT_SIZE = 25;
    public static final int DEFAULT_TXT_COLOR = Color.WHITE;
    public static final int DEFAULT_WAVE_SPEED = 500;
    public static final float DEFAULT_MAX_RADIUS_RATE = 0.95F;
    public static final int DEFAULT_WAVE_COLOR = Color.YELLOW;
    public static final int DEFAULT_WAVE_DURATION = 500;
    private boolean mMaxRadiusSet;
    private boolean mIsRunning;
    private long mLastCreateTime;
    private List<Circle> mCircleList = new ArrayList<>();
    private Runnable mCreateCircle;
    private Interpolator mInterpolator;
    private Paint mPaint;
    private Paint mTxtPaint;
    private int mTxtSize;
    private int mTxtColor;
    private String mMarkStr;
    private int mWaveColor;

    public WaveView(Context context) {
        super(context);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        //中间的文字 大小 颜色
        mMarkStr = typedArray.getString(R.styleable.WaveView_android_text);
        mTxtSize = typedArray.getDimensionPixelSize(R.styleable.WaveView_android_textSize, DEFAULT_TXT_SIZE);
        mTxtColor = typedArray.getColor(R.styleable.WaveView_android_textColor, DEFAULT_TXT_COLOR);
        //水波的颜色 , 扩散速度 ， 持续时间，最大的圆角率
        mWaveColor = typedArray.getColor(R.styleable.WaveView_android_color, DEFAULT_WAVE_COLOR);
        mSpeed = typedArray.getInt(R.styleable.WaveView_wCreateDuration, DEFAULT_WAVE_SPEED);
        mDuration = typedArray.getInt(R.styleable.WaveView_wRunDuration, DEFAULT_WAVE_DURATION);
        mMaxRadiusRate = typedArray.getFloat(R.styleable.WaveView_wMaxRadiusRate, DEFAULT_MAX_RADIUS_RATE);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInterpolator = new LinearInterpolator();
        mCreateCircle = new Runnable() {
            @Override
            public void run() {
                if (mIsRunning) {
                    newCircle();
                    postDelayed(mCreateCircle, mSpeed);
                }
            }
        };
    }


    public void setWaveColor(int color) {
        this.mWaveColor = color;
    }

    public void setTxtColor(int color){
        this.mTxtColor = color;
    }

    public void setText(String mark) {
        this.mMarkStr = mark;
    }

    public void setTextSize(int txtSize) {
        this.mTxtSize = txtSize;
    }

    public void setMaxRadiusRate(float maxRadiusRate) {

        mMaxRadiusRate = maxRadiusRate;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (!mMaxRadiusSet) mMaxRadius = Math.min(w, h) * mMaxRadiusRate / 2.0f;
    }


    /**
     * 开始
     */
    public void start() {
        if (!mIsRunning) {
            mIsRunning = true;
            mCreateCircle.run();
        }
    }


    /**
     * 缓慢停止
     */
    public void stop() {
        mIsRunning = false;
    }

    /**
     * 立即停止
     */
    public void stopImmediately() {
        mIsRunning = false;
        mCircleList.clear();
        invalidate();
    }

    public void setInitialRadius(float radius) {
        mInitialRadius = radius;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public void setMaxRadius(float maxRadius) {
        mMaxRadius = maxRadius;
        mMaxRadiusSet = true;
    }

    public void setSpeed(int speed) {
        mSpeed = speed;
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
        if (mInterpolator == null) {
            mInterpolator = new LinearInterpolator();
        }
    }


    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mWaveColor);
        Iterator<Circle> iterator = mCircleList.iterator();
        while (iterator.hasNext()) {
            Circle circle = iterator.next();
            float radius = circle.getCurrentRadius();
            if (System.currentTimeMillis() - circle.mCreateTime < mDuration) {
                mPaint.setAlpha(circle.getAlpha());
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mPaint);
            } else {
                iterator.remove();
            }
        }
        if (!TextUtils.isEmpty(mMarkStr)) {
            mTxtPaint = new Paint();
            mTxtPaint.setAntiAlias(true);
            mTxtPaint.setTextSize(mTxtSize);
            mTxtPaint.setColor(mTxtColor);
            Rect bounds = new Rect();
            mTxtPaint.getTextBounds(mMarkStr, 0, mMarkStr.length(), bounds);
            Paint.FontMetricsInt fontMetrics = mTxtPaint.getFontMetricsInt();
            int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            canvas.drawText(mMarkStr, getMeasuredWidth() / 2 - bounds.width() / 2, baseline, mTxtPaint);

        }
        if (mCircleList.size() > 0) {
            postInvalidateDelayed(10);
        }
    }

    private void newCircle() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastCreateTime < mSpeed) {
            return;
        }
        Circle circle = new Circle();
        mCircleList.add(circle);
        invalidate();
        mLastCreateTime = currentTime;
    }

    private class Circle {
        private long mCreateTime;

        public Circle() {
            mCreateTime = System.currentTimeMillis();
        }

        public int getAlpha() {
            float percent = (getCurrentRadius() - mInitialRadius) / (mMaxRadius - mInitialRadius);
            return (int) (255 - mInterpolator.getInterpolation(percent) * 255);
        }

        public float getCurrentRadius() {
            float percent = (System.currentTimeMillis() - mCreateTime) * 1.0f / mDuration;
            return mInitialRadius + mInterpolator.getInterpolation(percent) * (mMaxRadius - mInitialRadius);
        }
    }
}
