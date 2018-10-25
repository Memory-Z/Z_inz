package com.inz.z.util;

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
}
