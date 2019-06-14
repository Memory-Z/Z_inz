package com.inz.z.music.view.activity;

import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.Window;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.inz.z.base.view.widget.BaseTopActionLayout;
import com.inz.z.music.R;
import com.inz.z.music.base.AbsBaseActivity;
import com.inz.z.music.view.adapter.AlbumRvAdapter;
import com.inz.z.music.view.adapter.ItemAlbumBean;
import com.inz.z.music.view.adapter.TopPlayerAdapter;
import com.inz.z.music.view.adapter.ItemTopPlayerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/12 11:57.
 */
@Route(path = "/music/mainActivity", group = "music")
public class MainActivity extends AbsBaseActivity {
    private static final String TAG = "MainActivity";
    protected int statusBarHeight;

    private Window window;
    /**
     * 顶部
     */
    private RecyclerView topRv;
    private TopPlayerAdapter topPlayerAdapter;
    private List<ItemTopPlayerBean> itemTopPlayerBeanList;
    private BaseTopActionLayout topActionLayout;

    private List<ItemAlbumBean> recentlyItemAlbumBeanList;
    /**
     * 最近
     */
    private AlbumRvAdapter recentlyAlbumRvAdapter;
    private RecyclerView recentlyRv;

    @Override
    protected void initWindow() {
        window = getWindow();
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.main_activity;
    }

    @Override
    protected void initView() {
        topActionLayout = findViewById(R.id.main_action_btal);
        setSupportActionBar(topActionLayout.getToolbar());
        setTopActionLayoutHeight();

        topRv = findViewById(R.id.main_top_play_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        topRv.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL));
        topRv.setLayoutManager(layoutManager);
        topPlayerAdapter = new TopPlayerAdapter(mContext);
        topRv.setAdapter(topPlayerAdapter);

        recentlyRv = findViewById(R.id.main_recently_rv);
        LinearLayoutManager recentlyLm = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recentlyRv.setLayoutManager(recentlyLm);
        recentlyAlbumRvAdapter = new AlbumRvAdapter(mContext, 6);
        recentlyRv.setAdapter(recentlyAlbumRvAdapter);
    }

    /**
     * 设置ActionBar 高度
     */
    private void setTopActionLayoutHeight() {
        int statusBarHeightId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        statusBarHeight = getResources().getDimensionPixelSize(statusBarHeightId);
//        topActionLayout.setPadding(topActionLayout.getPaddingLeft(), topActionLayout.getPaddingTop() + statusBarHeight, topActionLayout.getPaddingRight(), topActionLayout.getPaddingBottom());
        topActionLayout.setStatusBarHeight(statusBarHeight);
        ViewGroup.LayoutParams layoutParams = topActionLayout.getLayoutParams();
        layoutParams.height = layoutParams.height + statusBarHeight;
        topActionLayout.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {
        itemTopPlayerBeanList = new ArrayList<>(16);
        String[] imageSrcArray = getResources().getStringArray(R.array.image_array);
        for (int i = 0; i < 10; i++) {
            ItemTopPlayerBean bean = new ItemTopPlayerBean();
            if (i > 5) {
                bean.setMinPhotoStyle(ItemTopPlayerBean.MinStyle.MUSIC);
            }
            bean.setPhotoName("Player - " + i);
            bean.setPhotoUrlStr(imageSrcArray[i % imageSrcArray.length]);
            itemTopPlayerBeanList.add(bean);
        }
        topPlayerAdapter.refreshData(itemTopPlayerBeanList);

        recentlyItemAlbumBeanList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ItemAlbumBean bean = new ItemAlbumBean();
            bean.setAlbumSrc(imageSrcArray[i % imageSrcArray.length]);
            bean.setPlayerName("player name " + i);
            bean.setTitleName(" Title Name " + i);
            recentlyItemAlbumBeanList.add(bean);
        }
        recentlyAlbumRvAdapter.refreshData(recentlyItemAlbumBeanList);
    }

    @Nullable
    @Override
    protected String[] needRequestPermission() {
        return new String[0];
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
