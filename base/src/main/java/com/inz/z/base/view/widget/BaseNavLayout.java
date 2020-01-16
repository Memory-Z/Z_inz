package com.inz.z.base.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.LayoutRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.widget.TintTypedArray;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.inz.z.base.R;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/17 11:54.
 */
public class BaseNavLayout extends ConstraintLayout {
    private static final String TAG = "BaseNavLayout";
    private View mView;
    private Context mContext;

    private View topLineV, bottomLineV;
    private RelativeLayout leftLayout, centerLayout, rightLayout;

    private boolean topLineVisible, bottomLineVisible;
    private View userStartView, userCenterView, userEndView;
    private int userStartViewId, userCenterViewId, userEndViewId;

    public BaseNavLayout(Context context) {
        this(context, null);
    }

    public BaseNavLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseNavLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initStyle(attrs);
    }

    @SuppressLint("InflateParams")
    private void initView() {
        if (mView == null) {
            LayoutInflater.from(mContext).inflate(R.layout.base_nav_layout, this, true);
            mView = findViewById(R.id.base_nav_cl);
            topLineV = findViewById(R.id.base_nav_top_line_v);
            bottomLineV = findViewById(R.id.base_nav_bottom_line_v);
            leftLayout = findViewById(R.id.base_nav_center_start_rl);
            centerLayout = findViewById(R.id.base_nav_center_content_rl);
            rightLayout = findViewById(R.id.base_nav_center_end_rl);
//            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            addView(mView, layoutParams);
        }
    }

    @SuppressLint("RestrictedApi")
    private void initStyle(AttributeSet set) {
        TintTypedArray array = TintTypedArray.obtainStyledAttributes(mContext, set, R.styleable.BaseNavLayout, 0, 0);
        topLineVisible = array.getBoolean(R.styleable.BaseNavLayout_base_top_line_visible, false);
        bottomLineVisible = array.getBoolean(R.styleable.BaseNavLayout_base_bottom_line_visible, false);
        userStartViewId = array.getResourceId(R.styleable.BaseNavLayout_base_left_layout, R.id.base_nav_center_start_rl);
        userCenterViewId = array.getResourceId(R.styleable.BaseNavLayout_base_center_layout, R.id.base_nav_center_content_rl);
        userEndViewId = array.getResourceId(R.styleable.BaseNavLayout_base_right_layout, R.id.base_nav_center_end_rl);
        array.recycle();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initStartView();
        initCenterView();
        initEndView();
        setViewStyle();
    }

    public void setTopLineVisible(boolean topLineVisible) {
        this.topLineVisible = topLineVisible;
    }

    public void setBottomLineVisible(boolean bottomLineVisible) {
        this.bottomLineVisible = bottomLineVisible;
    }

    public void setUserStartViewId(@LayoutRes int userStartViewId) {
        this.userStartViewId = userStartViewId;
    }

    public void setUserCenterViewId(@LayoutRes int userCenterViewId) {
        this.userCenterViewId = userCenterViewId;
    }

    public void setUserEndViewId(@LayoutRes int userEndViewId) {
        this.userEndViewId = userEndViewId;
    }


    private void setViewStyle() {
        topLineV.setVisibility(topLineVisible ? VISIBLE : GONE);
        bottomLineV.setVisibility(bottomLineVisible ? VISIBLE : GONE);
    }

    private void initStartView() {
        if (userStartViewId != R.id.base_nav_center_start_rl) {
            View view = findViewById(userStartViewId);
            if (view != null) {
                removeView(view);
                leftLayout.addView(userStartView = view);
            }
        }
    }

    private void initCenterView() {
        if (userCenterViewId != R.id.base_nav_center_content_rl) {
            View view = findViewById(userCenterViewId);
            if (view != null) {
                removeView(view);
                centerLayout.addView(userCenterView = view);
            }
        }
    }

    private void initEndView() {
        if (userEndViewId != R.id.base_nav_center_end_rl) {
            View view = findViewById(userEndViewId);
            if (view != null) {
                removeView(view);
                rightLayout.addView(userEndView = view);
            }
        }
    }
}
