package com.inz.z.dial_phone.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 拨号按键
 *
 * @author 11654
 * @version 1.0.0
 * Create by inz in 2019/11/18 19:28
 **/
public class DialNumberView extends View {

    private Context mContext;


    private Paint mainTextP, secondTextP;
    private float mainTextSize, secondTextSize;
    private int mainTextColor, secondTextColor;

    private String mainTextStr;
    private List<String> secondTextStrArray;
    private StringBuilder secondTextSb;
    /**
     * 次文字 间隔标识符
     */
    private String secondTextIntervalSymbol = "";
    private Rect secondRect;

    /**
     * 是否显示两行文本
     */
    private boolean haveSecondText = false;

    public DialNumberView(Context context) {
        this(context, null);
    }

    public DialNumberView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialNumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initConfigure();
        initPaint();
    }

    /**
     * 初始化配置
     */
    private void initConfigure() {

        setClickable(true);
        setFocusable(true);

        mainTextColor = Color.parseColor("#FF474747");
        secondTextColor = Color.parseColor("#8A474747");

        mainTextSize = 56;
        secondTextSize = 36;

        mainTextStr = "1";
        secondTextStrArray = new ArrayList<>(2);
        secondTextSb = new StringBuilder();
        secondRect = new Rect();

        haveSecondText = true;
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mainTextP = new Paint();
        mainTextP.setAntiAlias(true);
        mainTextP.setColor(mainTextColor);
        mainTextP.setTextSize(mainTextSize);
        mainTextP.setTextAlign(Paint.Align.CENTER);
        mainTextP.setStyle(Paint.Style.FILL);

        secondTextP = new Paint();
        secondTextP.setAntiAlias(true);
        secondTextP.setTextAlign(Paint.Align.CENTER);
        secondTextP.setColor(secondTextColor);
        secondTextP.setTextSize(secondTextSize);
        secondTextP.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int baseSize = Math.min(width, height);

        setMeasuredDimension(baseSize, baseSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GRAY);
        int width = getWidth();
        int height = getHeight();
        int x = width / 2;
        Paint.FontMetrics mainFontMetrics = mainTextP.getFontMetrics();
        int y = (int) ((height - mainFontMetrics.bottom + mainFontMetrics.top) / 2 - mainFontMetrics.top);
        canvas.drawText(mainTextStr.toUpperCase(), x, y, mainTextP);

        if (haveSecondText) {
            Paint.FontMetrics secondFm = secondTextP.getFontMetrics();
            int secondY = (int) (y + secondFm.bottom - secondFm.top);
            secondTextSb.setLength(0);
            for (String str : secondTextStrArray) {
                secondTextSb.append(str).append(secondTextIntervalSymbol);
            }
            String secondTextStr = secondTextSb.toString();
            // 处理 第二行 文字长度 大于 布局宽度
//            secondTextP.getTextBounds(secondTextStr, 0 , secondTextStr.length(), secondRect);
//            int textWidth = secondRect.width();
//            if (textWidth > width) {
//                //
//            }
            canvas.drawText(secondTextStr.toUpperCase(), x, secondY, secondTextP);
        }


    }

    public void setMainTextStr(String mainTextStr) {
        this.mainTextStr = mainTextStr;
    }


    public void setSecondTextStrArray(String[] strings) {
        this.secondTextStrArray = Arrays.asList(strings);
    }

    public void setSecondTextStrArray(List<String> secondTextStrArray) {
        this.secondTextStrArray = secondTextStrArray;
    }

    public String getMainTextStr() {
        return mainTextStr;
    }

    public List<String> getSecondTextStrArray() {
        return secondTextStrArray;
    }

    public String[] getSecondTextStringArray() {
        String[] strings = new String[secondTextStrArray.size()];
        return secondTextStrArray.toArray(strings);
    }

    @Override
    public boolean performClick() {
        if (numberClickListener != null) {
            numberClickListener.onClick(this, mainTextStr, getSecondTextStringArray());
        }
        return super.performClick();
    }

    public interface OnNumberClickListener {
        void onClick(View v, String main, String[] strings);
    }

    private OnNumberClickListener numberClickListener;

    public void setNumberClickListener(OnNumberClickListener numberClickListener) {
        this.numberClickListener = numberClickListener;
    }
}
