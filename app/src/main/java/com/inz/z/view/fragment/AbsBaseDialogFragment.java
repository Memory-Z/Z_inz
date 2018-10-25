package com.inz.z.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.inz.z.base.IViewTemplate;

/**
 * 弹窗
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/25 16:43.
 */
public abstract class AbsBaseDialogFragment extends DialogFragment implements IViewTemplate {

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
