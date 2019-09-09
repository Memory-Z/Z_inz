package com.inz.z.base.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.appbar.AppBarLayout;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inz.z.base.R;
import com.inz.z.base.util.ImageUtils;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/12 16:59.
 */
public class BaseTopActionLayout extends AppBarLayout {
    private static final String TAG = "BaseTopActionLayout";
    private Context mContext;
    private View mView;
    private RelativeLayout leftRl, rightRl, centerRl;
    private ConstraintLayout centerCl;
    private TextView titleTv;
    private Toolbar toolbar;

    private View userLeftView, userCenterView, userRightView;
    private int userLeftLayoutId, userCenterLayoutId, userRightLayoutId;
    private String titleStr;
    private int textGravity;
    private int statusBarHeight = 0;

    public enum TitleGravity {
        START(Gravity.START),
        CENTER(Gravity.CENTER),
        END(Gravity.END);

        int value;

        TitleGravity(int value) {
            this.value = value;
        }
    }

    public BaseTopActionLayout(Context context) {
        this(context, null);
    }

    public BaseTopActionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        initStyle(attrs);
    }

//    public BaseTopActionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        mContext = context;
//        initView();
//        initStyle(attrs);
//    }

    @SuppressLint("InflateParams")
    private void initView() {
        if (mView == null) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.base_top_action_layout, null, false);
            leftRl = mView.findViewById(R.id.base_top_action_left_rl);
            centerRl = mView.findViewById(R.id.base_top_action_center_rl);
            rightRl = mView.findViewById(R.id.base_top_action_right_rl);
            titleTv = mView.findViewById(R.id.base_top_action_center_title_tv);
            centerCl = mView.findViewById(R.id.base_top_action_center_cl);
            toolbar = mView.findViewById(R.id.base_top_action_toolbar);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addView(mView, layoutParams);
        }
    }

    @SuppressLint("RestrictedApi")
    private void initStyle(AttributeSet attrs) {
        TintTypedArray array = TintTypedArray.obtainStyledAttributes(mContext, attrs, R.styleable.BaseTopActionLayout, 0, 0);
        userLeftLayoutId = array.getResourceId(R.styleable.BaseTopActionLayout_base_top_left_layout, R.id.base_top_action_left_rl);
        userCenterLayoutId = array.getResourceId(R.styleable.BaseTopActionLayout_base_top_center_layout, R.id.base_top_action_center_rl);
        userRightLayoutId = array.getResourceId(R.styleable.BaseTopActionLayout_base_top_right_layout, R.id.base_top_action_right_rl);
        titleStr = array.getString(R.styleable.BaseTopActionLayout_base_top_title);
        textGravity = array.getInt(R.styleable.BaseTopActionLayout_base_top_title_gravity, TitleGravity.CENTER.value);
    }


    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTopActionTitle(String title) {
        titleStr = title;
    }

    public void setTopActionTitleGravity(TitleGravity gravity) {
        textGravity = gravity.value;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initializeView();
//        setTopStatusHeight(statusBarHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setTopStatusHeight(statusBarHeight);

    }

    /**
     * 初始化自定义 View
     */
    private void initializeView() {
        setUserLeftView();
        setUserCenterView();
        setUserRightView();
        setTitleTv();
        offsetLeftAndRight(48);
    }

    /**
     * 设置左侧自定义View
     */
    private void setUserLeftView() {
        if (userLeftLayoutId != R.id.base_top_action_left_rl) {
            View view = findViewById(userLeftLayoutId);
            if (view != null) {
                removeView(view);
                userLeftView = view;
                leftRl.addView(userLeftView);
            }
        }
    }

    /**
     * 设置中间自定义View
     */
    private void setUserCenterView() {
        if (userCenterLayoutId != R.id.base_top_action_center_rl) {
            View view = findViewById(userCenterLayoutId);
            if (view != null) {
                removeView(view);
                userCenterView = view;
                centerRl.addView(userCenterView);
            }
        }
    }

    /**
     * 设置右侧自定义View
     */
    private void setUserRightView() {
        if (userRightLayoutId != R.id.base_top_action_right_rl) {
            View view = findViewById(userRightLayoutId);
            if (view != null) {
                removeView(view);
                userRightView = view;
                rightRl.addView(userRightView);
            }
        }
    }

    /**
     * 设置标题
     */
    private void setTitleTv() {
        if (titleTv != null) {
            titleTv.setText(titleStr);
            titleTv.setGravity(textGravity);
        }
    }

    /**
     * 设置顶部状态栏高
     *
     * @param statusHeight 状态栏高度
     */
    private void setTopStatusHeight(int statusHeight) {
        setPaddingRelative(getPaddingStart(), statusHeight, getPaddingEnd(), getPaddingBottom());
        if (toolbar != null) {
            Bitmap bitmap = getToolbarBackground(toolbar, statusHeight);
            if (bitmap != null) {
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                toolbar.setBackground(drawable);
            } else {
                toolbar.setBackground(getBackground());
            }
        }
    }

    /**
     * 获取 Toolbar 背景
     *
     * @return Bitmap
     */
    private Bitmap getToolbarBackground(Toolbar toolbar, int statusBarHeight) {
        Drawable backgroundDrawable = getBackground();
        Bitmap backBitmap = ImageUtils.getBitmapFromDrawable(backgroundDrawable);
        if (backBitmap != null) {
            int bW = backBitmap.getWidth();
            int bH = backBitmap.getHeight();
            int lW = getMeasuredWidth();
            int lH = getMeasuredHeight();
            int tH = toolbar.getMeasuredHeight();
            float needH = (float) bH / lH * tH + .5F;
            float startY = (float) bH / lH * statusBarHeight + .5F;
            return Bitmap.createBitmap(backBitmap, 0, (int) startY, bW, (int) needH);
        }
        return null;
    }

    public void setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
    }

}
