package com.junmeng.gdv;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.junmeng.gdv.detector.RotateGestureDetector;

/**
 * 手势识别器(支持八方位和缩放旋转手势)
 * Created by HWJ on 2016/11/25.
 */

public class GestureDetectorView extends View {
    private static final String TAG = "GestureDetectorView";

    public interface OnGestureListener {
        /**
         * @param gesture 手势
         * @param factor  缩放手势表示缩放因子，滑动手势表示速率，旋转手势表示角度
         */
        void onGesture(int gesture, float factor);
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
    public static final int GESTURE_ROTATE_CLOCKWISE = 11;//顺时针
    public static final int GESTURE_ROTATE_ANTICLOCKWISE = 12;


    private GestureDetector gestureDetector;//一般手势
    private ScaleGestureDetector scaleGestureDetector;//缩放手势
    RotateGestureDetector rotateGestureDetector;
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
        rotateGestureDetector = new RotateGestureDetector(context, new MyRotateGestureListener());
    }

    /**
     * 设置手势监听器
     *
     * @param listener
     */
    public void setOnGestureListener(OnGestureListener listener) {
        onGestureListener = listener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        scaleGestureDetector.onTouchEvent(event);
        rotateGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return true;
    }

    private class MyRotateGestureListener extends RotateGestureDetector.SimpleOnRotateGestureListener {

        @Override
        public boolean onRotate(RotateGestureDetector detector) {
            return false;
        }

        @Override
        public boolean onRotateBegin(RotateGestureDetector detector) {
            return super.onRotateBegin(detector);
        }

        @Override
        public void onRotateEnd(RotateGestureDetector detector) {
            float degree = detector.getRotationDegreesDelta();
            if (degree < -10) {
                Log.i(TAG, "顺时针旋转:" + (-degree));
                onGesture(GESTURE_ROTATE_CLOCKWISE, degree);
            }
            if (degree > 10) {
                Log.i(TAG, "逆时针旋转:" + degree);
                onGesture(GESTURE_ROTATE_ANTICLOCKWISE, degree);
            }

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
            if (detector.getScaleFactor() < 0.8) {
                onGesture(GESTURE_SCALE_ZOOMOUT, detector.getScaleFactor());
            }
            if (detector.getScaleFactor() > 1.2) {//放大
                onGesture(GESTURE_SCALE_ZOOMIN, detector.getScaleFactor());
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
