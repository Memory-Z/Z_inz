package com.inz.z.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.inz.z.base.util.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/08 15:12.
 */
public class EditImageView extends AppCompatImageView {

    private static final String TAG = "EditImageView";

    private Context mContext;
    private List<Path> historyPathList;
    private Path mPath;
    private Paint drawPaint;
    private float drawPaintWidth = 8f;
    private int drawPaintColor = Color.RED;
    private RectF drawRectF;
    private Bitmap mBitmap;
    private float drawScale = 1.0f;

    private RectF canDrawRectF;
    private Matrix bitmapPathMatrix;

    /**
     * 旧路径条数，用于保存后，再次编辑进行撤销操作
     */
    private int oldPathCount = 0;

    /**
     * 是否可以启用绘制
     */
    private boolean isCanDrawable = false;

    /**
     * 是否准备
     */
    private boolean mReady;
    /**
     * 是否设置完成
     */
    private boolean mSetupPending;

    /**
     * 多点触控时
     */
    private boolean canDrawable = false;
    private boolean isManyTouch = false;

    private EditImagePoint editImagePoint;

    private class EditImagePoint {
        float x, y;
    }

    public EditImageView(Context context) {
        this(context, null);
    }

    public EditImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {
        mReady = true;

        mPath = null;
        isManyTouch = false;
        canDrawable = false;
        historyPathList = new ArrayList<>();

        drawPaintWidth = 8;
        drawPaintColor = Color.RED;

        drawPaint = new Paint();
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        drawPaint.setColor(drawPaintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeWidth(drawPaintWidth);

        drawRectF = new RectF();
        canDrawRectF = new RectF();

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        // 拦截设置类型
        super.setScaleType(ScaleType.CENTER_INSIDE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        setup();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        setup();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }

    private void initializeBitmap() {
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }
        if (getWidth() == 0 && getHeight() == 0) {
            return;
        }
        if (mBitmap == null) {
            invalidate();
            return;
        }

        drawRectF = calculateBounds();
        float dw = drawRectF.width();
        float dh = drawRectF.height();

        float bw = mBitmap.getWidth();
        float bh = mBitmap.getHeight();

        float scale;
        // 两测边距
        float dx = 0, dy = 0;
        // 判断ImageView 长宽比和bitmap 长宽比
        if (bw * dh > bh * dw) {
            // 以宽为准
            scale = dw / bw;
            dy = (dh - bh * scale) * .5f;
        } else {
            // 以长为准
            scale = dh / bh;
            dx = (dw - bw * scale) * .5f;
        }

        canDrawRectF = new RectF(dx, dy, dw - dx, dh - dy);
        drawScale = 1 / scale;
        bitmapPathMatrix = new Matrix();
        bitmapPathMatrix.setScale(drawScale, drawScale);
        bitmapPathMatrix.postTranslate(-dx * drawScale, -dy * drawScale);
    }

    /**
     * 计算 ImageView 范围
     *
     * @return 可用范围
     */
    private RectF calculateBounds() {
        int availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        float left = getPaddingLeft();
        float top = getPaddingTop();
//        // 以最短边为 基准
//        int sideLength = Math.min(availableWidth, availableHeight);
//
//        float left = getPaddingLeft() + (availableWidth - sideLength) / 2f;
//        float top = getPaddingTop() + (availableHeight - sideLength) / 2f;
        // 设置为 矩形，
        return new RectF(0, 0, left + availableWidth, top + availableHeight);
    }

    /**
     * 获取Bitmap
     *
     * @param drawable 资源
     * @return Bitmap 对象
     */
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;
            Bitmap.Config config = Bitmap.Config.ARGB_8888;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(2, 2, config);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), config);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if (drawBitmap != null) {
//            canvas.setBitmap(drawBitmap);
//        }
        Log.i(TAG, "onDraw: " + canDrawRectF.width() + " : " + getWidth() + " -- " + canDrawRectF.height() + " : " + getHeight());

        setPaintProperty(1.0f);
        canvas.clipRect(canDrawRectF);

        if (pointerMap != null && pointerMap.size() == 1) {
            if (canDrawable && !isManyTouch && mPath != null) {
                canvas.drawPath(mPath, drawPaint);
            }
        }
        if (historyPathList != null) {
            for (Path path : historyPathList) {
                canvas.drawPath(path, drawPaint);
            }
        }

    }

    /**
     * 设置画笔属性
     */
    private void setPaintProperty(float scale) {
        drawPaint.setStrokeWidth(drawPaintWidth * scale);
        drawPaint.setColor(drawPaintColor);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private Map<Float, EditImagePoint> pointerMap;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 判断是否启用绘制
        if (!isCanDrawable) {
            if (!isSavePath) {
                savePath();
                postInvalidate();
            }
            // 不可绘制
            return super.onTouchEvent(event);
        }

        if (pointerMap == null) {
            pointerMap = new ArrayMap<>(6);
        }
        float x = event.getX();
        float y = event.getY();
        int actionMask = event.getActionMasked();
        float actionIndex = event.getActionIndex();
        EditImagePoint imagePoint = pointerMap.get(actionIndex);

        StringBuilder sb = new StringBuilder();
        for (Float index : pointerMap.keySet()) {
            sb.append(index).append(" -- ");
        }

        switch (actionMask) {
            case MotionEvent.ACTION_DOWN:
                canDrawable = true;
                if (mPath == null) {
                    mPath = new Path();
                }
                mPath.moveTo(x, y);
                if (editImagePoint == null) {
                    editImagePoint = new EditImagePoint();
                }
                editImagePoint.x = x;
                editImagePoint.y = y;
                if (imagePoint == null) {
                    imagePoint = new EditImagePoint();
                }
                if (pointerMap.size() != 0) {
                    pointerMap.clear();
                }
                imagePoint.x = x;
                imagePoint.y = y;
                pointerMap.put(actionIndex, imagePoint);
                Log.i(TAG, "onTouchEvent: Down: " + sb.toString());
                break;
            case MotionEvent.ACTION_MOVE:
                canDrawable = true;
                if (mPath != null) {
                    mPath.quadTo(editImagePoint.x, editImagePoint.y, (editImagePoint.x + x) / 2, (editImagePoint.y + y) / 2);
                }
                editImagePoint.x = x;
                editImagePoint.y = y;
                if (imagePoint == null) {
                    imagePoint = new EditImagePoint();
                }
                imagePoint.x = x;
                imagePoint.y = y;
                pointerMap.put(actionIndex, imagePoint);
                Log.i(TAG, "onTouchEvent: Move: " + sb.toString());
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                canDrawable = false;
                if (mPath != null && mPath.isEmpty()) {
                    Log.i(TAG, "onTouchEvent: performClick: ");
                    performClick();
                    break;
                }
                if (!isManyTouch) {
                    if (historyPathList == null) {
                        historyPathList = new ArrayList<>();
                    }
                    editImagePoint.x = x;
                    editImagePoint.y = y;
                    savePath();
                } else {
                    isManyTouch = false;
                }
                if (event.getPointerCount() == 0) {
                    pointerMap.clear();
                }
                Log.i(TAG, "onTouchEvent: UP: " + sb.toString());
                postInvalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                isManyTouch = true;
                canDrawable = false;
                if (historyPathList == null) {
                    historyPathList = new ArrayList<>();
                }
                savePath();
                // 将路径至null 防止多点触控时出现异常
                mPath = null;
                editImagePoint.x = x;
                editImagePoint.y = y;
                if (imagePoint == null) {
                    imagePoint = new EditImagePoint();
                }
                imagePoint.x = x;
                imagePoint.y = y;
                pointerMap.put(actionIndex, imagePoint);
                Log.i(TAG, "onTouchEvent: POINTER_down: " + sb.toString());

                postInvalidate();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (imagePoint != null) {
                    pointerMap.remove(actionIndex);
                }
                Log.i(TAG, "onTouchEvent: Pointer_ UP: " + sb.toString());
                postInvalidate();
                break;
        }
        return true;
    }

    /**
     * 是否保存地址
     */
    private boolean isSavePath = false;

    /**
     * 保存路径
     */
    private void savePath() {
        if (mPath != null) {
            historyPathList.add(new Path(mPath));
            isSavePath = true;
            mPath.reset();
        }
    }

    /**
     * 设置画笔大小
     *
     * @param paintSize 画笔大小
     */
    public void setPaintSize(float paintSize) {
        this.drawPaintWidth = paintSize;
    }

    /**
     * 设置画笔颜色
     *
     * @param color 颜色
     */
    public void setPaintColor(int color) {
        this.drawPaintColor = color;
    }

    /**
     * 获取Bitmap，截取View
     *
     * @return bitmap
     * @see #getBitmap4Canvas() 获取对图片的修改
     */
    public Bitmap getBitmap() {
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);
        return bitmap;
    }

    /**
     * 获取原图改变
     *
     * @return Bitmap 原图大小
     */
    public Bitmap getBitmap4Canvas() {
        Bitmap saveBitmap;
        if (mBitmap != null) {
            // copy 原bitmap ,在其上方绘制路径
            saveBitmap = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
            Canvas saveCanvas = new Canvas(saveBitmap);
            // 保存时，不保存正在绘制的路径
            setPaintProperty(drawScale);
            if (historyPathList != null) {
                for (Path path : historyPathList) {
                    Path path1 = new Path(path);
                    // 对路径进行缩放平移操作
                    path1.transform(bitmapPathMatrix);
                    saveCanvas.drawPath(path1, drawPaint);
                }
                oldPathCount = historyPathList.size();
            }
        } else {
            // 获取截图
            saveBitmap = getBitmap();
        }
        return saveBitmap;
    }

    /**
     * 获取原始图像
     *
     * @return bitmap
     */
    public Bitmap getOriginalBitmap() {
        return mBitmap;
    }

    public String saveBitmapToPic(Bitmap bitmap) {
        String imagePath = null;
        if (bitmap == null) {
            return null;
        }
        File picFile = new File(FileUtils.getCacheImagePath(mContext) + File.separatorChar + System.currentTimeMillis() + ".jpeg");
        if (!picFile.getParentFile().exists()) {
            picFile.getParentFile().mkdirs();
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(picFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            imagePath = picFile.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "saveBitmapToPic: " + picFile.getPath());
        return imagePath;
    }

    /**
     * 开始绘制
     */
    public void startDraw() {
        Log.i(TAG, "startDraw: ");
        isCanDrawable = true;
    }

    /**
     * 停止绘制
     */
    public void stopDraw() {
        Log.i(TAG, "stopDraw: ");
        isCanDrawable = false;
    }

    /**
     * 撤回上一步
     */
    public void recallPath() {
        if (historyPathList != null && historyPathList.size() > oldPathCount) {
            historyPathList.remove(historyPathList.size() - 1);
        }
        postInvalidate();
    }

    /**
     * 重置
     */
    public void reset() {
        if (historyPathList != null && oldPathCount >= 0) {
            historyPathList = historyPathList.subList(0, oldPathCount);
        }
        if (mPath != null) {
            mPath = null;
        }
        postInvalidate();
    }


}
