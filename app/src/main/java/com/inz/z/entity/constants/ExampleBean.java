package com.inz.z.entity.constants;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

/**
 * 例子 实体数据对象
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/12/30 16:28.
 */
public class ExampleBean {

    private String clzName;

    private boolean haveLeftIcon;
    @DrawableRes
    private int leftIconRes;
    private String contentStr;
    private String contentHintStr;
    private boolean haveRightIcon;
    @DrawableRes
    private int rightIconRes;
    private boolean haveTopLine;
    private boolean haveBottomLine;

    public String getClzName() {
        return clzName;
    }

    public void setClzName(String clzName) {
        this.clzName = clzName;
    }

    public boolean isHaveLeftIcon() {
        return haveLeftIcon;
    }

    public void setHaveLeftIcon(boolean haveLeftIcon) {
        this.haveLeftIcon = haveLeftIcon;
    }

    public int getLeftIconRes() {
        return leftIconRes;
    }

    public void setLeftIconRes(@DrawableRes int leftIconRes) {
        this.leftIconRes = leftIconRes;
    }

    public String getContentStr() {
        return contentStr;
    }

    public void setContentStr(String contentStr) {
        this.contentStr = contentStr;
    }

    public String getContentHintStr() {
        return contentHintStr;
    }

    public void setContentHintStr(String contentHintStr) {
        this.contentHintStr = contentHintStr;
    }

    public boolean isHaveRightIcon() {
        return haveRightIcon;
    }

    public void setHaveRightIcon(boolean haveRightIcon) {
        this.haveRightIcon = haveRightIcon;
    }

    public int getRightIconRes() {
        return rightIconRes;
    }

    public void setRightIconRes(@DrawableRes int rightIconRes) {
        this.rightIconRes = rightIconRes;
    }

    public boolean isHaveTopLine() {
        return haveTopLine;
    }

    public void setHaveTopLine(boolean haveTopLine) {
        this.haveTopLine = haveTopLine;
    }

    public boolean isHaveBottomLine() {
        return haveBottomLine;
    }

    public void setHaveBottomLine(boolean haveBottomLine) {
        this.haveBottomLine = haveBottomLine;
    }

    @NonNull
    @Override
    public String toString() {
        return "ExampleBean{" +
                "clzName='" + clzName + '\'' +
                ", haveLeftIcon=" + haveLeftIcon +
                ", leftIconRes=" + leftIconRes +
                ", contentStr='" + contentStr + '\'' +
                ", contentHintStr='" + contentHintStr + '\'' +
                ", haveRightIcon=" + haveRightIcon +
                ", rightIconRes=" + rightIconRes +
                ", haveTopLine=" + haveTopLine +
                ", haveBottomLine=" + haveBottomLine +
                '}';
    }
}
