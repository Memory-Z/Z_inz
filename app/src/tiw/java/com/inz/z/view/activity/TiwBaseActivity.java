package com.inz.z.view.activity;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.inz.z.R;
import com.inz.z.bean.TiwFood;
import com.inz.z.view.adapter.TiwBaseFoodAdapter;
import com.inz.z.view.widget.TiwActionLayout;

import org.jetbrains.annotations.NotNull;

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
        tiwBaseFoodAdapter.setLoadMoreListener(new AdapterDataLoadListenerImpl());
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
            "http://papers.co/wallpaper/papers.co-bb47-circle-rainbow-minimal-illustration-art-36-3840x2400-4k-wallpaper.jpg",
            "http://papers.co/wallpaper/papers.co-ba66-circle-line-simple-illustration-art-blue-36-3840x2400-4k-wallpaper.jpg",
            "http://papers.co/wallpaper/papers.co-hr48-kpop-girl-asian-face-pretty-36-3840x2400-4k-wallpaper.jpg",
            "http://papers.co/wallpaper/papers.co-hr40-kristen-stewart-girl-film-face-36-3840x2400-4k-wallpaper.jpg",
            "http://papers.co/wallpaper/papers.co-bg96-face-black-drawing-line-art-36-3840x2400-4k-wallpaper.jpg",
            "http://papers.co/wallpaper/papers.co-hr45-girl-kpop-taeyeon-pink-music-snsd-36-3840x2400-4k-wallpaper.jpg",
            "http://papers.co/wallpaper/papers.co-bb48-circle-rainbow-minimal-illustration-art-dark-36-3840x2400-4k-wallpaper.jpg",
            "http://papers.co/wallpaper/papers.co-hr19-girl-asian-kpop-red-fan-36-3840x2400-4k-wallpaper.jpg",
            "http://papers.co/wallpaper/papers.co-hr13-girl-taylor-swift-white-artist-36-3840x2400-4k-wallpaper.jpg",
            "http://papers.co/wallpaper/papers.co-hr63-girl-face-film-emma-watson-36-3840x2400-4k-wallpaper.jpg",
            "http://papers.co/wallpaper/papers.co-hr65-peace-girl-tzuyu-cute-twice-36-3840x2400-4k-wallpaper.jpg",
            "http://papers.co/wallpaper/papers.co-hr67-kpop-girl-cute-face-36-3840x2400-4k-wallpaper.jpg",
            "http://papers.co/wallpaper/papers.co-hr52-kpop-krystal-girl-green-face-36-3840x2400-4k-wallpaper.jpg",
            "http://papers.co/wallpaper/papers.co-hr48-kpop-girl-asian-face-pretty-36-3840x2400-4k-wallpaper.jpg",
            "http://papers.co/wallpaper/papers.co-hr43-sana-girl-twice-blue-summer-36-3840x2400-4k-wallpaper.jpg",
            "http://papers.co/wallpaper/papers.co-hq82-irene-girl-kpop-asian-blue-summer-beauty-idol-36-3840x2400-4k-wallpaper.jpg",
            "http://papers.co/wallpaper/papers.co-hr98-jennifer-lawrence-girl-film-green-36-3840x2400-4k-wallpaper.jpg",
            "http://papers.co/wallpaper/papers.co-hr92-girl-nana-bw-beauty-kpop-36-3840x2400-4k-wallpaper.jpg",
            "http://papers.co/wallpaper/papers.co-hr84-kpop-girl-trench-coat-36-3840x2400-4k-wallpaper.jpg",
            "http://papers.co/wallpaper/papers.co-hr52-kpop-krystal-girl-green-face-36-3840x2400-4k-wallpaper.jpg"
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

    private class AdapterDataLoadListenerImpl implements TiwBaseFoodAdapter.DataLoadListener {
        @Override
        public void loadMoreData(int position, @NotNull View view) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    while (true) {
                        if (mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || !mRecyclerView.isComputingLayout()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tiwBaseFoodAdapter.setHaveMoreData(false);
                                    setContentData();

                                }
                            });
                            break;
                        }

                    }
                }
            }).start();
        }
    }
}
