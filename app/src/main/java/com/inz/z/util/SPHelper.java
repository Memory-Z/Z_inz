package com.inz.z.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * SharedPreferences 存储工具
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create By 2018/7/21 10:33
 */
public class SPHelper {

    // 默认保存文件名
    private static final String FILE_NAME = "inzShareData";
    private static SharedPreferences sharedPreferences = null;
    private static SPHelper spHelper;

    /**
     * 获取 实例，单例模式 始终只有 一个实例
     *
     * @return SPHelper 实例
     */
    public static SPHelper getInstance() {
        if (spHelper == null) {
            spHelper = new SPHelper();
        }
        return spHelper;
    }

    private SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    /**
     * 初始化 SharePreferences ，推荐在 InzApplication 中进行
     *
     * @param context 上下文
     */
    public void initSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 将 数据 存入文件中
     *
     * @param key    键
     * @param object 值
     */
    @SuppressWarnings("unchecked")
    private void setShared(String key, Object object) {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (object instanceof String) {
                editor.putString(key, (String) object);
            } else if (object instanceof Boolean) {
                editor.putBoolean(key, (Boolean) object);
            } else if (object instanceof Float) {
                editor.putFloat(key, (Float) object);
            } else if (object instanceof Integer) {
                editor.putInt(key, (Integer) object);
            } else if (object instanceof Long) {
                editor.putLong(key, (Long) object);
            } else if (object instanceof Set) {
                editor.putStringSet(key, (Set<String>) object);
            }
            editor.apply();
        } catch (Exception e) {
            delShare(key);
        }
    }

    /**
     * 保存 String 类型数据
     *
     * @param key   键
     * @param value 值
     */
    private void setSharedString(String key, String value) {
        setShared(key, value);
    }

    /**
     * 获取 String 类型数据
     *
     * @param key 键
     * @return 返回的值，默认 null
     */
    private String getSharedString(String key) {
        return sharedPreferences.getString(key, null);
    }

    /**
     * 保存 Boolean 类型数据
     *
     * @param key   键
     * @param value 值
     */
    private void setSharedBoolean(String key, boolean value) {
        setShared(key, value);
    }

    /**
     * 获取 Boolean 类型数据
     *
     * @param key 键
     * @return 返回的值，默认false
     */
    private boolean getSharedBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     * 保存Integer 类型数据
     *
     * @param key   键
     * @param value 值
     */
    private void setSharedInteger(String key, int value) {
        setShared(key, value);
    }

    /**
     * 获取 Integer 类型数据
     *
     * @param key 键
     * @return 返回的值，默认-1
     */
    private Integer getSharedInteger(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    /**
     * 保存 Float 类型数据
     *
     * @param key   键
     * @param value 值
     */
    private void setSharedFloat(String key, Float value) {
        setShared(key, value);
    }

    /**
     * 获取 Float 类型数据
     *
     * @param key 键
     * @return 返回的值，默认 -1F
     */
    private Float getSharedFloat(String key) {
        return sharedPreferences.getFloat(key, -1F);
    }

    /**
     * 保存 Long 类型数据
     *
     * @param key   键
     * @param value 值
     */
    private void setSharedLong(String key, Long value) {
        setShared(key, value);
    }

    /**
     * 获取 Long 数据类型
     *
     * @param key 键
     * @return 返回的值，默认 -1L
     */
    private Long getSharedLong(String key) {
        return sharedPreferences.getLong(key, -1L);
    }

    /**
     * 保存 Set String 类型数据
     *
     * @param key   键
     * @param value 值
     */
    private void setSharedSet(String key, Set<String> value) {
        setShared(key, value);
    }

    /**
     * 获取 Set String 类型数据
     *
     * @param key 键
     * @return 返回的值，默认 null
     */
    private Set<String> getSharedSet(String key) {
        return sharedPreferences.getStringSet(key, null);
    }

    /**
     * 删除 保存的 内容
     *
     * @param key 保存的 键
     */
    private void delShare(String key) {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(key);
            editor.apply();
        } catch (Exception e) {
            //
        }
    }

    /**
     * 设置是否存在用户
     */
    public void setHaveUser(boolean haveUser) {
        setSharedBoolean("haveUser", haveUser);
    }

    /**
     * 获取是否存在用户
     *
     * @return 是否存在用户
     */
    public boolean isHaveUser() {
        return getSharedBoolean("haveUser");
    }

    /**
     * 保存用户Id
     *
     * @param userId 用户ID
     */
    public void saveUserId(String userId) {
        setSharedString("userId", userId);
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    public String getUserId() {
        return getSharedString("userId");
    }

    /**
     * 保存用户登录信息
     *
     * @param userLoginInfo 用户登录信息
     */
    public void saveUserLoginInfo(String userLoginInfo) {
        setSharedString("userLoginInfo", userLoginInfo);
    }

    /**
     * 获取用户登录信息
     *
     * @return 用户登录信息
     */
    public String getUserLoginInfo() {
        return getSharedString("userLoginInfo");
    }

    /**
     * 保存用户登录名
     *
     * @param loginName 登录名
     */
    public void saveUserLoginName(String loginName) {
        setSharedString("loginName", loginName);
    }

    /**
     * 获取用户登录名
     *
     * @return 登录名
     */
    public String getUserLoginName() {
        return getSharedString("loginName");
    }

    /**
     * 保存用户登录密码
     *
     * @param password 密码
     */
    public void saveUserPassword(String password) {
        setSharedString("userPassword", password);
    }

    /**
     * 获取用户登录密码
     *
     * @return 密码
     */
    public String getUserPassword() {
        return getSharedString("userPassword");
    }
}
