package com.junmeng.gdv;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * 手势识别器
 * Created by HWJ on 2016/11/25.
 */

public class GestureDetectorView extends View {
    private static final String TAG = "GestureDetectorView";

    public interface OnGestureListener {
        /**
         * @param gesture  手势
         * @param velocity 缩放手势表示范围，滑动手势表示速率
         */
        void onGesture( int gesture, float velocity);
    }

    public static final int GESTURE_SLIDE_UP = 1;
    public static final int GESTURE_SLIDE_DOWN = 2;
    public static final int GESTURE_SLIDE_LEFT = 3;
    public static final int GESTURE_SLIDE_RIGHT = 4;
    public static final int GESTURE_SLIDE_LEFT_UP = 5;
    public static final int GESTURE_SLIDE_LEFT_DOWN = 6;
    public static final int GESTURE_SLIDE_RIGHT_UP = 7;
    public static final int GESTURE_SLIDE_RIGHT_DOWN = 8;
    public static final int GESTURE_SCALE_ZOOMIN = 9;//放大
    public static final int GESTURE_SCALE_ZOOMOUT = 10;

   // @IntDef({GESTURE_SLIDE_UP, GESTURE_SLIDE_DOWN, GESTURE_SLIDE_LEFT, GESTURE_SLIDE_RIGHT, GESTURE_SLIDE_LEFT_UP
   //         , GESTURE_SLIDE_LEFT_DOWN, GESTURE_SLIDE_RIGHT_UP, GESTURE_SLIDE_RIGHT_DOWN, GESTURE_SCALE_ZOOMIN, GESTURE_SCALE_ZOOMOUT
   // })
   // public @interface Gesture {
   // }


    private GestureDetector gestureDetector;//一般手势
    private ScaleGestureDetector scaleGestureDetector;//缩放手势
    private OnGestureListener onGestureListener;

    public GestureDetectorView(Context context) {
        super(context, null, 0);
    }

    public GestureDetectorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureDetectorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        Log.i(TAG, "init");
        setClickable(true);
        scaleGestureDetector = new ScaleGestureDetector(context, new MyScaleGestureListener());
        gestureDetector = new GestureDetector(context, new MyGestureListener());
    }

    /**
     * 设置手势监听器
     *
     * @param listener
     */
    public void setOnGestureListener(OnGestureListener listener) {
        onGestureListener = listener;
    }


    boolean isSingle = true;//是否单指
    boolean isDown = false;//是否第一次按下

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.i(TAG, "onTouchEvent");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Log.i(TAG, "ACTION_DOWN" + event.getPointerCount());
                isDown = true;
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.i(TAG, "ACTION_MOVE" + event.getPointerCount());
                if (isDown && event.getPointerCount() > 1) {//关键判断，如果是多指，则move事件中PointerCount大于1的个数居多
                    isSingle = false;
                }
                isDown = false;
                break;
            case MotionEvent.ACTION_UP:
                // Log.i(TAG, "ACTION_UP" + event.getPointerCount());
                isSingle = true;
                isDown = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                //Log.i(TAG, "ACTION_CANCEL" + event.getPointerCount());
                isSingle = true;
                isDown = false;
                break;
        }

        if (isSingle) {
            return gestureDetector.onTouchEvent(event);
        } else {
            return scaleGestureDetector.onTouchEvent(event);
        }

    }

    private class MyScaleGestureListener extends android.view.ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return super.onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            float previousSpan = detector.getPreviousSpan();
            float currentSpan = detector.getCurrentSpan();
            float dis = Math.abs(previousSpan - currentSpan);
            if (currentSpan < previousSpan) {
                Log.i(TAG, "缩小:" + dis);
                onGesture(GESTURE_SCALE_ZOOMOUT, dis);
            } else {
                Log.i(TAG, "放大:" + dis);
                onGesture(GESTURE_SCALE_ZOOMIN, dis);
            }

        }
    }

    private void onGesture(int gesture, float dis) {
        if (onGestureListener != null) {
            onGestureListener.onGesture(gesture, dis);
        }
    }

    private class MyGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // Log.i(TAG, "velocityX=" + velocityX + ",velocityY=" + velocityY);
            float velocity = (float) Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2));
            float x1 = e1.getX();
            float y1 = e1.getY();
            float x2 = e2.getX();
            float y2 = e2.getY();
            float distanceX = x2 - x1;
            float distanceY = y2 - y1;
            // Log.i(TAG, "distanceX=" + distanceX + ",distanceY=" + distanceY);
            if (distanceX > 0) {//向右
                double y3 = distanceX * Math.tan(22.5 * Math.PI / 180);
                double y4 = distanceX * Math.tan((45 + 22.5) * Math.PI / 180);
                double absY = Math.abs(distanceY);
                if (absY < y3) {//向右滑

                    Log.i(TAG, "向右滑,速率：" + velocity);
                    onGesture(GESTURE_SLIDE_RIGHT, velocity);
                } else if (absY < y4) {
                    if (distanceY > 0) {//向右下滑
                        Log.i(TAG, "向右下滑,速率：" + velocity);
                        onGesture(GESTURE_SLIDE_RIGHT_DOWN, velocity);
                    } else {//向右上滑
                        Log.i(TAG, "向右上滑,速率：" + velocity);
                        onGesture(GESTURE_SLIDE_RIGHT_UP, velocity);
                    }
                } else {
                    if (distanceY > 0) {//向下滑
                        Log.i(TAG, "向下滑,速率：" + velocity);
                        onGesture(GESTURE_SLIDE_DOWN, velocity);
                    } else {//向上滑
                        Log.i(TAG, "向上滑,速率：" + velocity);
                        onGesture(GESTURE_SLIDE_UP, velocity);
                    }
                }

            } else {//向左
                double y3 = Math.abs(distanceX * Math.tan(22.5 * Math.PI / 180));
                double y4 = Math.abs(distanceX * Math.tan((45 + 22.5) * Math.PI / 180));
                double absY = Math.abs(distanceY);
                if (absY < y3) {//向左滑
                    Log.i(TAG, "向左滑,速率：" + velocity);
                    onGesture(GESTURE_SLIDE_LEFT, velocity);
                } else if (absY < y4) {
                    if (distanceY > 0) {//向左下滑
                        Log.i(TAG, "向左下滑,速率：" + velocity);
                        onGesture(GESTURE_SLIDE_LEFT_DOWN, velocity);
                    } else {//向左上滑
                        Log.i(TAG, "向左上滑,速率：" + velocity);
                        onGesture(GESTURE_SLIDE_LEFT_UP, velocity);
                    }
                } else {
                    if (distanceY > 0) {//向下滑
                        Log.i(TAG, "向下滑,速率：" + velocity);
                        onGesture(GESTURE_SLIDE_DOWN, velocity);
                    } else {//向上滑
                        Log.i(TAG, "向上滑,速率：" + velocity);
                        onGesture(GESTURE_SLIDE_UP, velocity);
                    }
                }
            }
            return true;

        }

        @Override
        public void onShowPress(MotionEvent e) {
            super.onShowPress(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            if (e.getPointerCount() >= 2) {
                return false;
            }

            return true;//需要返回true 否则onFling不起作用
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onContextClick(MotionEvent e) {
            return super.onContextClick(e);
        }
    }

}
