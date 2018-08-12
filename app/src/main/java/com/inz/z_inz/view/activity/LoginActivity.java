package com.inz.z_inz.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.inz.z_inz.view.ILoginView;

/**
 * 登录界面
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * @date Create By 2018/8/12 14:54
 */
public class LoginActivity extends AppCompatActivity implements ILoginView {
    private static final String TAG = "LoginActivity";
    /**
     * 上下文
     */
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

    }

    // -----ILoginView-----//
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
        return null;
    }
    // -----ILoginView-----//
}
