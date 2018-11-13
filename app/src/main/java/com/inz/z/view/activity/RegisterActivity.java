package com.inz.z.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.inz.z.R;
import com.inz.z.view.IRegisterView;
import com.inz.z.view.fragment.CheckEmailDialogFragment;
import com.inz.z.view.fragment.ThirdLoginDialogFragment;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/23 19:34.
 */
public class RegisterActivity extends AbsBaseActivity implements IRegisterView {
    private static final String TAG = "RegisterActivity";


    private DialogFragment thirdDialogFragment;
    private DialogFragment checkEmailDialogFragment;

    @Override
    public void onCreateZ(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        mContext = this;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        // 第三方登录按钮
        LinearLayout thirdLoginLl = findViewById(R.id.register_other_account_ll);
        thirdLoginLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdDialogFragment = (DialogFragment) getSupportFragmentManager().findFragmentByTag("thirdLoginDialogFragment");
                if (thirdDialogFragment == null) {
                    thirdDialogFragment = new ThirdLoginDialogFragment();
                    thirdDialogFragment.show(getSupportFragmentManager(), "thirdLoginDialogFragment");
                }
            }
        });
        Button registerBtn = findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailDialogFragment = (DialogFragment) getSupportFragmentManager().findFragmentByTag("checkEmailDialogFragment");
                if (checkEmailDialogFragment == null) {
                    checkEmailDialogFragment = new CheckEmailDialogFragment();
                    checkEmailDialogFragment.show(getSupportFragmentManager(), "checkEmailDialogFragment");
                }
            }
        });
        RelativeLayout toLoginRl = findViewById(R.id.register_to_login_rl);
        toLoginRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                quitView();
            }
        });
    }

    @Override
    public boolean myOnKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * 退出 视图
     */
    private void quitView() {
        RegisterActivity.this.finish();
    }

}
