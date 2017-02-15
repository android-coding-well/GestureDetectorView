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
    private RotateGestureDetector rotateGestureDetector;//旋转手势
    private OnGestureListener onGestureListener;//手势监听器

    private boolean isSlideGestureDetecteOn = true;//是否启用滑动手势识别
    private boolean isScaleGestureDetecteOn = true;//是否启用缩放手势识别
    private boolean isRotateGestureDetecteOn = true;//是否启用旋转手势识别

    private int minSlideDetecteDistance = 80;//最小滑动距离检测
    private float minScaleDetecteFactor = 0.2f;//最小缩放因子检测
    private int minRotateDetecteDegree = 10;//最小旋转角度检测

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


//*****************************对外接口start*****************************************

    /**
     * 设置手势监听器
     *
     * @param listener
     */
    public void setOnGestureListener(OnGestureListener listener) {
        onGestureListener = listener;
    }

    /**
     * 是否启用滑动手势识别,默认true
     *
     * @param isSlideGestureDetecteOn
     */
    public void isSlideGestureDetecteOn(boolean isSlideGestureDetecteOn) {
        this.isSlideGestureDetecteOn = isSlideGestureDetecteOn;
    }

    /**
     * 是否启用缩放手势识别,默认true
     *
     * @param isScaleGestureDetecteOn
     */
    public void isScaleGestureDetecteOn(boolean isScaleGestureDetecteOn) {
        this.isScaleGestureDetecteOn = isScaleGestureDetecteOn;
    }

    /**
     * 是否启用旋转手势识别,默认true
     *
     * @param isRotateGestureDetecteOn
     */
    public void isRotateGestureDetecteOn(boolean isRotateGestureDetecteOn) {
        this.isRotateGestureDetecteOn = isRotateGestureDetecteOn;
    }

    /**
     * 设置最小滑动检测距离，默认80
     * 即超过指定值才会触发识别
     *
     * @param minSlideDetecteDistance
     */
    public void setMinSlideDetecteDistance(int minSlideDetecteDistance) {
        this.minSlideDetecteDistance = minSlideDetecteDistance;
    }

    /**
     * 设置最小缩放检测因子(0-1之间)，默认0.2
     * 即超过指定值才会触发识别
     *
     * @param minScaleDetecteFactor
     */
    public void setMinScaleDetecteFactor(float minScaleDetecteFactor) {
        this.minScaleDetecteFactor = minScaleDetecteFactor;
    }

    /**
     * 设置最小旋转检测角度，默认10
     * 即超过指定值才会触发识别
     *
     * @param minRotateDetecteDegree
     */
    public void setMinRotateDetecteDegree(int minRotateDetecteDegree) {
        this.minRotateDetecteDegree = minRotateDetecteDegree;
    }

//*****************************对外接口end*******************************************


    boolean isSingle = true;//是否单个手指

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                isSingle = true;
                break;
            case MotionEvent.ACTION_UP:
                isSingle = true;
                break;
            case MotionEvent.ACTION_CANCEL:
                isSingle = true;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                isSingle = false;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
        }
        if (isRotateGestureDetecteOn) {
            rotateGestureDetector.onTouchEvent(event);
        }
        if (isScaleGestureDetecteOn) {
            scaleGestureDetector.onTouchEvent(event);
        }
        if (isSingle && isSlideGestureDetecteOn) {
            gestureDetector.onTouchEvent(event);
        }
        return true;
    }

    private void onGesture(int gesture, float dis) {
        if (onGestureListener != null) {
            onGestureListener.onGesture(gesture, dis);
        }
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
            if (degree > 180) {
                degree = degree - 360;
            }
            if (degree < -180) {
                degree = 360 + degree;
            }
            if (degree < -minRotateDetecteDegree) {
                Log.i(TAG, "顺时针旋转:" + (-degree));
                onGesture(GESTURE_ROTATE_CLOCKWISE, degree);
            }
            if (degree > minRotateDetecteDegree) {
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
            if (detector.getScaleFactor() < (1.0f - minScaleDetecteFactor)) {
                Log.i(TAG, "缩小:" + detector.getScaleFactor());
                onGesture(GESTURE_SCALE_ZOOMOUT, detector.getScaleFactor());
            }
            if (detector.getScaleFactor() > (1.0f + minScaleDetecteFactor)) {//放大
                Log.i(TAG, "放大:" + detector.getScaleFactor());
                onGesture(GESTURE_SCALE_ZOOMIN, detector.getScaleFactor());
            }

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
            int distance = (int) Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
            if (distance < 80) {//小于一定距离则忽略掉
                return true;
            }
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
            if (e.getPointerCount() == 1) {
                return true;//需要返回true 否则onFling不起作用
            }

            return false;
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
