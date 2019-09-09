package com.inz.z.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inz.z.R;
import com.inz.z.util.AppBaseTools;
import com.inz.z.view.adapter.example.RollViewPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/1/27 16:11.
 */
public class RollViewPager extends ConstraintLayout implements ViewPager.OnPageChangeListener {
    private Context mContext;
    private View mView;
    private int currentPosition;
    private ViewPager viewPager;
    private LinearLayout textLl;
    private LinearLayout dotLl;
    private TextView titleTv;
    private TextView hintTv;
    private List<Map<String, Object>> mapList;
    private RollViewPagerAdapter rollViewPagerAdapter;

    public RollViewPager(@NonNull Context context) {
        this(context, null);
    }

    public RollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RollViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mapList = new ArrayList<>();
        initView();
        if (attrs != null) {
            initStyle(attrs);
        }
    }

    private void initView() {
        if (mView == null) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.roll_view_pager, null, false);
            viewPager = mView.findViewById(R.id.roll_view_pager_vp);
            textLl = mView.findViewById(R.id.roll_view_pager_text_ll);
            dotLl = mView.findViewById(R.id.roll_view_pager_dot_ll);
            titleTv = mView.findViewById(R.id.roll_view_pager_title_tv);
            hintTv = mView.findViewById(R.id.roll_view_pager_hint_tv);
            viewPager.addOnPageChangeListener(this);
            rollViewPagerAdapter = new RollViewPagerAdapter(this);
            viewPager.setAdapter(rollViewPagerAdapter);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            addView(mView, layoutParams);
        }
    }

    private void initStyle(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.RollViewPager);
        float dotSize = typedArray.getFloat(R.styleable.RollViewPager_bottom_dot_size_mode, 3);
        setBottomDotSize(dotSize);
        boolean showBottomText = typedArray.getBoolean(R.styleable.RollViewPager_show_bottom_text, false);
        showBottomTextLinearLayout(showBottomText);
        typedArray.recycle();
    }

    /**
     * 底部圆点大小
     */
    private int dotSize = 3;

    /**
     * 更新下方 圆点
     *
     * @param count 个数
     */
    public void updateDotView(int count) {
        // 清除全部布局
        dotLl.removeAllViews();
        // 清除缓存布局
        for (int i = 0; i < count; i++) {
            View view = new View(mContext);
            if (i == 0) {
                view.setBackgroundResource(R.drawable.bg_dot_black_selected);
            } else {
                view.setBackgroundResource(R.drawable.bg_dot_black_unselect);
            }
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dotSize, dotSize);
            lp.leftMargin = 10;
            lp.rightMargin = 10;
            lp.gravity = Gravity.CENTER;
            view.setLayoutParams(lp);
            dotLl.addView(view);
        }
    }

    /**
     * 选中圆点
     *
     * @param position 被选中的项
     */
    public void choseDotView(int position) {
        this.currentPosition = position;
        int size = dotLl.getChildCount();
        for (int i = 0; i < size; i++) {
            View view = dotLl.getChildAt(i);
            if (i == position) {
                view.setBackgroundResource(R.drawable.bg_dot_black_selected);
            } else {
                view.setBackgroundResource(R.drawable.bg_dot_black_unselect);
            }
        }
    }

    /**
     * 添加图片链接地址
     *
     * @param imageUrlList 图片链接地址
     */
    public void addImageUrls(List<String> imageUrlList) {
        this.mapList.clear();
        Map<String, Object> map;
        for (int i = 0; i < imageUrlList.size(); i++) {
            map = new HashMap<>();
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            String imageUrl = imageUrlList.get(i);
            Glide.with(mContext).load(imageUrl).into(imageView);
            map.put("order", i);
            map.put("imageView", imageView);
            this.mapList.add(map);
        }
        rollViewPagerAdapter.replaceAll(mapList);
    }

    /**
     * 添加图片
     *
     * @param mapList 图片列表
     */
    public void addImageViewList(List<Map<String, Object>> mapList) {
        this.mapList.clear();
        this.mapList = mapList;
        rollViewPagerAdapter.replaceAll(this.mapList);
    }

    /**
     * 设置 图片内容
     *
     * @param title   标题
     * @param hintStr 提示
     */
    public void setImageDetail(String title, String hintStr) {
        titleTv.setText(title);
        if (hintStr == null || "".equals(hintStr)) {
            hintTv.setGravity(View.GONE);
        } else {
            hintTv.setGravity(View.VISIBLE);
            hintTv.setText(hintStr);
        }
    }

    /**
     * 获取当前页数
     *
     * @return 当前选中图片
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * 设置底部圆点大小
     *
     * @param dotSize 圆点大小
     */
    public void setBottomDotSize(float dotSize) {
        dotSize = dotSize < 2 ? 2 : dotSize;
        this.dotSize = AppBaseTools.dp2px(mContext, dotSize);
    }

    /**
     * 设置是否显示底部文本布局
     *
     * @param isShow 是否显示
     */
    public void showBottomTextLinearLayout(boolean isShow) {
        if (textLl != null) {
            textLl.setVisibility(isShow ? View.VISIBLE : GONE);
        }
    }

    /* ==========================  ViewPager.OnPageChangeListener ========================== */
    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int i) {
        choseDotView(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
    /* ==========================  ViewPager.OnPageChangeListener ========================== */
}
