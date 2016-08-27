package com.wangmaodou.heartview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Maodou on 2016/8/11.
 */
public class HeartView extends View {

    public static int LIKE_STATUS=0x1000;
    public static int UNLIKE_STATUS=0x2000;

    private Paint mBorderPaint,mLikePaint;
    private int lineColor,likeColor;
    private Path mPath;
    private int mWidth,mHeight,mReal;
    private ValueAnimator mValueAnimator;

    private StatusChangeListener mListener;

    private float mSeekValue=1;
    private int mState=UNLIKE_STATUS;

    public HeartView(Context context){
        this(context,null);
    }
    public HeartView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init();
    }

    public void setStateChangeListener(StatusChangeListener listener){
        this.mListener=listener;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMeasureMode= MeasureSpec.getMode(widthMeasureSpec);
        int heightMeasureMode=MeasureSpec.getMode(heightMeasureSpec);
        if(widthMeasureMode==MeasureSpec.AT_MOST||heightMeasureMode==MeasureSpec.AT_MOST){
            this.mWidth=dip2px(50);
            this.mHeight=dip2px(50);
            setMeasuredDimension(mWidth,mHeight);
        }else {
            this.mWidth=MeasureSpec.getSize(widthMeasureSpec);
            this.mHeight=MeasureSpec.getSize(heightMeasureSpec);
        }
        mReal=Math.min(mWidth,mHeight);
    }

    private void init(){
        initPaint();
        mValueAnimator=getValueAnimator(0,1,300);
    }
    private void initPaint(){
        initColor();
        mLikePaint=new Paint();
        mLikePaint.setAntiAlias(true);
        mLikePaint.setColor(likeColor);
        mLikePaint.setStyle(Paint.Style.FILL);

        mBorderPaint=new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(lineColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeJoin(Paint.Join.ROUND);
        mBorderPaint.setStrokeCap(Paint.Cap.ROUND);
        mBorderPaint.setStrokeWidth(dip2px(2));
    }
    private void initColor(){
        lineColor=Color.BLUE;
        likeColor=Color.RED;
    }
    private Path getHeartPath(float f){
        int X=mWidth/2;
        int Y=mReal/2;
        Path path=new Path();
        int rate=(int)(mReal*f);
        path.moveTo(X,Y-rate/4);
        path.cubicTo(X+rate/4,Y-rate*3/4,X+rate,Y,X,Y+rate*3/7);
        path.cubicTo(X-rate,Y,X-rate/4,Y-rate*3/4,X,Y-rate/4);
        return path;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        mPath=getHeartPath(0.83f);
        if (mState==UNLIKE_STATUS) {
            canvas.drawPath(mPath,mBorderPaint);
        }else {
            canvas.drawPath(mPath, mBorderPaint);
            canvas.drawPath(getHeartPath(mSeekValue*0.95f),mLikePaint);
        }
        canvas.restore();
    }

    float x,y;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                x=event.getX();
                y=event.getY();
            }
            case MotionEvent.ACTION_UP:{
                float nx=Math.abs(event.getX()-x);
                float ny=Math.abs(event.getY()-y);
                if(nx<5&&ny<5){
                    changeState();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void changeState(){
        if(mState==UNLIKE_STATUS){
            mState=LIKE_STATUS;
            if(!mValueAnimator.isRunning())
                mValueAnimator.start();
            if (mListener!=null)
                mListener.onLike();
        }else {
            mState=UNLIKE_STATUS;
            if (mListener!=null)
                mListener.onUnlike();
        }
        invalidate();
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private ValueAnimator getValueAnimator(float start,float end,long time){
        ValueAnimator animator=ValueAnimator.ofFloat(start,end);
        animator.setDuration(time);
        animator.setRepeatCount(0);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Log.d("===========",valueAnimator.getAnimatedValue().toString());
                mSeekValue=(Float)valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        return animator;
    }

    public interface StatusChangeListener{
       public void onLike();
       public void onUnlike();
    }
}
