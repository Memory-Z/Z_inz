package com.inz.z.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inz.z.R;
import com.inz.z.view.adapter.TiwBaseFoodAdapter;
import com.inz.z.view.widget.TiwActionLayout;

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

    @Override
    protected void initWindow() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.tiw_base_layout;
    }

    @Override
    protected void initView() {
        tiwActionLayout = findViewById(R.id.tiw_base_tal);
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
    }

    @Override
    public boolean myOnKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
