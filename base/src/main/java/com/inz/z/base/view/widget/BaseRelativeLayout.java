package com.inz.z.base.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.inz.z.base.R;
import com.inz.z.base.util.BaseTools;
import com.inz.z.base.util.L;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义 RelativeLayout 实现侧滑删除
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/09/12 09:31.
 */
public class BaseRelativeLayout extends RelativeLayout {
    private static final String TAG = "BaseRelativeLayout";

    private Context mContext;
    private List<BaseCatalogView> catalogViewList;
    private LinearLayout catalogLl;
    private RelativeLayout swipedRl;


    public BaseRelativeLayout(Context context) {
        this(context, null);
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        catalogViewList = new ArrayList<>(2);

        initView();
        initStyle();
    }

    /**
     * 初始化内容
     */
    private void initView() {
        catalogLl = new LinearLayout(mContext);
        catalogLl.setId(R.id.base_relative_layout_operation_ll);
        catalogLl.setOrientation(LinearLayout.HORIZONTAL);
        catalogLl.setGravity(Gravity.CENTER);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);

        addView(catalogLl, layoutParams);

        View rootView = getRootView();
        L.i(TAG, "initView: " + rootView);

    }

    private void initStyle() {


    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View rootView = getRootView();
        L.i(TAG, "onFinishInflate: " + rootView);
        if (rootView instanceof BaseRelativeLayout) {
            int childViewCount = ((BaseRelativeLayout) rootView).getChildCount();
            if (childViewCount > 1) {
                initSwipedView((BaseRelativeLayout) rootView, childViewCount);
            }
        }
    }

    /**
     * 设置滑动布局
     */
    private void initSwipedView(BaseRelativeLayout rootView, int childViewCount) {
        if (mContext == null) {
            L.w(TAG, "initSwipedView: mContext is null. ");
            return;
        }
        if (swipedRl == null) {
            swipedRl = new RelativeLayout(mContext);
        }

        for (int i = childViewCount - 1; i > 0; i--) {
            View view = rootView.getChildAt(i);
            rootView.removeViewAt(i);
            swipedRl.addView(view, 0);
        }

        Drawable backgroundDrawable = this.getBackground();
        swipedRl.setBackground(backgroundDrawable);

//        this.setBackgroundColor(Color.TRANSPARENT);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(swipedRl, layoutParams);
    }

    /**
     * 添加删除按钮
     */
    public void addDeleteView() {
        BaseCatalogView deleteCatalogView = new BaseCatalogView(mContext);
        deleteCatalogView.setBackgroundColor(Color.RED);
        deleteCatalogView.setTextColor(R.color.white);
        deleteCatalogView.setTextSize(36);
        deleteCatalogView.setText("删除");
        deleteCatalogView.setId(R.id.base_relative_layout_delete_dcv);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(BaseTools.dp2px(mContext, 56), ViewGroup.LayoutParams.MATCH_PARENT);
        catalogLl.addView(deleteCatalogView, layoutParams);
        catalogViewList.add(deleteCatalogView);
        invalidate();
    }

    /**
     * 扩展菜单创建
     */
    public class CatalogViewBuilder {

        BaseCatalogView catalogView;
        Context mContext;

        public CatalogViewBuilder(Context mContext) {
            this.catalogView = new BaseCatalogView(mContext);
        }

        /**
         * 设置标题
         *
         * @param title 标题名称
         */
        public CatalogViewBuilder setTitleStr(String title) {
            catalogView.setText(title);
            return this;
        }

        /**
         * 设置标题大小
         *
         * @param textSize 文字大小 UNIT: SP
         */
        public CatalogViewBuilder setTextSize(int textSize) {
            int size = BaseTools.sp2px(mContext, textSize);
            catalogView.setTextSize(size);
            return this;
        }

        /**
         * 设置显示类型
         *
         * @param showType 显示类型
         */
        public CatalogViewBuilder setShowType(BaseCatalogView.ShowType showType) {
            catalogView.setShowType(showType);
            return this;
        }

        public CatalogViewBuilder setIconDrawable(Drawable drawable) {
            return this;
        }

        public CatalogViewBuilder setIcon(@DrawableRes int res) {
            Drawable drawable = ContextCompat.getDrawable(mContext, res);
            setIconDrawable(drawable);
            return this;
        }

        public void build() {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(BaseTools.dp2px(mContext, 56), ViewGroup.LayoutParams.MATCH_PARENT);
            catalogLl.addView(catalogView, layoutParams);
            catalogViewList.add(catalogView);
            invalidate();
        }

    }

    /**
     * 添加菜单栏
     *
     * @param title           菜单标题
     * @param iconDrawable    菜单图标
     * @param showType        显示类型
     * @param onClickListener 点击监听
     */
    public void addCatalogView(String title, Drawable iconDrawable, BaseCatalogView.ShowType showType, View.OnClickListener onClickListener) {

    }

}
