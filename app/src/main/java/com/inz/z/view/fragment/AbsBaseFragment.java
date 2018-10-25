package com.inz.z.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.inz.z.base.IViewTemplate;

/**
 * 基础 Fragment 类
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create By 2018/7/22 18:06
 */
public abstract class AbsBaseFragment extends Fragment implements IViewTemplate {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showError() {

    }
}
