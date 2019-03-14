package com.inz.z.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inz.z.R;
import com.inz.z.view.fragment.AbsBaseFragment;

/**
 * ExampleActivity 装载 Example Fragment
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/12/31 14:34.
 */
public class ExMainActivity extends AbsBaseActivity {

    private TextView titleTv;

    private String clzName;
    private ShowType showType;
    private int showTypeValue;
    // 顶部 栏
    private ConstraintLayout topCl;

    /**
     * 显示类型
     */
    public enum ShowType {
        FULL_SCREEN(0);

        private int type;

        ShowType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

    }

    @Override
    public void onCreateZ(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_ex_main);
    }

    @Override
    public void initView() {
//        contentFrameLayout = findViewById(R.id.ex_main_frame_content);
        LinearLayout backLl = findViewById(R.id.base_top_back_ll);
        backLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitExMain();
            }
        });
        titleTv = findViewById(R.id.base_top_title_tv);
        topCl = findViewById(R.id.ex_main_top_nav_inc);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            clzName = bundle.getString("clzName", "");
            showTypeValue = bundle.getInt("showType", ShowType.FULL_SCREEN.ordinal());
        }
        loadFragment();
    }

    @Override
    public boolean myOnKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * 加载 Fragment
     */
    private void loadFragment() {
        if ("".equals(clzName)) {
            quitExMain();
        }
        // 获取显示 类型
        getShowType();
        try {
            Class clz = Class.forName(clzName);
            Object clzObj = clz.newInstance();
            if (clzObj instanceof AbsBaseFragment) {
                // 判断是否为 Fragment
                titleTv.setText(clz.getSimpleName());
                AbsBaseFragment fragment = (AbsBaseFragment) clzObj;
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.ex_main_frame_content, fragment, clz.getSimpleName());
                transaction.commit();
            } else if (clzObj instanceof AbsBaseActivity) {
                // 判断是否为 Activity
                Intent intent = new Intent(mContext, clzObj.getClass());
                startActivity(intent);
                quitExMain();
            } else {
                titleTv.setText("未适配当前界面");
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            quitExMain();
        }
    }

    /**
     * 获取显示类型
     */
    private void getShowType() {
        if (showTypeValue == ShowType.FULL_SCREEN.ordinal()) {
            showType = ShowType.FULL_SCREEN;
            topCl.setVisibility(View.GONE);
        }
    }

    /**
     * 退出 ExMain 界面
     */
    private void quitExMain() {
        ExMainActivity.this.finish();
    }
}
