package com.inz.z.base.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.inz.z.base.R;

/**
 * 自定义RecyclerView 实现 侧滑显示删除按钮
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/09/12 09:29.
 */
public class BaseRecyclerView extends RecyclerView {
    private static final String TAG = "BaseRecyclerView";
    private Context mContext;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private final static int CLOSE = 0x00;
    private final static int WILL_OPEN = 0x01;
    private final static int OPEN = 0x02;
    private final static int WILL_CLOSE = 0x03;

    public BaseRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public BaseRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScroller = new Scroller(mContext, new LinearInterpolator());
        // 构建滑动速度检测
        mVelocityTracker = VelocityTracker.obtain();

    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    /**
     * 计算按下和抬起的时间差
     */
    private long downTime = 0;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                View view = findChildViewUnder(x, y);
                if (view == null) {
                    return super.onTouchEvent(e);
                }

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                long cTime = System.currentTimeMillis();
                if (cTime - downTime < 1000) {
                    this.performClick();
                }
                break;
            default:
                break;
        }

        return super.onTouchEvent(e);

    }


    public abstract static class BaseViewHolder extends RecyclerView.ViewHolder {
        protected boolean canSwipe = false;
        /**
         * 可滑动布局
         */
        protected BaseRelativeLayout baseRelativeLayout = null;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            View view = itemView.findViewById(R.id.base_relative_layout_operation_ll);
            if (view != null) {
                View parentView = (View) view.getParent();
                if (parentView instanceof BaseRelativeLayout) {
                    baseRelativeLayout = (BaseRelativeLayout) parentView;
                    baseRelativeLayout.addDeleteView();
                    canSwipe = true;
                }
            }
        }


    }
}
