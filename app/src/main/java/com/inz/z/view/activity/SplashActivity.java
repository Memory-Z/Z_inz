package com.inz.z.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inz.z.R;
import com.inz.z.entity.Constants;
import com.inz.z.util.SPHelper;

/**
 * 闪屏
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/11/6 10:06.
 */
public class SplashActivity extends AbsBaseActivity {

    /**
     * 倒计时
     */
    private CountDownTimer countDownTimer;

    @Override
    public void onCreateZ(@Nullable Bundle savedInstanceState) {
        // 隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mContext = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void initView() {
        ImageView imageView = findViewById(R.id.splash_iv);
        Glide.with(mContext).load(getResources().getDrawable(R.drawable.adolescent_casual_contemporary_1030895)).into(imageView);
    }

    @Override
    public void initData() {
        countDownTimer = new CountDownTimer(1000 + 500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                boolean isHaveUser = SPHelper.getInstance().isHaveUser();
                Intent intent = new Intent();
//                if (isHaveUser) {
                intent.setClass(mContext, MainActivity.class);
//                } else {
//                    intent.setClass(mContext, LoginActivity.class);
//                }
                startActivity(intent);
                finish();
            }
        };
        countDownTimer.start();
        // 创建 文件
        Constants.createFile();
    }

    @Override
    public boolean myOnKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
