package com.inz.z.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.inz.z.R;
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
    public void onCreateView(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        mContext = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showUpload() {

    }


    @Override
    public void initData() {

    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {

    }

    /**
     * 退出视图
     */
    private void quitView() {

    }
}
