package com.inz.z.base.entity;

import com.inz.z.base.util.FileUtils;
import com.orhanobut.logger.Logger;

import java.io.File;

/**
 * 静态 实体类
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create By 2018/7/21 10:16
 */
public class Constants {

    public static final String PROJECT_NAME = "Inz";
    // 是否为 测试 状态
    private final static boolean isTest = true;
    // 服务器IP
    private final static String IP = "47.99.69.193";
    private final static String PORT = "80";
    // 测试链接
    private final static String TEST_URL = "http://" + IP + ":" + PORT + "/api/";
    // 正式链接
    private final static String FORMAL_URL = "";

    private final static String BASE_PATH = "Inz";

    /**
     * 获取请求 链接
     *
     * @return 对应的 URL
     */
    public static String getBaseUrl() {
        if (isTest) {
            return TEST_URL;
        }
        return FORMAL_URL;
    }

}
