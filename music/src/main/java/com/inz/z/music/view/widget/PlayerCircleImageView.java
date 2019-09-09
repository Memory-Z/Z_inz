package com.inz.z.music.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.TintTypedArray;
import android.util.AttributeSet;

import com.inz.z.music.R;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/10 09:41.
 */
public class PlayerCircleImageView extends AppCompatImageView {

    public static final float OUTER_RING_MARGIN_INNER = 8F;
    public static final String OUTER_DEF_COLOR = "#4EF0FFF0";
    private Context mContext;
    private Paint mBitmapPaint;
    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;

    private Paint minPaint;
    private Bitmap minBitmap;

    private Matrix mBitmapMatrix;
    private int radius;
    private int outerRingSize = 0;

    private Paint outerPaint;
    private Bitmap outerBackBitmap;
    private BitmapShader outerBitmapShader;
    /**
     * 小环大小模式
     */
    private int minRingSize;
    /**
     * 外环到内环间距
     */
    private int outerRingMarinInner;
    /**
     * 是否准备
     */
    private boolean mReady;
    /**
     * 是否设置完成
     */
    private boolean mSetupPending;

    /**
     * 绘图矩阵
     */
    private final RectF mDrawableRect = new RectF();
    private final RectF mMinDrawableRect = new RectF();
    /**
     * 绘图半径
     */
    private float mDrawableRadius;
    /**
     * 小圆半径
     */
    private float minRingRadius;

    public PlayerCircleImageView(Context context) {
        this(context, null);
    }

    public PlayerCircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayerCircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(attrs);
        initParameter();
    }

    @SuppressLint("RestrictedApi")
    private void initView(AttributeSet attrs) {
        TintTypedArray array = TintTypedArray.obtainStyledAttributes(mContext, attrs,
                R.styleable.PlayerCircleImageView, 0, 0);
        outerRingSize = array.getDimensionPixelSize(R.styleable.PlayerCircleImageView_player_civ_outer_width, 4);
        if (array.hasValue(R.styleable.PlayerCircleImageView_player_civ_outer_background)) {
            Drawable outerBackground = array.getDrawable(R.styleable.PlayerCircleImageView_player_civ_outer_background);
            outerBackBitmap = getBitmapFromDrawable(outerBackground);
        }
        minRingSize = array.getInt(R.styleable.PlayerCircleImageView_player_civ_min_size_mode, 2);

        if (array.hasValue(R.styleable.PlayerCircleImageView_player_civ_outer_min_photo)) {
            Drawable drawable = array.getDrawable(R.styleable.PlayerCircleImageView_player_civ_outer_min_photo);
            minBitmap = getBitmapFromDrawable(drawable);
        }

        radius = array.getDimensionPixelSize(R.styleable.PlayerCircleImageView_player_civ_radius, 0);

        outerRingMarinInner = array.getDimensionPixelSize(R.styleable.PlayerCircleImageView_player_civ_outer_margin, 0);

        array.recycle();
    }

    /**
     * 初始化参数
     */
    private void initParameter() {
        mReady = true;

        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setStyle(Paint.Style.FILL);

        outerPaint = new Paint();
        outerPaint.setAntiAlias(true);
        outerPaint.setStyle(Paint.Style.STROKE);
        outerPaint.setStrokeWidth(4);
        outerPaint.setColor(Color.parseColor(OUTER_DEF_COLOR));

        minPaint = new Paint();
        minPaint.setAntiAlias(true);
        minPaint.setStyle(Paint.Style.FILL);

        mBitmapMatrix = new Matrix();

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        if (mBitmap == null) {
            return;
        }
        float bigRingRadius = mDrawableRadius - outerRingSize - OUTER_RING_MARGIN_INNER - outerRingMarinInner;
        canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), bigRingRadius, mBitmapPaint);


        outerPaint.setStrokeWidth(outerRingSize);
        float outDf = (float) outerRingSize / 2;
        canvas.drawArc(
                mDrawableRect.left + outDf,
                mDrawableRect.top + outDf,
                mDrawableRect.right - outDf,
                mDrawableRect.bottom - outDf,
                -90,
                360,
                false,
                outerPaint
        );

        if (minBitmap != null) {
            canvas.drawCircle(
                    mMinDrawableRect.centerX() - OUTER_RING_MARGIN_INNER - outDf,
                    mMinDrawableRect.centerY() - OUTER_RING_MARGIN_INNER - outDf,
                    minRingRadius,
                    minPaint
            );
        }

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
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }

    private void initializeBitmap() {
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    public void setMinImageBitmap(Bitmap bitmap) {
        minBitmap = bitmap;
        setup();
    }

    public void setMinImageDrawable(Drawable drawable) {
        initializeMinBitmap(drawable);
    }

    public void setMinImageResource(@DrawableRes int resId) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
        initializeMinBitmap(drawable);
    }

    private void initializeMinBitmap(Drawable drawable) {
        minBitmap = getBitmapFromDrawable(drawable);
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
        mBitmapPaint.setShader(mBitmapShader);

        // 获取显示 矩形矩阵
        mDrawableRect.set(calculateBounds());

//        SweepGradient sweepGradient = null;
//        if (innerRadius > 0) {
//            sweepGradient = new SweepGradient(mDrawableRect.centerX(), mDrawableRect.centerY(), Color.BLACK, Color.BLACK);
//            Matrix matrix = new Matrix();
//            matrix.setScale(innerRadius, innerRadius);
//            sweepGradient.setLocalMatrix(matrix);
//        }
//        if (sweepGradient != null) {
//            mComposeShader = new ComposeShader(mBitmapShader, sweepGradient, PorterDuff.Mode.DST_OUT);
////            mBitmapPaint.setShader(mComposeShader);
//        } else {
//            mComposeShader = null;
//        }

        // 获取能设置的最大半径
        mDrawableRadius = Math.min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f);
        if (radius != 0) {
            mDrawableRadius = Math.min(mDrawableRadius, radius);
        }

        BitmapShader minBitmapShader;
        if (minBitmap != null) {

            minRingRadius = mDrawableRadius * ((float) minRingSize / 4) * .5F;
            minBitmapShader = new BitmapShader(minBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            // 获取坐标点
            double dfw = Math.sqrt(mDrawableRadius * mDrawableRadius / 2);
            RectF minRect = new RectF(
                    mDrawableRect.centerX() + (int) (dfw - minRingRadius),
                    mDrawableRect.centerY() + (int) (dfw - minRingRadius),
                    mDrawableRect.centerX() + (int) (dfw + minRingRadius),
                    mDrawableRect.centerY() + (int) (dfw + minRingRadius)
            );
            mMinDrawableRect.set(minRect);
            minPaint.setShader(minBitmapShader);
            // 更新小图Shader
            updateMinShaderMatrix(minBitmap.getWidth(), minBitmap.getHeight(), minBitmapShader);
        }

        if (outerBackBitmap != null) {
            outerBitmapShader = new BitmapShader(outerBackBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            outerPaint.setShader(outerBitmapShader);
            updateOuterShaderMatrix(mDrawableRect, outerBackBitmap.getWidth(), outerBackBitmap.getHeight());
        }

        // 更新画笔
        updateShaderMatrix(mDrawableRect, mBitmap.getWidth(), mBitmap.getHeight());
        invalidate();
    }

    /**
     * 计算 ImageView 范围
     *
     * @return 可用范围
     */
    private RectF calculateBounds() {
        int availableWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        // 以最短边为 基准
        int sideLength = Math.min(availableWidth, availableHeight);

        float left = getPaddingLeft() + (availableWidth - sideLength) / 2f;
        float top = getPaddingTop() + (availableHeight - sideLength) / 2f;
        // 设置为 矩形，
        return new RectF(left, top, left + sideLength, top + sideLength);
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

        mBitmapMatrix.set(null);

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

    /**
     * 更新Shader 矩阵
     *
     * @param mDrawableRect 图形矩阵
     * @param mBitmapWidth  图形宽
     * @param mBitmapHeight 图形高
     */
    private void updateOuterShaderMatrix(RectF mDrawableRect, int mBitmapWidth, int mBitmapHeight) {
        float scale;
        float dx = 0;
        float dy = 0;

        Matrix outerMatrix = new Matrix();

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }
        outerMatrix.setScale(scale, scale);
        outerMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left, (int) (dy + 0.5f) + mDrawableRect.top);
        if (outerBitmapShader != null) {
            outerBitmapShader.setLocalMatrix(outerMatrix);
        }
    }

    /**
     * 更新Min Shader 矩阵
     *
     * @param mBitmapWidth  图形宽
     * @param mBitmapHeight 图形高
     */
    private void updateMinShaderMatrix(int mBitmapWidth, int mBitmapHeight, BitmapShader mBitmapShader) {
        float scale;
        float dx = 0;
        float dy = 0;
        Matrix matrix = new Matrix();

        if (mBitmapWidth * mMinDrawableRect.height() > mMinDrawableRect.width() * mBitmapHeight) {
            scale = mMinDrawableRect.height() / (float) mBitmapHeight;
            dx = (mMinDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mMinDrawableRect.width() / (float) mBitmapWidth;
            dy = (mMinDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }
        matrix.setScale(scale, scale);
        matrix.postTranslate((int) (dx + 0.5f) + mMinDrawableRect.left, (int) (dy + 0.5f) + mMinDrawableRect.top);
        mBitmapShader.setLocalMatrix(matrix);
    }

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


}
