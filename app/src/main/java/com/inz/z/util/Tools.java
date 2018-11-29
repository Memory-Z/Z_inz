package com.inz.z.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.StringRes;
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

    public static DateFormat baseDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    public static DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
    public static DateFormat dateFormatYMD = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    public static DateFormat dateFormatYM = new SimpleDateFormat("yyyy-MM", Locale.CHINA);

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
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
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
        ImageView imageView = view.findViewById(R.id.static_dialog_loading_iv);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.static_loading_animation);
        imageView.startAnimation(animation);
        return new AlertDialog.Builder(context)
                .setView(view)
                .create();
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
