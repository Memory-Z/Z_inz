package com.inz.z.view.activity;import android.databinding.DataBindingUtil;import android.os.Bundle;import android.support.annotation.NonNull;import android.support.annotation.Nullable;import android.support.design.widget.NavigationView;import android.support.v4.widget.DrawerLayout;import android.support.v7.app.ActionBarDrawerToggle;import android.support.v7.widget.Toolbar;import android.view.KeyEvent;import android.view.MenuItem;import android.view.View;import android.widget.Toast;import com.inz.z.R;import com.inz.z.databinding.ActivityMainBinding;import com.inz.z.databinding.IncludeToolbarBinding;import com.inz.z.databinding.NavigationHeaderBinding;import com.inz.z.view.fragment.AbsBackHandledFragment;import com.inz.z.view.fragment.AbsBaseFragment;import com.inz.z.view.fragment.ExampleFragment;import com.inz.z.view.fragment.MyPlanFragment;import de.hdodenhof.circleimageview.CircleImageView;/** * 主界面 * Create by 116546 * * @author Zhenglj */public class MainActivity extends AbsBaseActivity {    private DrawerLayout mDrawerLayout;    private ActionBarDrawerToggle mDrawerToggle;    private Toolbar mToolbar;    // 抽象类 选中的 框架    private AbsBackHandledFragment selectFragment;    private NavigationView mNavigationView;    private ActivityMainBinding activityMainBinding;    // 工具栏 动画 时间    private static final int ANIM_DURATION_TOOLBAR = 300;    @Override    public void onCreateZ(@Nullable Bundle savedInstanceState) {        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);        mContext = this;    }    /**     * 初始化 视图     */    @Override    public void initView() {        IncludeToolbarBinding includeToolbarBinding = activityMainBinding.mainIncludeToolbar;        mToolbar = includeToolbarBinding.toolbar;        setSupportActionBar(mToolbar);        mDrawerLayout = activityMainBinding.mainDrawerLayout;        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);        mDrawerToggle.syncState();        mDrawerLayout.addDrawerListener(mDrawerToggle);        mNavigationView = activityMainBinding.mainNv;        setupDrawerContent(mNavigationView);    }    @Override    public void initData() {        requestPermission();        checkUpdate();    }    @Override    public boolean myOnKeyDown(int keyCode, KeyEvent event) {        return false;    }    /**     * 设置 导航头     */    private void setNavHeader() {//        View headerView = mNavigationView.findViewById(R.id.nav_header_user_name_tv);        NavigationHeaderBinding navigationHeaderBinding = DataBindingUtil.bind(mNavigationView.getHeaderView(0));        if (navigationHeaderBinding != null) {            CircleImageView circleImageView = navigationHeaderBinding.navHeaderPhotoCiv;            circleImageView.setOnClickListener(new View.OnClickListener() {                @Override                public void onClick(View v) {                }            });        }    }    private ExampleFragment exampleFragment;    private MyPlanFragment myPlanFragment;    /**     * 设置 点击 NavigationView 中的 Item 切换 DrawerLayout 中 内容     *     * @param navigationView 导航视图     */    private void setupDrawerContent(NavigationView navigationView) {        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {            @Override            public boolean onNavigationItemSelected(@NonNull MenuItem item) {                switch (item.getItemId()) {                    case R.id.navigation_item_home:                        Toast.makeText(mContext, "提示", Toast.LENGTH_SHORT).show();                        break;                    case R.id.navigation_item_example:                        if (exampleFragment == null) {                            exampleFragment = new ExampleFragment();                        }                        switchToFragment(exampleFragment, "ExampleFragment");                        break;                    case R.id.navigation_item_person_center:                        break;                    case R.id.navigation_item_my_plan:                        if (myPlanFragment == null) {                            myPlanFragment = new MyPlanFragment();                        }                        switchToFragment(myPlanFragment, "MyPlanFragment");                        break;                    case R.id.navigation_item_my_photo_album:                        break;                    case R.id.navigation_item_my_file:                        break;                    case R.id.navigation_item_setting:                        break;                    case R.id.navigation_item_about:                        break;                }                item.setChecked(true);                mDrawerLayout.closeDrawers();                return true;            }        });    }    /**     * 切换框架 中的 布局内容     *     * @param absBaseFragment 框架     * @param tag             框架标题     */    private void switchToFragment(AbsBaseFragment absBaseFragment, String tag) {        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, absBaseFragment).commit();        mToolbar.setTitle(tag);    }}