package com.inz.z.view.fragment.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.View;

import com.inz.z.base.IBaseView;
import com.inz.z.util.AppBaseTools;
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
        AppBaseTools.showShortCenterToast(mContext, msg);
        Logger.i(mContext.getPackageName() + "; showToast: " + msg);
    }

    @Override
    public void showError(Throwable e) {
        AppBaseTools.showShortCenterToast(mContext, e.getMessage());
        Logger.e(mContext.getPackageName() + "; showError: " + e.getMessage());
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
