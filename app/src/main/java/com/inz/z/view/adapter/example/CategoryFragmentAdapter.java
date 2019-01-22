package com.inz.z.view.adapter.example;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 分类 Fragment 适配
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/1/3 20:02.
 */
public class CategoryFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private List<String> titleList;

    public CategoryFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titleList) {
        super(fm);
        this.fragmentList = fragments;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
