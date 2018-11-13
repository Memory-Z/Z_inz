package com.inz.z.view.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.inz.z.base.IBaseView;
import com.inz.z.util.Tools;
import com.orhanobut.logger.Logger;

/**
 * 基础 Activity 类
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/23 16:38.
 */
public abstract class AbsBaseActivity extends AppCompatActivity implements IBaseView {

    /**
     * 上下文
     */
    Context mContext;

    private Dialog loadDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateZ(savedInstanceState);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }


    /**
     * OnCreateView
     *
     * @param savedInstanceState Data
     */
    public abstract void onCreateZ(@Nullable Bundle savedInstanceState);

    /**
     * 初始化界面
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

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
        Tools.showShortToast(mContext, msg);
        Logger.i(mContext.getPackageName() + "; showToast: " + msg);
    }

    @Override
    public void showError(String errorMsg) {
        Tools.showShortToast(mContext, errorMsg);
        Logger.e(mContext.getPackageName() + "; showError: " + errorMsg);
    }

    @Override
    public Context getContext() {
        return mContext;
    }
    /* / -- IBaseView - End -- / */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (loadDialog != null && loadDialog.isShowing()) {
                loadDialog.dismiss();
            } else {
                return myOnKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 自定义按钮 点击
     *
     * @param keyCode 点击按键
     * @param event   事件
     * @return 是否拦截
     */
    public abstract boolean myOnKeyDown(int keyCode, KeyEvent event);
}
