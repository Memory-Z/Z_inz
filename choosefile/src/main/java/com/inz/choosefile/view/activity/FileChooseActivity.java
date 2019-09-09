package com.inz.choosefile.view.activity;

import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.inz.choosefile.R;
import com.inz.choosefile.view.fragment.ChooseFileFragment;
import com.inz.z.base.view.AbsBaseActivity;
import com.inz.z.base.view.widget.BaseNavLayout;

/**
 * 文件选择
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/09/04 10:33.
 */
@Route(path= "/fileChoose/vop", group = "fileChoose")
public class FileChooseActivity extends AbsBaseActivity {
    private static final String TAG = "FileChooseActivity";

    private ViewPager fileChooseViewPager;
    private TabLayout typeTabLayout;
    private BaseNavLayout fileNavLayout;
    private RelativeLayout navRl;


    @Override
    protected void initWindow() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.file_choose_activity;
    }

    @Override
    protected void initView() {
        fileChooseViewPager = findViewById(R.id.file_choose_activity_vp);
        typeTabLayout = findViewById(R.id.file_choose_activity_top_tabl);
        fileNavLayout = findViewById(R.id.file_choose_activity_top_bnl);
        navRl = findViewById(R.id.file_choose_activity_nav_rl);
        ChooseFileFragment chooseDocFileFragment = ChooseFileFragment.getInstance(ChooseFileFragment.ChooseFileType.DOC);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.file_choose_activity_fl, chooseDocFileFragment, "ChooseFileFragment")
                .commit();

    }

    @Override
    protected void initData() {

    }
}
