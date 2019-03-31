package com.inz.z.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.inz.z.R;
import com.inz.z.bean.TiwFood;
import com.inz.z.view.adapter.TiwBaseFoodAdapter;
import com.inz.z.view.widget.TiwActionLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/26 10:36.
 */
public class TiwBaseActivity extends AbsBaseActivity {

    private TiwActionLayout tiwActionLayout;
    private SwipeRefreshLayout mSwipeRefreshL;
    private RecyclerView mRecyclerView;
    private LinearLayout tabContentLl;
    private HorizontalScrollView tabTopHsl;
    private TextView tabBaseTv;
    private TiwBaseFoodAdapter tiwBaseFoodAdapter;
    private ScrollView contentSv;

    @Override
    protected void initWindow() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.tiw_base_layout;
    }

    @Override
    protected void initView() {
        tiwActionLayout = findViewById(R.id.tiw_base_tal);
        contentSv = findViewById(R.id.tiw_base_content_sv);
        mSwipeRefreshL = findViewById(R.id.tiw_base_content_main_srl);
        mSwipeRefreshL.setProgressBackgroundColorSchemeResource(R.color.tiw_main);
        mSwipeRefreshL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mSwipeRefreshL.setRefreshing(false);
            }
        });
        mRecyclerView = findViewById(R.id.tiw_base_content_main_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        tabContentLl = findViewById(R.id.tiw_base_main_top_hor_content_ll);
        tabTopHsl = findViewById(R.id.tiw_base_main_top_hor_sv);
        tabBaseTv = findViewById(R.id.tiw_base_main_top_hor_ll_base_tv);
    }

    @Override
    protected void initData() {
        tiwBaseFoodAdapter = new TiwBaseFoodAdapter(mContext);
        mRecyclerView.setAdapter(tiwBaseFoodAdapter);
        setTabListData();
        setContentData();
    }

    @Override
    public boolean myOnKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    private boolean measure = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !measure) {
            measure = true;
            setSwipeHeight();
        }
    }

    private void setSwipeHeight() {
        int tabH = tabTopHsl.getHeight();
        int scH = contentSv.getHeight();
        int h = scH - tabH;
        ViewGroup.LayoutParams lp = mSwipeRefreshL.getLayoutParams();
        lp.height = h;
        mSwipeRefreshL.setLayoutParams(lp);
        tiwBaseFoodAdapter.notifyDataSetChanged();
    }

    private String[] tabNameArray = new String[]{
            "全部", "小吃", "快餐", "饮料", "水果", "特色", "干果", "其他"
    };

    private List<TextView> tabTextViewList = new ArrayList<>();

    /**
     * 设置TAB 数据
     */
    private void setTabListData() {
        ViewGroup.LayoutParams layoutParams = tabBaseTv.getLayoutParams();
        tabBaseTv.setVisibility(View.GONE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMarginStart(20);
        lp.setMarginEnd(20);
        for (int i = 0; i < 6; i++) {
            TextView textView = new TextView(mContext);
            textView.setLayoutParams(layoutParams);
            textView.setText(tabNameArray[i]);
            textView.setTag("tab_" + i);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.tiw_bg_tab_normal);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.tiw_main));
            textView.setTextSize(18);
            textView.setClickable(true);
            textView.setFocusable(true);
            setTabClickListener(textView);
            tabContentLl.addView(textView, lp);
            tabTextViewList.add(textView);
        }
    }

    private void setTabClickListener(TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = (String) v.getTag();
                for (TextView view : tabTextViewList) {
                    if (tag.equals(view.getTag())) {
                        view.setBackgroundResource(R.drawable.tiw_bg_tab_selected);
                        view.setTextColor(ContextCompat.getColor(mContext, R.color.tiw_gray));
                    } else {
                        view.setBackgroundResource(R.drawable.tiw_bg_tab_normal);
                        view.setTextColor(ContextCompat.getColor(mContext, R.color.tiw_main));
                    }

                }
            }
        });
    }

    private String[] contentImageUrl = new String[]{
            "https://b-ssl.duitang.com/uploads/item/201506/26/20150626160448_GPnjW.jpeg",
            "https://drscdn.500px.org/photo/300179805/q%3D80_m%3D2000/v2?webp=true&sig=77f34e1cbb9a9c265a5e4ab580576f80e52eea1c016e5c296fcb766d89f97c2c",
            "https://drscdn.500px.org/photo/300212847/q%3D80_m%3D2000/v2?webp=true&sig=8761c9eaddd8321d6bd61f15db7b6ffc15149c8a71aa21bc6cfd36e493633f5a",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-759228.png",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-759223.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-758823.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-758366.png",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-758094.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-758054.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757836.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757833.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757832.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757738.png",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757645.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757610.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757530.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757480.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757417.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757389.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757355.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-757177.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-756834.jpg",
            "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-756770.png"
    };

    /**
     * 设置中间数据
     */
    private void setContentData() {
        List<TiwFood> tiwFoodList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TiwFood tiwFood = new TiwFood();
            tiwFood.setId(i);
            tiwFood.setFoodDetail("简介 + " + i);
            tiwFood.setFoodImageUrl(contentImageUrl[i]);
            tiwFood.setFoodName("名称" + i);
            tiwFood.setFoodNum(52 - i);
            tiwFood.setFoodPrice(10L + i);
            tiwFoodList.add(tiwFood);
        }
        tiwBaseFoodAdapter.addTiwFoodList(tiwFoodList);
    }

    private void setViewS() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}
