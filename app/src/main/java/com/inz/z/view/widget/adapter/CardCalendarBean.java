package com.inz.z.view.widget.adapter;

import android.support.annotation.NonNull;

import com.inz.z.R;

/**
 * BaseCardCalendarLayout  Item 对应 对象
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/2/26 22:36.
 */
public class CardCalendarBean {

    private String day = "1";
    private boolean isShowLeftDot = false;
    private int leftDotRes = R.drawable.ic_vd_dot_green;
    private boolean isShowBottomLine = false;
    private boolean isSelected = false;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isShowLeftDot() {
        return isShowLeftDot;
    }

    public void setShowLeftDot(boolean showLeftDot) {
        isShowLeftDot = showLeftDot;
    }

    public int getLeftDotRes() {
        return leftDotRes;
    }

    public void setLeftDotRes(int leftDotRes) {
        this.leftDotRes = leftDotRes;
    }

    public boolean isShowBottomLine() {
        return isShowBottomLine;
    }

    public void setShowBottomLine(boolean showBottomLine) {
        isShowBottomLine = showBottomLine;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
