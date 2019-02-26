package com.inz.z.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.inz.z.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/25 9:16.
 */
public class Tools {

    private Tools() {
    }

    private static DateFormat baseDateFormat;
    private static DateFormat dateFormatTime;
    private static DateFormat dateFormatYMD;
    private static DateFormat dateFormatYM;
    private static DateFormat dateFormatMD;
    private static DateFormat dateFormatY;

    public static DateFormat getBaseDateFormat() {
        if (baseDateFormat == null) {
            baseDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        }
        return baseDateFormat;
    }

    public static DateFormat getDateFormatTime() {
        if (dateFormatTime == null) {
            dateFormatTime = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        }
        return dateFormatTime;
    }

    public static DateFormat getDateFormatYMD() {
        if (dateFormatYMD == null) {
            dateFormatYMD = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        }
        return dateFormatYMD;
    }

    public static DateFormat getDateFormatYM() {
        if (dateFormatYM == null) {
            dateFormatYM = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        }
        return dateFormatYM;
    }

    public static DateFormat getDateFormatMD() {
        if (dateFormatMD == null) {
            dateFormatMD = new SimpleDateFormat("MM-dd", Locale.CHINA);
        }
        return dateFormatMD;
    }

    public static DateFormat getDateFormatY() {
        if (dateFormatY == null) {
            dateFormatY = new SimpleDateFormat("yyyy", Locale.CHINA);
        }
        return dateFormatY;
    }

    /**
     * 校验 邮箱格式是否正确
     *
     * @param string 邮箱地址
     * @return 是否正确
     */
    public static boolean isEmail(String string) {
        if (string == null)
            return false;
        // ^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$
        //正常邮箱 /^\w+((-\w)|(\.\w))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/
        // 含有特殊 字符的 个人邮箱  和 正常邮箱
        // js: 个人邮箱     /^[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+@[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+(\.[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+)+$/
        // java：个人邮箱  [\\w.\\\\+\\-\\*\\/\\=\\`\\~\\!\\#\\$\\%\\^\\&\\*\\{\\}\\|\\'\\_\\?]+@[\\w.\\\\+\\-\\*\\/\\=\\`\\~\\!\\#\\$\\%\\^\\&\\*\\{\\}\\|\\'\\_\\?]+\\.[\\w.\\\\+\\-\\*\\/\\=\\`\\~\\!\\#\\$\\%\\^\\&\\*\\{\\}\\|\\'\\_\\?]+
        // 范围 更广的 邮箱验证 "/^[^@]+@.+\\..+$/"
        String regEx1 = "^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        return m.matches();
    }

    /**
     * 校验 手机号是否正确
     *
     * @param mobileNumber 手机号
     * @return 是否正确
     */
    public static boolean checkMobileNumber(String mobileNumber) {
        Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
        Matcher matcher = regex.matcher(mobileNumber);
        return matcher.matches();
    }

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

    /**
     * 将 dp 转换成 px
     *
     * @return px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
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

    private static Toast toast;

    public static void showShortBottomToast(Context context, String content) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    public static void showShortBottomToast(Context context, @StringRes int resId) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    public static void showShortCenterToast(Context context, String content) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showShortCenterToast(Context context, @StringRes int resId) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
