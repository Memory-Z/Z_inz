package com.inz.z.util;

import android.os.Environment;
import android.support.annotation.Nullable;

import com.inz.z.entity.Constants;

import java.io.File;

/**
 * 文件 工具
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create By 2018/7/21 11:48
 */
public class FileUtils {

    // 公司名
    private static String companyNameStr = "inz";
    // 程序包名
    private static String packageNameStr = "z";
    // APP 的 SD 卡 路径
    private static String appSDPathStr = "";

    /**
     * 初始化 文件路径
     *
     * @param companyName 公司名
     * @param packageName 程序包名
     */
    public static void init(String companyName, String packageName) {
        StringBuilder sb = new StringBuilder();
        packageNameStr = packageName;
        // 注意： 这个函数 不同手机 获取到的路径 不一致：Environment.getExternalStorageDirectory()
        sb.append(Environment.getExternalStorageDirectory().toString());
        if (companyName != null && !companyName.isEmpty()) {
            sb.append(File.separator);
            sb.append(packageName);
        }
        sb.append(File.separator);
        sb.append(packageNameStr);
        appSDPathStr = sb.toString();
        isFolderExistWithCreate(appSDPathStr);
    }

    /**
     * 检查文件是否 存在 并  创建
     *
     * @param folderStr 文件地址
     * @return 返回是否存在
     */
    public static boolean isFolderExistWithCreate(String folderStr) {
        File dir = new File(folderStr);
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return true;
    }

    /**
     * 是否存在 SD 卡
     *
     * @return 返回 是否存在 SD 卡
     */
    private static boolean existsSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取 SD 卡文件路径
     *
     * @return SD 卡 路径
     */
    public static String getSDPath() {
        if (existsSDCard()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return Environment.getExternalStorageDirectory().toString();
    }

    /**
     * 创建 文件目录
     *
     * @param dirStr 目录名称
     * @return 文件 所在的 路径
     */
    public static String createFileDir(String dirStr) {
        String filePath = Constants.getBaseDirPath() + dirStr;
        File dir = new File(filePath);
        boolean flag = true;
        if (!dir.exists()) {
            flag = dir.mkdirs();
        }
        if (flag) {
            return filePath;
        }
        return "";
    }

    /**
     * 创建文件
     *
     * @param dirStr  文件所在目录
     * @param fileStr 文件名
     * @return 文件所在 路径
     */
    public static String createFile(@Nullable String dirStr, String fileStr) {
        String filePath = Constants.getBaseDirPath();
        if (dirStr == null || "".equals(dirStr)) {
            filePath = filePath + fileStr;
        } else {
            filePath = filePath + dirStr + File.separator + fileStr;
        }
        File file = new File(filePath);
        boolean flag = true;
        if (!file.exists()) {
            flag = file.mkdirs();
        }
        if (flag) {
            return filePath;
        }
        return "";
    }
}
