package com.inz.z.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.inz.z.R;
import com.inz.z.presenter.RegisterPresenter;
import com.inz.z.util.Tools;
import com.inz.z.view.IRegisterView;
import com.inz.z.view.fragment.dialog.CheckEmailDialogFragment;
import com.inz.z.view.fragment.dialog.ThirdLoginDialogFragment;
import com.orhanobut.logger.Logger;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/23 19:34.
 */
public class RegisterActivity extends AbsBaseActivity implements IRegisterView, CheckEmailDialogFragment.FragmentRegisterInterface {
    private static final String TAG = "RegisterActivity";


    private DialogFragment thirdDialogFragment;
    private CheckEmailDialogFragment checkEmailDialogFragment;
    private RegisterPresenter registerPresenter;
    private EditText userNameEt, userPasswordEt;

    private String userName, userPassword;
    private String userEmail;
    private String userId;

    @Override
    public void onCreateZ(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        mContext = this;
        registerPresenter = new RegisterPresenter();
        registerPresenter.attachView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerPresenter.detachView();
    }


    @Override
    public void initView() {
        userNameEt = findViewById(R.id.register_user_name_et);
        userPasswordEt = findViewById(R.id.register_user_password_et);
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
                userName = userNameEt.getText().toString();
                userPassword = userPasswordEt.getText().toString();
                if (!"".equals(userName) && !"".equals(userPassword)) {
                    registerPresenter.register(userName, userPassword);
                } else {
                    Tools.showShortCenterToast(mContext, "用户名或密码不能为空！");
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
    public void initData() {

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

    /* / - 接口实现 - / */

    @Override
    public void setIsRegister(boolean isRegister) {
        if (isRegister) {
            registerPresenter.registerLogin(userName, userPassword);
        }
    }

    @Override
    public void setIsSend(boolean isSend) {
        Logger.i(" 邮箱验证码 发送 状态： " + isSend);
    }

    @Override
    public void setRegisterUserId(String userId) {
        this.userId = userId;
        checkEmailDialogFragment = (CheckEmailDialogFragment) getSupportFragmentManager().findFragmentByTag("checkEmailDialogFragment");
        if (checkEmailDialogFragment == null) {
            checkEmailDialogFragment = new CheckEmailDialogFragment();
        }
        checkEmailDialogFragment.show(getSupportFragmentManager(), "checkEmailDialogFragment");
        checkEmailDialogFragment.setFragmentRegisterInterface(RegisterActivity.this);
    }

    @Override
    public void setCheckRegister(boolean checkRegister) {
        if (checkRegister) {
            // 检测通过
            if (checkEmailDialogFragment != null) {
                checkEmailDialogFragment.dismiss();
                Tools.showShortCenterToast(mContext, "邮箱验证成功，请登录");
            }
        }

    }
    /* / - 接口实现 - / */
    /* / - CheckEmailDialogFragment.interface -  / */

    @Override
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        registerPresenter.sendRegisterCode(userId, userEmail);
    }

    @Override
    public void setEmailCode(String emailCode) {
        registerPresenter.checkRegisterCode(userId, emailCode, userEmail);
    }
    /* / - CheckEmailDialogFragment.interface -  / */

}
