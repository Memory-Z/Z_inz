package com.inz.z.view.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.inz.z.base.IViewTemplate;

/**
 * 基础 Activity 类
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/23 16:38.
 */
public abstract class AbsBaseActivity extends AppCompatActivity implements IViewTemplate {

    /**
     * 上下文
     */
    Context mContext;

    /* / -- IViewTemplate - Start -- / */
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

    @Override
    public Context getContext() {
        return mContext;
    }
    /* / -- IViewTemplate - End -- / */
}
