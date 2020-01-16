package com.inz.z.base.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.provider.MediaStore;
import android.util.Base64;

import com.inz.z.base.entity.Constants;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
        String filePath = FileUtils.getProjectPath() + dirStr;
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
        String filePath = FileUtils.getProjectPath();
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

    /**
     * 根据手机的分辨率dp的单位转成(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率(像素)的单位转成dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取扩展存储路径，TF卡、U盘
     */
    public static String getExternalStorageDirectory() {
        String dir = "";
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure")) continue;
                if (line.contains("asec")) continue;

                if (line.contains("fat")) {
                    String columns[] = line.split(" ");
                    if (columns.length > 1) {
                        dir = dir.concat(columns[1] + "\n");
                    }
                } else if (line.contains("fuse")) {
                    String columns[] = line.split(" ");
                    if (columns.length > 1) {
                        dir = dir.concat(columns[1] + "\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dir;
    }

    /**
     * 获取项目地址
     *
     * @return 项目自定义地址
     */
    public static String getProjectPath() {
        File storyDir = Environment.getExternalStorageDirectory();
        String rootPath = "";
        if (storyDir != null) {
            // 如果内部存储 存在，获取内部存储地址
            rootPath = storyDir.getAbsolutePath();
        } else {
            // 如果内部存储不存在，获取 扩展SD 卡地址
            rootPath = getExternalStorageDirectory();
        }
        rootPath = rootPath + File.separatorChar + "Inz" + File.separatorChar + Constants.PROJECT_NAME;
        File projectFile = new File(rootPath);
        if (!projectFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            projectFile.mkdirs();
        }
        return rootPath;
    }

    /**
     * 获取项目文件地址
     *
     * @return 项目文件地址
     */
    public static String getProjectFilePath() {
        return getProjectPath() + File.separatorChar + "files";
    }

    /**
     * 获取项目图片地址
     *
     * @return 项目图片地址
     */
    public static String getProjectImagePath() {
        return getProjectPath() + File.separatorChar + "images";
    }

    /**
     * 获取项目配置地址
     *
     * @return 项目配置地址
     */
    public static String getProjectConfigPath() {
        return getProjectPath() + File.separatorChar + "config";
    }

    /**
     * 获取项目下载地址
     *
     * @return 项目下载地址
     */
    public static String getProjectDownloadPath() {
        return getProjectPath() + File.separatorChar + "download";
    }

    /**
     * 获取项目日志地址
     *
     * @return 项目日志地址
     */
    public static String getProjectLogPath() {
        return getProjectPath() + File.separatorChar + "logs";
    }

    /**
     * 获取项目缓存地址
     *
     * @return 项目缓存地址
     */
    public static String getProjectCachePath() {
        return getProjectPath() + File.separatorChar + "cache";
    }

    /**
     * 获取项目崩溃文件目录
     *
     * @return 项目崩溃文件地址
     */
    public static String getProjectCrashPath() {
        return getProjectPath() + File.separatorChar + "crash";
    }

    /**
     * 获取项目缓存路径
     *
     * @param context 上下文
     * @return 缓存路径
     */
    public static String getCachePath(Context context) {
        File file = context.getExternalCacheDir();
        String cacheDirPath = "";
        if (file != null) {
            cacheDirPath = file.getAbsolutePath();
        } else {
            cacheDirPath = getProjectCachePath();
        }
        return cacheDirPath;
    }

    /**
     * 获取项目缓存文件路径
     *
     * @param context 上下文
     * @return 缓存文件路径
     */
    public static String getCacheFilePath(Context context) {
        return getCachePath(context) + File.separatorChar + "files";
    }

    /**
     * 获取缓存图片路径
     *
     * @param context 上下文
     * @return 缓存图片地址
     */
    public static String getCacheImagePath(Context context) {
        return getCachePath(context) + File.separatorChar + "images";
    }

    /**
     * 获取缓存数据路径
     *
     * @param context 上下文
     * @return 缓存数据路径
     */
    public static String getCacheDataPath(Context context) {
        return getCachePath(context) + File.separatorChar + "data";
    }

    /**
     * 获取缓存日志路径
     *
     * @param context 上下文
     * @return 缓存日志路径
     */
    public static String getCacheLogPath(Context context) {
        return getCachePath(context) + File.separatorChar + "logs";
    }

    /**
     * 获取缓存崩溃日志路径
     *
     * @param context 上下文
     * @return 崩溃日志路径
     */
    public static String getCacheCrashLogPath(Context context) {
        return getCacheLogPath(context) + File.separatorChar + "crash";
    }

    /**
     * 是否由挂载外部存储
     *
     * @return 是否挂载
     */
    public static boolean haveExternalStorage() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取沙盒文档地址
     *
     * @param context 上下文
     * @return 文档地址
     */
    public static String getExternalDocumentPath(Context context) {
        File documentFile = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState(documentFile))
                && documentFile != null && documentFile.exists()) {
            return documentFile.getAbsolutePath();
        } else {
            return getCacheFilePath(context);
        }
    }

    /**
     * 获取沙河下载地址
     *
     * @param context 上下文
     * @return 下载文件地址
     */
    public static String getExternalDownloadPath(Context context) {
        File downloadFile = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState(downloadFile))
                && downloadFile != null && downloadFile.exists()) {
            return downloadFile.getAbsolutePath();
        }
        return getCacheFilePath(context);
    }

    /**
     * 获取相册地址
     *
     * @param context 上下文
     * @return 相册地址
     */
    @Nullable
    public static String getExternalDCIMPath(Context context) {
        File dcimPath = context.getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState(dcimPath))
                && dcimPath != null && dcimPath.exists()) {
            return dcimPath.getAbsolutePath();
        }
        return null;
    }

    /**
     * 文件夹 压缩
     *
     * @param dirPath     文件夹 地址
     * @param zipFilePath 压缩目标地址
     * @throws IOException 异常
     */
    public static void zipFolder(String dirPath, String zipFilePath) throws IOException {
        File file = new File(dirPath);
        if (!file.exists()) {
            return;
        }
        // 创建 ZIP
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath));
        // 压缩
        zipFiles(file.getParent() + File.separatorChar, file.getName(), zipOutputStream);
        zipOutputStream.finish();
        zipOutputStream.close();
    }

    /**
     * 压缩文件
     *
     * @param dirPath         文件目录地址
     * @param fileName        文件名
     * @param zipOutputStream ZipOutputStream
     * @throws IOException 异常
     */
    private static void zipFiles(String dirPath, String fileName, ZipOutputStream zipOutputStream) throws IOException {
        if (zipOutputStream == null) {
            return;
        }
        String filePath = dirPath + File.separatorChar;
        filePath = filePath.replaceAll("\\\\", File.separatorChar + "");
        filePath = filePath.replaceAll("//", File.separatorChar + "");
        File file = new File(filePath + fileName);
        // 如果是 文件 或空文件夹 压缩后删除
        if (file.isFile()) {
            // 文件
            ZipEntry zipEntry = new ZipEntry(fileName);
            FileInputStream fileInputStream = new FileInputStream(file);
            zipOutputStream.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = fileInputStream.read(buffer)) != -1) {
                zipOutputStream.write(buffer, 0, len);
            }
            zipOutputStream.closeEntry();
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        } else {
            // 文件夹
            String[] fileList = file.list();
            // 没有 子文件 的 文件夹压缩
            if (fileList.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(fileName + File.separatorChar);
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.closeEntry();
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
            // 子文件 递归压缩
            for (String fileNameStr : fileList) {
                zipFiles(filePath + fileName + File.separatorChar, fileNameStr, zipOutputStream);
            }
        }
    }

    /**
     * 创建系统信息文件
     *
     * @param systemInfo 系统信息
     */
    public static void createSystemInfoFile(String systemInfo) {
        String logPath = getProjectLogPath() + File.separatorChar + "logger" + File.separatorChar;
        String logFilePath = logPath + "system_info.log";
        File file = new File(logPath);
        boolean flag;
        if (!file.exists()) {
            flag = file.mkdirs();
        } else {
            flag = true;
        }
        File logFile = new File(logFilePath);
        if (logFile.exists()) {
            flag = logFile.delete();
        }
        try {
            flag = logFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        }
        if (flag) {
            try {
                FileOutputStream outputStream = new FileOutputStream(logFile);
                outputStream.write(systemInfo.getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 收集系统信息
     *
     * @param context 上下文
     * @return 系统信息
     */
    public static String collectSystemInfo(Context context) {
        // 用来存储设备信息和异常信息
        Map<String, String> info = new HashMap<>();
        info.clear();
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                info.put("versionName", versionName);
                info.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Logger.t("collectSystem").e(e, "收集系统信息出错");
        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                info.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                Logger.t("collectSystem").e(e, "收集系统信息出错");
            }
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : info.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            try {
                if ("TIME".equals(key)) {
                    Date date = new Date(Long.parseLong(value));
                    DateFormat sdf = BaseTools.getBaseDateFormat();
                    sb.append(key).append("=").append(sdf.format(date)).append("\n");
                } else {
                    sb.append(key).append("=").append(value).append("\n");
                }
            } catch (Exception e) {
                sb.append(key).append("=").append(value).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * 压缩日志文件
     *
     * @param context 上下文
     * @return 压缩文件路径
     */
    public static String zipCrashLog(Context context) {
        String path = "";
        // 日志保存地址
        String logPath = FileUtils.getProjectLogPath() + File.separatorChar + "logger";
        // 本地日志缓存地址
        String localLogCachePath = FileUtils.getCachePath(context);
        // 日志缓存地址
        String logCachePath = localLogCachePath + File.separatorChar + "logger";
        // 系统崩溃文件地址
        String crashPath = Environment.getExternalStorageDirectory().getPath() + "/crash-skyvis/";
        File logCacheDir = new File(logCachePath);
        boolean flag = false;
        if (!logCacheDir.exists()) {
            flag = logCacheDir.mkdirs();
        } else {
            flag = true;
        }
        if (flag) {
            try {
                // 压缩 崩溃文件
                FileUtils.zipFolder(crashPath, logCachePath + File.separatorChar + "crash.zip");
                // 压缩 日志文件
                FileUtils.zipFolder(logPath, logCachePath + File.separatorChar + "logger.zip");
                // 压缩 缓存文件
                path = localLogCachePath + File.separatorChar + "log.zip";
                FileUtils.zipFolder(logCachePath, path);
            } catch (IOException e) {
                e.printStackTrace();
                path = "";
            }
        }
        return path;
    }

    /**
     * Bitmap 转Base64 字符串
     *
     * @param bitmap Bitmap
     * @return 转码后Base64 字符串
     */
    // FIXME: 2019/07/12  待校验
    public static String bitmap2Base64(@NonNull Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        try {
            stream.flush();
            stream.close();
            byte[] bytes = stream.toByteArray();
            result = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Base64 字符串转Bitmap
     *
     * @param base64Str 字符串
     * @return Bitmap
     */
    public static Bitmap base64ToBitmap(String base64Str) {
        byte[] bytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}
