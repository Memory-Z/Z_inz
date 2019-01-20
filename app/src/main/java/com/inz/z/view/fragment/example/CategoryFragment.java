package com.inz.z.view.fragment.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inz.z.R;
import com.inz.z.view.adapter.example.CategoryFragmentAdapter;
import com.inz.z.view.fragment.AbsBaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/1/3 18:39.
 */
public class CategoryFragment extends AbsBaseFragment {

    private TabLayout categoryTabL;
    private ViewPager categoryVp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ex_fragment_shop_category, container, false);
    }

    @Override
    public void initView() {
        categoryTabL = mView.findViewById(R.id.ex_fragment_shop_category_tl);
        categoryVp = mView.findViewById(R.id.ex_fragment_shop_category_vp);
    }

    @Override
    public void initData() {

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new ItemCategoryFragment());
        fragmentList.add(new ItemCategoryFragment());
        fragmentList.add(new ItemCategoryFragment());
        fragmentList.add(new ItemCategoryFragment());
        List<String> titleList = new ArrayList<>();
        titleList.add("分类1");
        titleList.add("分类2");
        titleList.add("分类3");
        titleList.add("分类4");
        CategoryFragmentAdapter categoryFragmentAdapter = new CategoryFragmentAdapter(getFragmentManager(), fragmentList, titleList);
        categoryVp.setAdapter(categoryFragmentAdapter);
        categoryTabL.setupWithViewPager(categoryVp);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        categoryTabL.setMinimumWidth(widthPixels);
//        for (int i =0 ; i < categoryTabL.getTabCount(); i++) {
//            TabLayout.Tab tab = categoryTabL.getTabAt(i);
//            TabView = tab.view;
//        }


    }
}
