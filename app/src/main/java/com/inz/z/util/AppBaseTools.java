package com.inz.z.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.inz.z.R;
import com.inz.z.base.util.BaseTools;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/25 9:16.
 */
public class AppBaseTools extends BaseTools {

    /**
     * 加载 弹窗
     *
     * @param context 上下文
     * @return Dialog
     */
    public static Dialog loadDialog(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.static_dialog_loading, null, false);
//        RelativeLayout loadingRl = view.findViewById(R.id.static_dialog_loading_rl);
//        ImageView imageView = view.findViewById(R.id.static_dialog_loading_badge_iv);
//        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
//        imageView.setVisibility(View.GONE);
//        new MyCountDownTimer(600, 100, context, loadingRl, layoutParams).start();
//        Animation animation = AnimationUtils.loadAnimation(context, R.anim.static_loading_animation);
        ImageView imageView = view.findViewById(R.id.static_dialog_loading_iv);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.static_loading_animation);
        imageView.startAnimation(animation);
        return new AlertDialog.Builder(context, R.style.AppTheme_DialogTheme)
                .setView(view)
                .create();
    }

    private static class MyCountDownTimer extends CountDownTimer {

        private Context mContext;
        private RelativeLayout parentRl;
        private ViewGroup.LayoutParams layoutParams;

        MyCountDownTimer(long millisInFuture, long countDownInterval, Context mContext, RelativeLayout parentRl, ViewGroup.LayoutParams layoutParams) {
            super(millisInFuture, countDownInterval);
            this.mContext = mContext;
            this.parentRl = parentRl;
            this.layoutParams = layoutParams;
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            createLoadBadge(mContext, parentRl, layoutParams);
        }
    }

    private static void createLoadBadge(Context context, RelativeLayout parentRl, ViewGroup.LayoutParams layoutParams) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(layoutParams);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate_loading);
        imageView.startAnimation(animation);
        parentRl.addView(imageView);
    }

}
