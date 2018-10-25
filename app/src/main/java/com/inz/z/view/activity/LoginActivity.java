package com.inz.z.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.inz.z.view.ILoginView;

/**
 * 登录界面
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create By 2018/8/12 14:54
 */
public class LoginActivity extends AbsBaseActivity implements ILoginView {
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    public void showUpload() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
