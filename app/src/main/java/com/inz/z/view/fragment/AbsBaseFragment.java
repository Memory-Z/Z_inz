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

    Context mContext;
    View mView;
    private Dialog loadDialog;

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
        if (loadDialog == null) {
            loadDialog = Tools.loadDialog(mContext);
        }
        loadDialog.show();
        Logger.i(mContext.getPackageName() + "; showLoading.");
    }

    @Override
    public void hideLoading() {
        if (loadDialog != null) {
            loadDialog.dismiss();
            loadDialog = null;
        }
        Logger.i(mContext.getPackageName() + "; hideLoading.");
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

    @Override
    public Context getContext() {
        return mContext;
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
