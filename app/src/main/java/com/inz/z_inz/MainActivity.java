package com.inz.z_inz;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.inz.z_inz.databinding.IncludeToolbarBinding;
import com.inz.z_inz.databinding.NavigationHeaderBinding;
import com.inz.z_inz.fragment.BaseFragment;
import com.inz.z_inz.widget.AbsBackHandledFragment;
import com.inz.z_inz.databinding.ActivityMainBinding;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Context mContext;
    private Toolbar mToolbar;
    // 抽象类 选中的 框架
    private AbsBackHandledFragment selectFragment;
    private NavigationView mNavigationView;
    private ActivityMainBinding activityMainBinding;

    // 工具栏 动画 时间
    private static final int ANIM_DURATION_TOOLBAR = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mContext = this;
        initView();
        setupDrawerContent(mNavigationView);
    }

    /**
     * 初始化 视图
     */
    private void initView() {
        IncludeToolbarBinding includeToolbarBinding = activityMainBinding.mainIncludeToolbar;
        mToolbar = includeToolbarBinding.toolbar;
        setSupportActionBar(mToolbar);
        mDrawerLayout = activityMainBinding.mainDrawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mNavigationView = activityMainBinding.mainNv;
    }

    private void setNavHeader() {
//        View headerView = mNavigationView.findViewById(R.id.nav_header_user_name_tv);
        NavigationHeaderBinding navigationHeaderBinding = DataBindingUtil.bind(mNavigationView.getHeaderView(0));
        if (navigationHeaderBinding != null) {
            CircleImageView circleImageView = navigationHeaderBinding.navHeaderPhotoCiv;
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    /**
     * 设置 点击 NavigationView 中的 Item 切换 DrawerLayout 中 内容
     *
     * @param navigationView 导航视图
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_item_home:
                        break;
                    case R.id.navigation_item_example:
                        break;
                    case R.id.navigation_item_person_center:
                        break;
                    case R.id.navigation_item_my_photo_album:
                        break;
                    case R.id.navigation_item_my_file:
                        break;
                    case R.id.navigation_item_setting:
                        break;
                    case R.id.navigation_item_about:
                        break;
                }
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    /**
     * 切换框架 中的 布局内容
     *
     * @param baseFragment 框架
     * @param tag          框架标题
     */
    private void switchToFragment(BaseFragment baseFragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, baseFragment).commit();
        mToolbar.setTitle(tag);
    }
}
