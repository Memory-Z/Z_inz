package com.inz.z.base.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

/**
 * icon 图片布局
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/04 14:43.
 */
// TODO: 2019/11/04 添加 图标 ， 上图下文字，
@Deprecated
public class BaseIconImageView extends RelativeLayout {
    /**
     * 是否存在标记
     */
    private boolean haveBadge = false;
    /**
     * 标记类型， 默认： 红点
     */
    private IconBadgeType badgeType = IconBadgeType.DOT;
    /**
     * 显示 数字类型 时，数字
     */
    private int badgeNumber = 0;
    /**
     * 是否存在标题
     */
    private boolean haveTitle = false;
    /**
     * 标题
     */
    private String titleStr = "";
    /**
     * 图标icon
     */
    private ImageView iconIv;

    /**
     * 标记 类型
     */
    enum IconBadgeType {
        /**
         * 点
         */
        DOT,
        /**
         * 数
         */
        NUMBER
    }


    public BaseIconImageView(Context context) {
        this(context, null);
    }

    public BaseIconImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseIconImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置样式
     */
    private void initStyle() {

    }


    private void initView() {

    }






    /* ======================================= Setter / Getter ======================================= */

    public void setHaveBadge(boolean haveBadge) {
        this.haveBadge = haveBadge;
    }

    public void setBadgeType(IconBadgeType badgeType) {
        this.badgeType = badgeType;
    }

    public void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    public void setHaveTitle(boolean haveTitle) {
        this.haveTitle = haveTitle;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public ImageView getIconIv() {
        return iconIv;
    }
    /* ======================================= Setter / Getter ======================================= */
}
