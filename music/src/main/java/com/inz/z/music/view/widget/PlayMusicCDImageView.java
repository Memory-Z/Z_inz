package com.inz.z.music.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.TintTypedArray;
import android.util.AttributeSet;

import com.inz.z.music.R;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/10 14:21.
 */
public class PlayMusicCDImageView extends AppCompatImageView {

    public static final String OUTER_DEF_COLOR = "#4EF0FFF0";

    private Context mContext;
    /**
     * 图片画笔
     */
    private Paint mBitmapPaint;
    /**
     * 外边缘画笔
     */
    private Paint mCDOuterPaint;
    /**
     * 内边缘画笔
     */
    private Paint mInnerPaint;
    /**
     * CD 图片
     */
    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private ComposeShader mComposeShader;
    //    private int mBitmapWidth, mBitmapHeight;
    private Matrix mBitmapMatrix;
    /**
     * 外边缘背景
     */
    private Bitmap mOuterBitmap;

    private int radius;
    private int innerRadius = 0;
    /**
     * 旋转速度：1：慢，2：普通；3：快
     */
    private int rotateSpeedMode = 2;
    private Drawable backgroundDraw;
    private int outerColor;

    /**
     * 是否准备
     */
    private boolean mReady;
    /**
     * 是否设置完成
     */
    private boolean mSetupPending;

    private final RectF mDrawableRect = new RectF();
    private float mDrawableRadius;

    private int outerWidth;

    private float rotateAngle = 0F;
    /**
     * 是否旋转
     */
    private boolean needRotate = true;

    public PlayMusicCDImageView(Context context) {
        this(context, null);
    }

    public PlayMusicCDImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayMusicCDImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(attrs);
        initParameter();
    }

    /**
     * 初始化布局
     */
    @SuppressLint("RestrictedApi")
    private void initView(AttributeSet attrs) {
        TintTypedArray array = TintTypedArray.obtainStyledAttributes(mContext, attrs,
                R.styleable.PlayMusicCDImageView, 0, 0);
        radius = array.getDimensionPixelSize(R.styleable.PlayMusicCDImageView_cd_iv_radius, 0);
        rotateSpeedMode = array.getInt(R.styleable.PlayMusicCDImageView_cd_iv_rotate_speed, 2);
        outerWidth = array.getDimensionPixelSize(R.styleable.PlayMusicCDImageView_cd_iv_outer_width, 10);
        if (array.hasValue(R.styleable.PlayMusicCDImageView_cd_iv_background)) {
            backgroundDraw = array.getDrawable(R.styleable.PlayMusicCDImageView_cd_iv_background);
        }
        outerColor = array.getColor(R.styleable.PlayMusicCDImageView_cd_iv_outer_color, Color.parseColor(OUTER_DEF_COLOR));
        innerRadius = array.getDimensionPixelSize(R.styleable.PlayMusicCDImageView_cd_iv_inner_radius, 0);

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

        mInnerPaint = new Paint();
        mInnerPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setStrokeWidth(8);
        mInnerPaint.setColor(outerColor);

        mCDOuterPaint = new Paint();
        mCDOuterPaint.setAntiAlias(true);
        mCDOuterPaint.setStyle(Paint.Style.STROKE);
        mCDOuterPaint.setStrokeWidth(outerWidth);
        mCDOuterPaint.setColor(outerColor);

        mBitmapMatrix = new Matrix();

        if (mSetupPending) {
            setup();
            mSetupPending = false;
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
        int availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

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
        if (mBitmap == null) {
            return;
        }
        canvas.save();
        canvas.rotate(rotateAngle, mDrawableRect.centerX(), mDrawableRect.centerY());
        float l = mDrawableRect.left;
        float t = mDrawableRect.top;
        float r = mDrawableRect.right;
        float b = mDrawableRect.bottom;
        int difW = outerWidth / 2;
        if (innerRadius > 0) {
            float w = mDrawableRadius - outerWidth - innerRadius;
            float bw = w / 2;
            mBitmapPaint.setStyle(Paint.Style.STROKE);
            mBitmapPaint.setStrokeWidth(w);
            canvas.drawArc(
                    l + outerWidth + bw,
                    t + outerWidth + bw,
                    r - outerWidth - bw,
                    b - outerWidth - bw,
                    -90,
                    360,
                    false,
                    mBitmapPaint
            );
            mInnerPaint.setStrokeWidth(2);
            mInnerPaint.setColor(Color.WHITE);
            canvas.drawArc(
                    l + w,
                    t + w,
                    r - w,
                    b - w,
                    -90,
                    360,
                    false,
                    mInnerPaint
            );
            mInnerPaint.setStrokeWidth(outerWidth);
            mInnerPaint.setColor(outerColor);
            canvas.drawArc(
                    l + w + outerWidth + difW,
                    t + w + outerWidth + difW,
                    r - w - outerWidth - difW,
                    b - w - outerWidth - difW,
                    -90,
                    360,
                    false,
                    mInnerPaint
            );
        } else {
            mBitmapPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius - outerWidth, mBitmapPaint);
        }

//        canvas.drawArc(mDrawableRect, -90, 360, false, mCDOuterPaint);
        canvas.drawArc(
                mDrawableRect.left + difW,
                mDrawableRect.top + difW,
                mDrawableRect.right - difW,
                mDrawableRect.bottom - difW,
                -90, 360, false, mCDOuterPaint);
        canvas.restore();
        if (needRotate) {
            mHandler.sendEmptyMessageAtTime(0, 500);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        if (mBitmap != null) {
//            Bitmap blurBitmap = GaussianBlurHelper.blur(mContext, mBitmap, .4F, true);
//            BitmapDrawable drawable = new BitmapDrawable(getResources(), blurBitmap);
//            setBackgroundDrawable(drawable);
//        }

        if (backgroundDraw != null) {
//            Bitmap background = getBitmapFromDrawable(backgroundDraw);
//            if (background != null) {
//                background = GaussianBlurHelper.blur(getContext(), background, 20, true);
//                BitmapDrawable drawable = new BitmapDrawable(getResources(), background);
//                setBackgroundDrawable(drawable);
//            } else {
//            }
            setBackgroundDrawable(backgroundDraw);
        }
    }

    private Handler mHandler = new Handler(new CDImageViewHandlerCallback());


    private class CDImageViewHandlerCallback implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            if (needRotate) {
                rotateAngle = rotateAngle % 360 + (float) rotateSpeedMode / 20;
            } else {
                rotateAngle = 0;
            }
            invalidate();
            return false;
        }
    }


    /**
     * 开始旋转
     */
    public void startRotate() {
        needRotate = true;
        invalidate();
    }

    /**
     * 停止旋转
     */
    public void stopRotate() {
        needRotate = false;
        invalidate();
    }


}
