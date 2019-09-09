package com.inz.z.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;

import androidx.appcompat.widget.TintTypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.inz.z.other_module.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 编辑界面
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/05 11:42.
 */
public class EditSurfaceView extends SurfaceView implements SurfaceHolder.Callback2, Runnable {
    private static final String TAG = "EditSurfaceView";
    private Context mContext;
    private SurfaceHolder mSurfaceHolder;
    private Path mPath;
    /**
     * 历史路径
     */
    private List<Path> historyPath;
    /**
     * 绘制步数
     */
    private long drawStep = 0;
    private Paint drawPaint;
    private Paint mPaint;

    /**
     * 绘制区域大小
     */
    private Size mSize;

    /**
     * 画笔宽度
     */
    private int paintWidth = 8;
    private ScheduledExecutorService singleThreadExecutor;
    /**
     * 能否绘制。当处于多点触控时，禁止绘制 {@see #onTouchEvent }
     */
    private boolean canDrawable = false;


    public EditSurfaceView(Context context) {
        this(context, null);
    }

    public EditSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(attrs);
        initView();
        initData();
    }

    @SuppressLint("RestrictedApi")
    private void initAttrs(AttributeSet attributeSet) {
        TintTypedArray array = TintTypedArray.obtainStyledAttributes(mContext, attributeSet,
                R.styleable.EditSurfaceView, 0, 0);
        array.recycle();
    }

    private void initView() {

    }

    private void initData() {
        drawStep = 0;
        paintWidth = 8;
        historyPath = new ArrayList<>();

//        this.setZOrderOnTop(true);
        this.setFocusable(true);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        mEditPoint = null;
        mPath = null;
        mSize = null;
        canDrawable = false;

        drawPaint = new Paint();
        drawPaint.setAntiAlias(true);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeWidth(paintWidth);
        drawPaint.setColor(Color.RED);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(48);

        this.setSecure(true);
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
//        this.setLayerType(View.LAYER_TYPE_HARDWARE, mPaint);
//        this.setLayerType(View.LAYER_TYPE_HARDWARE, drawPaint);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GREEN);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Canvas canvas = mSurfaceHolder.lockCanvas();
        if (canvas != null) {
            canvas.drawColor(Color.GREEN);
            mSurfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private EditPoint mEditPoint;

    private static class EditPoint {
        float mX;
        float mY;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        event.getActionIndex();
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mEditPoint == null) {
                    mEditPoint = new EditPoint();
                }
                mEditPoint.mX = x;
                mEditPoint.mY = y;
                if (mPath == null) {
                    mPath = new Path();
                }
                canDrawable = true;
                mPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                canDrawable = true;
                if (mPath != null) {
                    mPath.quadTo(mEditPoint.mX, mEditPoint.mY, (mEditPoint.mX + x) / 2, (mEditPoint.mY + y) / 2);
//                    mPath.lineTo(mEditPoint.mX, mEditPoint.mY);
                }
                mEditPoint.mX = x;
                mEditPoint.mY = y;
                break;
            case MotionEvent.ACTION_UP:
                historyPath.add(new Path(mPath));
                mPath.reset();
                canDrawable = false;
//                mPath = null;
//                mEditPoint = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                int pointIndex = event.getActionIndex();
                float pointX = event.getX(pointIndex);
                float pointY = event.getY(pointIndex);
                // 非第一个点击时, 设置不可绘制
                canDrawable = false;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                int pointUpIndex = event.getActionIndex();
                float pointUpX = event.getX(pointUpIndex);
                float pointUpY = event.getY(pointUpIndex);
                // 非第一个点击时, 设置不可绘制
                canDrawable = false;
                break;
        }
        return true;
    }

    private enum DrawStatus {

    }
//
//    private static final int EDIT_DRAW_DOWN = 0x1101;
//    private static final int EDIT_DRAW_MOVE = 0x1102;
//    private static final int EDIT_DRAW_UP = 0x1103;
//    private static final int EDIT_POINTER_DOWN = 0x1104;
//    private static final int EDIT_POINTER_MOVE = 0x1105;
//    private static final int EDIT_POINTER_UP = 0x1106;

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated: ");
        this.mSurfaceHolder = holder;
        singleThreadExecutor = Executors.newScheduledThreadPool(1);
        singleThreadExecutor.scheduleWithFixedDelay(this, 200, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged: ");
        this.mSurfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed: ");
        this.mSurfaceHolder = holder;
        if (singleThreadExecutor != null) {
            singleThreadExecutor.shutdown();
            singleThreadExecutor = null;
        }
    }

    private int count = 0;

    @Override
    public void run() {
        Log.i(TAG, "run: " + System.currentTimeMillis());
        while (canDrawable && mSurfaceHolder != null) {
            Canvas surfaceCanvas = mSurfaceHolder.lockCanvas();
//                    mSurfaceHolder.getSurface().lockCanvas(mSurfaceHolder.getSurfaceFrame());
//            Log.i(TAG, "run: Path = " + mPath.toString());
            if (mPath != null && surfaceCanvas != null && surfaceCanvas.getSaveCount() != 0) {
//                surfaceCanvas.drawColor(Color.RED);
                surfaceCanvas.drawPath(mPath, drawPaint);
                count++;
//                surfaceCanvas.drawText("" + count, 100, 50 * count, mPaint);
//                List<Path> oldPaths;
//                if (historyPath.size() > 3) {
//                    oldPaths = historyPath.subList(historyPath.size() - 4, historyPath.size() - 1);
//                } else {
//                    oldPaths = historyPath;
//                }
                Log.i(TAG, "run: " + historyPath.size());
//                for (Path path : historyPath) {
//                    surfaceCanvas.drawPath(path, drawPaint);
//                }
//                oldPaths.clear();
//                mSurfaceHolder.getSurface().unlockCanvasAndPost(surfaceCanvas);
                mSurfaceHolder.unlockCanvasAndPost(surfaceCanvas);
            }
        }
    }
}
