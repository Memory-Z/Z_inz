package com.inz.z.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.awt.font.TextAttribute;
import java.util.Locale;

/**
 * 表盘视图
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/1/23 10:56.
 */
public class WatchView extends View {

    /**
     * 表盘
     */
    private Paint circlePaint;

    /**
     * 时刻点
     */
    private Paint pointPaint;
    private Paint hourPaint;
    private Paint minPaint;
    private Paint secPaint;
    private Paint numPaint;

    private RectF circleRect;
    private RectF hourRect;
    private RectF minuteRect;
    private RectF secondRect;
    private Path hourPath;
    private Path minutePath;
    private Path secondPath;

    private int centerW;
    private int centerH;
    private int watchSize = 240;
    private float screenWidth;
    private float screenHeight;

    private int mHour = 0;
    private int mMinute = 59;
    private int mSecond = 0;

    public WatchView(Context context) {
        this(context, null);
    }

    public WatchView(Context context, AttributeSet attrs) {
        super(context, attrs);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        display.getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(10);

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(Color.BLACK);

        hourPaint = new Paint();
        hourPaint.setStyle(Paint.Style.FILL);
        hourPaint.setAntiAlias(true);
        hourPaint.setColor(Color.BLUE);

        minPaint = new Paint();
        minPaint.setAntiAlias(true);
        minPaint.setStyle(Paint.Style.STROKE);
        minPaint.setStrokeWidth(6);
        minPaint.setColor(Color.GREEN);

        secPaint = new Paint();
        secPaint.setStyle(Paint.Style.STROKE);
        secPaint.setAntiAlias(true);
        secPaint.setStrokeWidth(2);
        secPaint.setColor(Color.RED);

        numPaint = new Paint();
        numPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        numPaint.setColor(Color.BLACK);
        numPaint.setTextSize(18);
        numPaint.setStrokeWidth(2);
        numPaint.setTextLocale(Locale.CHINA);
        numPaint.setTextAlign(Paint.Align.CENTER);

        circleRect = new RectF();
        hourRect = new RectF();
        minuteRect = new RectF();
        secondRect = new RectF();

        hourPath = new Path();
        minutePath = new Path();
        secondPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        centerH = heightMeasureSpec / 2;
        centerW = widthMeasureSpec / 2;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerW = w / 2;
        centerH = h / 2;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        long time = System.currentTimeMillis();
        mSecond = (int) (time / 1000 % 60);
        mMinute = (int) (time / 1000 / 60 % 60);
        mHour = (int) ((time / 1000 / 60 / 60 + 8) % 12);

        // 绘制表盘
        circleRect.left = centerW - watchSize;
        circleRect.top = centerH - watchSize;
        circleRect.right = centerW + watchSize + 1;
        circleRect.bottom = centerH + watchSize + 1;
        canvas.drawArc(circleRect, -90, 360, false, circlePaint);

        // 绘制 表刻度 和 数字
        for (int i = 0; i < 60; ) {
            canvas.save();
            canvas.rotate(i * 6, centerW, centerH);
            if (i % 5 == 0) {
                // 整时刻
                canvas.drawLine(centerW, centerH - watchSize, centerW, centerH - watchSize + 20, pointPaint);
                // 设置数字
                int z = i / 5;
                if (i > 0) {
                    canvas.drawText(z + "", centerW, centerH - watchSize + 40, numPaint);
                } else {
                    canvas.drawText("12", centerW, centerH - watchSize + 40, numPaint);
                }
            } else {
                canvas.drawLine(centerW, centerH - watchSize, centerW, centerH - watchSize + 10, pointPaint);
            }
            canvas.restore();
            i = i + 1;
        }

        canvas.save();
        // 绘制时针
        hourPath.reset();
        hourPath.moveTo(centerW, centerH + 20);
        hourPath.lineTo(centerW - 20, centerH);
        hourPath.lineTo(centerW - 10, centerH - 20);
        hourPath.lineTo(centerW, centerH - watchSize + 180);
        hourPath.lineTo(centerW + 10, centerH - 20);
        hourPath.lineTo(centerW + 20, centerH);
        hourPath.lineTo(centerW, centerH + 20);
        hourPath.close();
        hourPaint.setStrokeJoin(Paint.Join.ROUND);
        int hourDegrees = (int) (mHour * 30 + mMinute * 0.5);
        canvas.rotate(hourDegrees, centerW, centerH);
        canvas.drawPath(hourPath, hourPaint);
        canvas.restore();
        canvas.save();

        // 绘制分针
        minutePath.reset();
        minutePath.moveTo(centerW, centerH + 30);
        minutePath.lineTo(centerW - 6, centerH);
        minutePath.lineTo(centerW - 3, centerH - 5);
        minutePath.lineTo(centerW, centerH - watchSize + 120);
        minutePath.lineTo(centerW + 3, centerH - 5);
        minutePath.lineTo(centerW + 6, centerH);
        minutePath.lineTo(centerW, centerH + 30);
        minPaint.setStrokeJoin(Paint.Join.ROUND);
        int minuteDegrees = (int) (mMinute * 6 + (mSecond % 360 * 0.06));
        canvas.rotate(minuteDegrees, centerW, centerH);
        canvas.drawPath(minutePath, minPaint);
        canvas.restore();

        canvas.save();
        // 绘制秒针
        secondPath.reset();
        secondPath.moveTo(centerW, centerH + 40);
        secondPath.lineTo(centerW -1, centerH);
        secondPath.lineTo(centerW, centerH - watchSize + 80);
        secondPath.lineTo(centerW + 1, centerH);
        secondPath.lineTo(centerW, centerH + 40);
        secondPath.close();
        int secondDegrees = mSecond * 6;
        canvas.rotate(secondDegrees, centerW, centerH);
        canvas.drawPath(secondPath, secPaint);
        canvas.restore();
        postInvalidateDelayed(1000);
    }

}
