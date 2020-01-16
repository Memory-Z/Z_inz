package com.inz.z.base.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.TintTypedArray;

import com.inz.z.base.R;

/**
 * 保密 ImageView
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2020/01/15 13:44.
 */
public class BaseSecurityImageView extends AppCompatImageView {

    private static final String TAG = "BaseSecurityImageView";
    private static final int SECURITY_TEXT_ANGLE = -45;
    private static final int SECURITY_TEXT_COLOR = Color.parseColor("#80474747");
    private static final int SECURITY_TEXT_SIZE = 28;
    private View mView;
    private Context mContext;

    public enum SecurityType {
        TEXT,
    }

    private String securityMessage = "";
    private Paint messagePaint;
    private int messageTextColor = SECURITY_TEXT_COLOR;
    private int messageTextSize = SECURITY_TEXT_SIZE;
    private Rect messageRect;


    public BaseSecurityImageView(Context context) {
        super(context);
        this.mContext = context;
        initPaint();
    }

    public BaseSecurityImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseSecurityImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initAttrs(attrs);
        initView();
        initPaint();
    }

    private void initView() {

    }

    /**
     * 初始化配置
     *
     * @param attrs 配置
     */
    private void initAttrs(AttributeSet attrs) {
        TintTypedArray array = TintTypedArray.obtainStyledAttributes(mContext, attrs, R.styleable.BaseSecurityImageView, 0, 0);
        messageTextColor = array.getColor(R.styleable.BaseSecurityImageView_textColor, SECURITY_TEXT_COLOR);
        messageTextSize = array.getDimensionPixelSize(R.styleable.BaseSecurityImageView_textSize, SECURITY_TEXT_SIZE);
        securityMessage = array.getString(R.styleable.BaseSecurityImageView_text);
        array.recycle();
    }

    private void initPaint() {
        messagePaint = new Paint();
        messagePaint.setAntiAlias(true);
        messagePaint.setTextSize(messageTextSize);
        messagePaint.setColor(messageTextColor);
        messagePaint.setTextAlign(Paint.Align.CENTER);
        messagePaint.setStyle(Paint.Style.FILL);

        messageRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        float centerX = getWidth() / 2F;
        float centerY = getHeight() / 2F;
        Paint.FontMetricsInt fontMetricsInt = messagePaint.getFontMetricsInt();
        float textCenterY = centerY - (fontMetricsInt.bottom - fontMetricsInt.top) / 2F;
        messagePaint.getTextBounds(securityMessage, 0, securityMessage.length(), messageRect);
        float textWidth = messageRect.right - messageRect.left;
        int baseBlockWidth = (int) Math.abs(Math.sqrt(textWidth * textWidth / 2)) + 200;
        float baseBlockCenterX = baseBlockWidth / 2F;
        int xBlockNum = 3 * getWidth() / baseBlockWidth + 1;
        int yBlockNum = 2 * getHeight() / baseBlockWidth + 1;
        for (int yN = 0; yN < yBlockNum; yN++) {
            float lineCenterY = baseBlockWidth * yN + baseBlockCenterX;
            for (int xN = 0; xN < xBlockNum; xN++) {
                canvas.save();
                canvas.rotate(SECURITY_TEXT_ANGLE);
                float rawCenterX = baseBlockWidth * xN + baseBlockCenterX - getHeight();
                canvas.drawText(securityMessage, rawCenterX, lineCenterY, messagePaint);
                canvas.restore();
            }
        }

    }
}
