package com.inz.z.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.Display;
import android.view.View;

import com.inz.z.util.AppBaseTools;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/1/22 9:32.
 */
public class DrawView extends View {

    private Context mContext;
    private Paint paint;
    private int startAngle = 0;
    private Paint borderPaint;
    private RectF ltRect;
    private int width;
    private int height;
    private Display display;

    public DrawView(Context context) {
        super(context);
        mContext = context;
        paint = new Paint();
        // 设置抗锯齿
        paint.setAntiAlias(true);
        // 设置 描边
        paint.setStyle(Paint.Style.STROKE);

        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.FILL);

        ltRect = new RectF();
        ltRect.left = 0;
        ltRect.top = 0;
        display = getDisplay();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = Math.min(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = Math.min(w, h);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width = getMeasuredWidth();
        width = getWidth();
        height = getMeasuredHeight();
        height = getHeight();
        int centerW = getWidth() / 2;
        int centerH = getHeight() / 2;
        // 内圆半径
        int innerCircle = AppBaseTools.dp2px(mContext, 80);
        // 圆环宽度
        int ringWidth = AppBaseTools.dp2px(mContext, 10);

        paint.setColor(Color.BLUE);
        paint.setAlpha(244);
        // 设置 画笔宽度
        paint.setStrokeWidth(2);
        // 绘制内圆
        canvas.drawCircle(centerW, centerH, innerCircle, paint);

        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(ringWidth);
        canvas.drawCircle(centerW, centerH, innerCircle + 1 + ringWidth / 2, paint);

        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2);
        canvas.drawCircle(centerW, centerH, innerCircle + ringWidth + 1, paint);

        paint.setStrokeWidth(2);
        paint.setTextSize(60);
        canvas.drawText("Abc测试", centerW, centerH + 1 + innerCircle + ringWidth * 2, paint);

        int width = 200;
        RectF rectF = new RectF(centerW - (innerCircle + 1 + ringWidth / 2), centerH - (innerCircle + 1 + ringWidth / 2), centerW + (innerCircle + 1 + ringWidth / 2), centerH + (innerCircle + 1 + ringWidth / 2));
        paint.setStrokeWidth(6);
        paint.setColor(Color.BLUE);
        canvas.drawArc(rectF, -90 + startAngle, 90, false, paint);

        paint.setStrokeWidth(20);
        Path linePath = new Path();
        linePath.moveTo(centerW - (innerCircle + 1 + ringWidth / 2), centerH + startAngle);
        linePath.lineTo(centerW - (innerCircle + 1 + ringWidth / 2), centerH + startAngle);
        canvas.drawPath(linePath, paint);
        startAngle += 4;
        if (startAngle == 360) {
            startAngle = 0;
        }
        invalidate();
    }
}
