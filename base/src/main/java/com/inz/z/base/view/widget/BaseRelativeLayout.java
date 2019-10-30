package com.inz.z.base.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.TintTypedArray;

import com.inz.z.base.R;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/23 10:08.
 */
public class BaseRelativeLayout extends RelativeLayout {

    private Context mContext;
    private View mView;
    private RelativeLayout headerRl, contentRl, footerRl;
    private int headerLayoutId, contentLayoutId, footerLayoutId;

    public BaseRelativeLayout(Context context) {
        this(context, null);
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initStyle(attrs);
    }

    @SuppressLint("InflateParams")
    private void initView() {
        if (mView == null) {
            LayoutInflater.from(mContext).inflate(R.layout.base_relative_layout, this, true);
            mView = findViewById(R.id.base_relative_layout_rl);
            headerRl = findViewById(R.id.base_relative_layout_header_rl);
            contentRl = findViewById(R.id.base_relative_layout_content_rl);
            footerRl = findViewById(R.id.base_relative_layout_footer_rl);
//            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            addView(mView, params);
        }
    }

    @SuppressLint("RestrictedApi")
    private void initStyle(AttributeSet attributeSet) {
        TintTypedArray array = TintTypedArray.obtainStyledAttributes(mContext, attributeSet, R.styleable.BaseRelativeLayout, 0, 0);
        headerLayoutId = array.getResourceId(R.styleable.BaseRelativeLayout_base_relative_layout_header_view, R.id.base_relative_layout_header_rl);
        contentLayoutId = array.getResourceId(R.styleable.BaseRelativeLayout_base_relative_layout_content_view, R.id.base_relative_layout_content_rl);
        footerLayoutId = array.getResourceId(R.styleable.BaseRelativeLayout_base_relative_layout_footer_view, R.id.base_relative_layout_footer_rl);
        array.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initializeView();
    }

    private View headerView = null, footerView = null, contentView = null;

    /**
     * 初始化自定义View
     */
    private void initializeView() {
        setHeaderView();
        setFooterView();
        setContentView();
    }

    /**
     * 设置顶部View
     */
    private void setHeaderView() {
        if (headerLayoutId != R.id.base_relative_layout_header_rl) {
            headerView = findViewById(headerLayoutId);
            if (headerView != null) {
                addHeader(headerView);
            }
        }
    }

    /**
     * 设置中间 View
     */
    private void setContentView() {
        if (contentLayoutId != R.id.base_relative_layout_content_rl) {
            contentView = findViewById(contentLayoutId);
            if (contentView != null) {
                addContent(contentView);
            }
        } else {
            int viewCount = getChildCount();
//            View hV = null;
//            if (headerLayoutId != R.id.base_relative_layout_header_rl) {
//                hV = findViewById(headerLayoutId);
//            }
//            if (headerView != null) {
//
//            }
//            View fV = null;
//            if (footerLayoutId != R.id.base_relative_layout_footer_rl) {
//                fV = findViewById(footerLayoutId);
//            }
            if (viewCount < 2) {
                // gt 2 , is have more self view add. , because first is this view. in [@initView() ]
                return;
            }
            for (int i = 1; i < viewCount; i++) {
                View v = this.getChildAt(i);
                if (headerView != null && v == headerView) {
                    break;
                }
                if (footerView != null && v == footerView) {
                    break;
                }
                addContent(v);
            }
        }
    }

    /**
     * 设置底部 View
     */
    private void setFooterView() {
        if (footerLayoutId != R.id.base_relative_layout_footer_rl) {
            footerView = findViewById(footerLayoutId);
            if (footerView != null) {
                addFooter(footerView);
            }
        }
    }

    /**
     * 添加头部布局
     *
     * @param view 布局
     */
    public void addHeader(@NonNull View view) {
        removeView(view);
        headerRl.addView(view);
    }

    /**
     * 显示顶部提示
     *
     * @param view    提示内容
     * @param dismiss 消失时间， &lt; 0  不消失
     */
    public void showHeaderNotification(@NonNull View view, long dismiss) {
        headerRl.removeAllViews();
        headerRl.addView(view);
        if (dismiss <= 0) {
            return;
        }
        headerRl.postDelayed(new Runnable() {
            @Override
            public void run() {
                headerRl.removeAllViews();
            }
        }, dismiss);
    }

    /**
     * 添加中间布局
     *
     * @param view 布局
     */
    public void addContent(@NonNull View view) {
        if (contentLayoutId == headerLayoutId || contentLayoutId == footerLayoutId) {
            throw new IllegalStateException("content view id already used. ");
        }
        removeView(view);
        contentRl.addView(view);
    }

    /**
     * 添加底部布局
     *
     * @param view 布局
     */
    public void addFooter(@NonNull View view) {
        if (footerLayoutId == headerLayoutId) {
            throw new IllegalStateException(" footer view with header view is one view id ");
        }
        removeView(view);
        footerRl.addView(view);
    }

    /**
     * 获取顶部
     *
     * @return 顶部
     */
    public RelativeLayout getHeaderRl() {
        return headerRl;
    }

    /**
     * 获取中间
     *
     * @return 中间
     */
    public RelativeLayout getContentRl() {
        return contentRl;
    }

    /**
     * 获取底部
     *
     * @return 底部
     */
    public RelativeLayout getFooterRl() {
        return footerRl;
    }


    /**
     * 设置顶部布局显示隐藏
     *
     * @param show 是否显示。true: 显示
     */
    public void setHeaderViewVisibility(boolean show) {
        if (headerRl != null) {
            headerRl.setVisibility(show ? VISIBLE : GONE);
        }
    }

    /**
     * 设置底部布局显示隐藏
     *
     * @param show 是否显示。true: 显示
     */
    public void setFooterViewVisibility(boolean show) {
        if (footerRl != null) {
            footerRl.setVisibility(show ? VISIBLE : GONE);
        }
    }
}
