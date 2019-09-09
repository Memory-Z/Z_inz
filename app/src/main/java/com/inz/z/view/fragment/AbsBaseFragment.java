package com.inz.z.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inz.z.base.IBaseView;
import com.inz.z.util.AppBaseTools;
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
    protected Context mContext;
    public View mView;
    private Dialog loadDialog;
    protected LayoutInflater mInflater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        } else {
            mView = inflater.inflate(getLayoutId(), container, false);
            mInflater = inflater;
        }
        return mView;
    }

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
            loadDialog = AppBaseTools.loadDialog(mContext);
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
        AppBaseTools.showShortCenterToast(mContext, msg);
        Logger.t(TAG).i(mContext.getPackageName() + "; showToast: " + msg);
    }

    @Override
    public void showError(Throwable e) {
        AppBaseTools.showShortCenterToast(mContext, e.getMessage());
        Logger.t(TAG).e(mContext.getPackageName() + "; showError: " + e.getMessage());
    }
    /* / -- IBaseView - End -- / */

    /**
     * 获取布局ID
     *
     * @return 布局ID
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

}
