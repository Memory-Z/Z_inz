package com.inz.z_inz.view.fragment;

import android.support.v4.app.Fragment;

import com.inz.z_inz.base.IViewTemplate;

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
