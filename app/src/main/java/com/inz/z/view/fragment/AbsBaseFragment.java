package com.inz.z.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.inz.z.base.IBaseView;
import com.inz.z.util.Tools;
import com.orhanobut.logger.Logger;

/**
 * 基础 Fragment 类
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create By 2018/7/22 18:06
 */
public abstract class AbsBaseFragment extends Fragment implements IBaseView {

    private static final String TAG = "AbsBaseFragment";
    public Context mContext;
    public View mView;
    private Dialog loadDialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
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
        if (loadDialog == null) {
            loadDialog = Tools.loadDialog(mContext);
        }
        loadDialog.show();
        Logger.t(TAG).i(mContext.getPackageName() + "; showLoading.");
    }

    @Override
    public void hideLoading() {
        if (loadDialog != null) {
            loadDialog.dismiss();
            loadDialog = null;
        }
        Logger.t(TAG).i(mContext.getPackageName() + "; hideLoading.");
    }

    @Override
    public void showToast(String msg) {
        Tools.showShortCenterToast(mContext, msg);
        Logger.t(TAG).i(mContext.getPackageName() + "; showToast: " + msg);
    }

    @Override
    public void showError(Throwable e) {
        Tools.showShortCenterToast(mContext, e.getMessage());
        Logger.t(TAG).e(mContext.getPackageName() + "; showError: " + e.getMessage());
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
