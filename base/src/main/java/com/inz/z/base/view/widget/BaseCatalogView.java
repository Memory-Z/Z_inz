package com.inz.z.base.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * 自定义 目录 View
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/09/12 09:35.
 */
public class BaseCatalogView extends View {
    private static final String TAG = "BaseCatalogView";
    private Context mContext;

    /**
     * 文字
     */
    private Paint textPaint;
    /**
     * 图标
     */
    private Paint iconPaint;
    /**
     * 文字颜色
     */
    private int textColorInt;
    /**
     * 显示文字
     */
    private String text = "删除";
    /**
     * 文字大小
     */
    private int textSize = 18;
    private Rect textRect = new Rect();

    /**
     * 显示类型
     */
    private ShowType showType = ShowType.ONLY_TEXT;

    public enum ShowType {
        ONLY_TEXT,
        ONLY_ICON,
        TEXT_AND_ICON
    }


    public BaseCatalogView(Context context) {
        this(context, null);
    }

    public BaseCatalogView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseCatalogView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initializePaint();
    }

    /**
     * 初始化 Paint
     */
    private void initializePaint() {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(textSize);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextAlign(Paint.Align.CENTER);

        iconPaint = new Paint();
        iconPaint.setAntiAlias(true);
        iconPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float midX = getWidth() >> 1;
        float midY = getHeight() >> 1;
        textPaint.setColor(textColorInt);
        textPaint.setTextSize(textSize);

        switch (showType) {
            case TEXT_AND_ICON:
                float fourMidX = midX / 2;
                drawText(canvas, text, (int) fourMidX * 3, (int) midX);
                break;
            case ONLY_TEXT:
                drawText(canvas, text, (int) midX, getWidth());
                break;
            case ONLY_ICON:
                break;
            default:
                break;
        }


    }

    /**
     * 绘制文本
     *
     * @param canvas   画布
     * @param text     文字
     * @param midX     中心点
     * @param maxWidth 最大宽度
     */
    private void drawText(Canvas canvas, String text, int midX, int maxWidth) {
        textPaint.getTextBounds(text, 0, text.length(), textRect);
        int left = textRect.left;
        int right = textRect.right;
        if (right - left > maxWidth) {
            drawText(canvas, text.substring(0, text.length() - 1), midX, maxWidth);
        } else {
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float textBaseLine = (getHeight() >> 1) + (fontMetrics.bottom - fontMetrics.top) / 2;
            if (!this.text.equals(text) && text.length() > 1) {
                text = text.substring(0, text.length() - 1) + "..";
            }
            canvas.drawText(text, midX, textBaseLine, textPaint);
        }
    }


    /**
     * 设置文字颜色
     *
     * @param textColorId 颜色资源ID
     */
    public void setTextColor(@ColorRes int textColorId) {
        int textColor = Color.BLACK;
        try {
            textColor = ContextCompat.getColor(mContext, textColorId);
        } catch (Exception e) {
            Log.e(TAG, "setTextColor: ", e);
        }
        this.textColorInt = textColor;
    }

    /**
     * 设置显示文字
     *
     * @param text 文字
     */
    public void setText(@NonNull String text) {
        this.text = text;
    }

    /**
     * 设置文字大小
     *
     * @param textSize 文字大小
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * 设置显示类型
     *
     * @param showType 显示类型
     */
    public void setShowType(ShowType showType) {
        this.showType = showType;
    }


}
