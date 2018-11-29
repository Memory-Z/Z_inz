package com.inz.z.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.View;

import com.inz.z.base.IBaseView;
import com.inz.z.util.Tools;
import com.orhanobut.logger.Logger;

/**
 * 基础弹窗 Fragment
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/25 16:43.
 */
public abstract class AbsBaseDialogFragment extends DialogFragment implements IBaseView {

    Context mContext;
    View mView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    /* / -- IBaseView - Start -- / */
    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showToast(String msg) {
        Tools.showShortCenterToast(mContext, msg);
        Logger.i(mContext.getPackageName() + "; showToast: " + msg);
    }

    @Override
    public void showError(String errorMsg) {
        Tools.showShortCenterToast(mContext, errorMsg);
        Logger.e(mContext.getPackageName() + "; showError: " + errorMsg);
    }
    /* / -- IBaseView - End -- / */

    /**
     * 初始化视图
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

}
