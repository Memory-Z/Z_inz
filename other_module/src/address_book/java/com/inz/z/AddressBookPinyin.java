package com.inz.z;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/2 9:46.
 */
public class AddressBookPinyin extends BaseIndexPinyinBean {
    private String userName;
    private String userPhoto;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    @Override
    public String getTarget() {
        return userName;
    }

    @Override
    public boolean isShowSuspension() {
        return true;
    }
}
