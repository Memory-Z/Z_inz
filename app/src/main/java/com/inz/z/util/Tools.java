package com.inz.z.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.inz.z.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/25 9:16.
 */
public class Tools {

    private Tools() {
    }

    public static DateFormat baseDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    public static DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
    public static DateFormat dateFormatYMD = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    public static DateFormat dateFormatYM = new SimpleDateFormat("yyyy-MM", Locale.CHINA);

    /**
     * 加载 弹窗
     *
     * @param context 上下文
     * @return Dialog
     */
    public static Dialog loadDialog(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_register_email, null, false);
        ImageView imageView = view.findViewById(R.id.static_dialog_loading_iv);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.static_loading_animation);
        imageView.startAnimation(animation);
        return new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false)
                .create();
    }

    /**
     * 显示 提示
     *
     * @param context 上下文
     * @param msg     内容
     */
    public static void showShortToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
