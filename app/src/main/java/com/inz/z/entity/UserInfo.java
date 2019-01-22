package com.inz.z.entity;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * 用户信息
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/17 18:39.
 */
public class UserInfo implements Serializable {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户邮箱
     */
    private String userEmail;
    /**
     * 用户性别
     */
    private String userSex;
    /**
     * 用户手机号
     */
    private String userPhone;
    /**
     * 用户生日
     */
    private String userBirthday;
    /**
     * 用户简介
     */
    private String userIntro;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 用户备注
     */
    private String userMemo;
    /**
     * 用户头像链接
     */
    private String userPhotoUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getUserIntro() {
        return userIntro;
    }

    public void setUserIntro(String userIntro) {
        this.userIntro = userIntro;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserMemo() {
        return userMemo;
    }

    public void setUserMemo(String userMemo) {
        this.userMemo = userMemo;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return "UserInfo{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userSex='" + userSex + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userBirthday='" + userBirthday + '\'' +
                ", userIntro='" + userIntro + '\'' +
                ", userType='" + userType + '\'' +
                ", userMemo='" + userMemo + '\'' +
                ", userPhotoUrl='" + userPhotoUrl + '\'' +
                '}';
    }
}
