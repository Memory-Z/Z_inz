package com.inz.z.base.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.TintTypedArray;
import android.util.AttributeSet;

import com.inz.z.base.R;
import com.inz.z.base.util.ImageUtils;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/17 16:34.
 */
public class RoundImageView extends AppCompatImageView {

    private static final String TAG = "RoundImageView";
    private Context mContext;

    private Paint imagePaint;
    /**
     * 绘图矩阵
     */
    private final RectF mDrawableRect = new RectF();
    private BitmapShader mBitmapShader;
    private Bitmap mBitmap;
    /**
     * 是否准备
     */
    private boolean mReady;
    /**
     * 是否设置完成
     */
    private boolean mSetupPending;
    private Path imagePath;
    private RectF topStartRect, topEndRect, bottomStartRect, bottomEndRect;

    /**
     * 圆角半径
     */
    private int roundRadius = 0;
    private int topStartRadius = 0, topEndRadius = 0, bottomStartRadius = 0, bottomEndRadius = 0;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initStyle(attrs);
        initPaint();
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

    @SuppressLint("RestrictedApi")
    private void initStyle(AttributeSet set) {
        TintTypedArray array = TintTypedArray.obtainStyledAttributes(mContext, set, R.styleable.RoundImageView, 0, 0);
        roundRadius = array.getDimensionPixelSize(R.styleable.RoundImageView_round_image_view_radius, 0);
        topStartRadius = array.getDimensionPixelSize(R.styleable.RoundImageView_round_image_view_top_start_radius, 0);
        topEndRadius = array.getDimensionPixelSize(R.styleable.RoundImageView_round_image_view_top_end_radius, 0);
        bottomStartRadius = array.getDimensionPixelSize(R.styleable.RoundImageView_round_image_view_bottom_start_radius, 0);
        bottomEndRadius = array.getDimensionPixelSize(R.styleable.RoundImageView_round_image_view_bottom_end_radius, 0);
        array.recycle();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mReady = true;

        imagePaint = new Paint();
        imagePaint.setStyle(Paint.Style.FILL);
        imagePaint.setStrokeWidth(2);

        imagePath = new Path();
        topStartRect = new RectF();
        topEndRect = new RectF();
        bottomStartRect = new RectF();
        bottomEndRect = new RectF();

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    private void initializeBitmap() {
        mBitmap = ImageUtils.getBitmapFromDrawable(getDrawable());
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
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        imagePaint.setShader(mBitmapShader);
        mDrawableRect.set(calculateBounds());


        // 更新画笔
        updateShaderMatrix(mDrawableRect, mBitmap.getWidth(), mBitmap.getHeight());
        invalidate();
    }

    /**
     * 计算 ImageView 范围 （获取矩形）
     *
     * @return 可用范围
     */
    private RectF calculateBounds() {
        int availableWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

//        // 以最短边为 基准
//        int sideLength = Math.min(availableWidth, availableHeight);
//        计算偏移
//        float left = getPaddingLeft() + (availableWidth - sideLength) / 2f;
//        float top = getPaddingTop() + (availableHeight - sideLength) / 2f;
        float left = getPaddingLeft();
        float top = getPaddingTop();
        // 设置为 矩形，
        return new RectF(left, top, left + availableWidth, top + availableHeight);
    }

    /**
     * 更新Shader 矩阵
     *
     * @param mDrawableRect 图形矩阵
     * @param mBitmapWidth  图形宽
     * @param mBitmapHeight 图形高
     */
    private void updateShaderMatrix(RectF mDrawableRect, int mBitmapWidth, int mBitmapHeight) {
        float scale;
        float dx = 0;
        float dy = 0;

        Matrix mBitmapMatrix = new Matrix();

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }
        mBitmapMatrix.setScale(scale, scale);
        mBitmapMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left, (int) (dy + 0.5f) + mDrawableRect.top);
        mBitmapShader.setLocalMatrix(mBitmapMatrix);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (roundRadius != 0) {
            canvas.drawRoundRect(mDrawableRect, roundRadius, roundRadius, imagePaint);
        } else if (topStartRadius != 0 || topEndRadius != 0 || bottomStartRadius != 0 || bottomEndRadius != 0) {
            float left = mDrawableRect.left;
            float right = mDrawableRect.right;
            float top = mDrawableRect.top;
            float bottom = mDrawableRect.bottom;
            imagePath.moveTo(left + topStartRadius, top);
            imagePath.lineTo(right - topEndRadius, top);

            topEndRect.set(right - topEndRadius * 2 + .5F, top, right, top + topEndRadius * 2);
            imagePath.arcTo(topEndRect, -90, 90);
            imagePath.lineTo(right, bottom - bottomEndRadius);

            bottomEndRect.set(right - bottomEndRadius * 2, bottom - bottomEndRadius * 2, right, bottom);
            imagePath.arcTo(bottomEndRect, 0, 90);
            imagePath.lineTo(left + bottomStartRadius, bottom);

            bottomStartRect.set(left, bottom - bottomStartRadius * 2, left + bottomStartRadius * 2, bottom);
            imagePath.arcTo(bottomStartRect, 90, 90);
            imagePath.lineTo(left, top + topStartRadius);

            topStartRect.set(left, top, left + topStartRadius * 2, top + topStartRadius * 2);
            imagePath.arcTo(topStartRect, 180, 90);
            imagePath.close();

            canvas.drawPath(imagePath, imagePaint);
        } else {
            canvas.drawRect(mDrawableRect, imagePaint);
        }
    }
}
