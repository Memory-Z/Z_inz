package com.inz.choosefile.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.inz.choosefile.BuildConfig;
import com.inz.choosefile.bean.FileBean;
import com.inz.choosefile.bean.FileFolderBean;
import com.inz.choosefile.bean.VideoBean;

import java.util.List;
import java.util.Map;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/28 10:05.
 */
public class FileUtils {

    /**
     * 扫描文件监听
     */
    public static interface ScanFileListener {
        void createLoader();

        void loadFinish(FileFolderBean allFolder, Map<String,
                FileFolderBean> folderBeanMap);

        void loadReset();
    }

    private static final String[] VIDEOS = {
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.LATITUDE,
            MediaStore.Video.Media.LONGITUDE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DURATION
    };

    /**
     * 扫描视频文件
     *
     * @param context       上下文
     * @param allFolder     全文目录
     * @param folderBeanMap 目录结构Map
     */
    public static void scanVideoFile(Context context, FileFolderBean allFolder, Map<String, FileFolderBean> folderBeanMap) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                VIDEOS, null, null, VIDEOS[3] + " DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(0);
                String bucketName = cursor.getString(1);
                String mimeType = cursor.getString(2);
                long addDate = cursor.getLong(3);
                float latitude = cursor.getFloat(4);
                float longitude = cursor.getFloat(5);
                long size = cursor.getLong(6);
                long duration = cursor.getLong(7);

                FileBean fileBean = new FileBean();
                fileBean.setFileType(FileBean.FileType.TYPE_VIDEO);
                fileBean.setChecked(false);
                fileBean.setDisable(false);
                fileBean.setmPath(path);
                fileBean.setmBucketName(bucketName);
                fileBean.setmMineType(mimeType);
                fileBean.setmAddDate(addDate);
                fileBean.setmLatitude(latitude);
                fileBean.setmLongitude(longitude);
                fileBean.setmSize(size);
                fileBean.setmDuration(duration);

                allFolder.addFileBean(fileBean);

                // 判断 Map 中是否存在当前 目录结构
                FileFolderBean folderBean = folderBeanMap.get(bucketName);
                if (folderBean != null) {
                    // 存在，添加文件至目录下
                    folderBean.addFileBean(fileBean);
                } else {
                    // 不存在，添加结构至 Map 中
                    folderBean = new FileFolderBean();
                    folderBean.setFolderName(bucketName);
                    folderBean.addFileBean(fileBean);

                    folderBeanMap.put(bucketName, folderBean);
                }

            }
            cursor.close();
        }
    }

    /**
     * 视频加载回调
     */
    public static class VideoLoadCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
        private Context mContext;
        private Map<String, FileFolderBean> folderBeanMap;
        private FileFolderBean allFolder;
        private ScanFileListener listener;

        public VideoLoadCallbacks(Context mContext, FileFolderBean allFolder, Map<String,
                FileFolderBean> folderBeanMap, ScanFileListener listener) {
            this.mContext = mContext;
            this.allFolder = allFolder;
            this.folderBeanMap = folderBeanMap;
            this.listener = listener;
        }

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
            if (listener != null) {
                listener.createLoader();
            }
//            if (i == 1) {
//                if (bundle != null) {
//                    return new CursorLoader(mContext, MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//                            VIDEOS, VIDEOS[0] + " like '%" + bundle.getString("path") + "%'",
//                            null, IMAGES[3] + " DESC");
//                }
//            }
            return new CursorLoader(mContext, MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    VIDEOS, null, null, VIDEOS[3] + " DESC");
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
            // 创建完毕时调用
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String path = cursor.getString(0);
                    String bucketName = cursor.getString(1);
                    String mimeType = cursor.getString(2);
                    long addDate = cursor.getLong(3);
                    float latitude = cursor.getFloat(4);
                    float longitude = cursor.getFloat(5);
                    long size = cursor.getLong(6);
                    long duration = cursor.getLong(7);

                    FileBean fileBean = new FileBean();
                    fileBean.setFileType(FileBean.FileType.TYPE_VIDEO);
                    fileBean.setChecked(false);
                    fileBean.setDisable(false);
                    fileBean.setmPath(path);
                    fileBean.setmBucketName(bucketName);
                    fileBean.setmMineType(mimeType);
                    fileBean.setmAddDate(addDate);
                    fileBean.setmLatitude(latitude);
                    fileBean.setmLongitude(longitude);
                    fileBean.setmSize(size);
                    fileBean.setmDuration(duration);

                    allFolder.addFileBean(fileBean);

                    // 判断 Map 中是否存在当前 目录结构
                    FileFolderBean folderBean = folderBeanMap.get(bucketName);
                    if (folderBean != null) {
                        // 存在，添加文件至目录下
                        folderBean.addFileBean(fileBean);
                    } else {
                        // 不存在，添加结构至 Map 中
                        folderBean = new FileFolderBean();
                        folderBean.setFolderName(bucketName);
                        folderBean.addFileBean(fileBean);

                        folderBeanMap.put(bucketName, folderBean);
                    }

                }
                cursor.close();
            }
            if (listener != null) {
                listener.loadFinish(allFolder, folderBeanMap);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            // 数据重置时调用
            if (listener != null) {
                listener.loadReset();
            }
        }
    }

    private static final String[] IMAGES = {
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.LATITUDE,
            MediaStore.Video.Media.LONGITUDE,
            MediaStore.Video.Media.SIZE
    };

    /**
     * 扫描 图片文件
     *
     * @param context       上下文
     * @param allFolder     全文目录
     * @param folderBeanMap 目录结构Map
     */
    public static void scanImageFile(Context context, FileFolderBean allFolder, Map<String, FileFolderBean> folderBeanMap) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                IMAGES, null, null, IMAGES[3] + " DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(0);
                String bucketName = cursor.getString(1);
                String mimeType = cursor.getString(2);
                long addDate = cursor.getLong(3);
                float latitude = cursor.getFloat(4);
                float longitude = cursor.getFloat(5);
                long size = cursor.getLong(6);

                FileBean fileBean = new FileBean();
                fileBean.setFileType(FileBean.FileType.TYPE_IMAGE);
                fileBean.setChecked(false);
                fileBean.setDisable(false);
                fileBean.setmPath(path);
                fileBean.setmBucketName(bucketName);
                fileBean.setmMineType(mimeType);
                fileBean.setmAddDate(addDate);
                fileBean.setmLatitude(latitude);
                fileBean.setmLongitude(longitude);
                fileBean.setmSize(size);

                allFolder.addFileBean(fileBean);

                // 判断 Map 中是否存在当前 目录结构
                FileFolderBean folderBean = folderBeanMap.get(bucketName);
                if (folderBean != null) {
                    // 存在，添加文件至目录下
                    folderBean.addFileBean(fileBean);
                } else {
                    // 不存在，添加结构至 Map 中
                    folderBean = new FileFolderBean();
                    folderBean.setFolderName(bucketName);
                    folderBean.addFileBean(fileBean);

                    folderBeanMap.put(bucketName, folderBean);
                }
            }
            cursor.close();
        }
    }

    /**
     * 图片加载回调
     */
    public static class ImageLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
        private Context mContext;
        private Map<String, FileFolderBean> folderBeanMap;
        private FileFolderBean allFolder;
        private ScanFileListener listener;

        public ImageLoaderCallbacks(Context mContext, FileFolderBean allFolder,
                                    Map<String, FileFolderBean> folderBeanMap,
                                    ScanFileListener listener) {
            this.mContext = mContext;
            this.allFolder = allFolder;
            this.folderBeanMap = folderBeanMap;
            this.listener = listener;
        }

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
            if (listener != null) {
                listener.createLoader();
            }
//            if (i == 1) {
//                if (bundle != null) {
//                    return new CursorLoader(mContext, MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//                            IMAGES, IMAGES[0] + " like '%" + bundle.getString("path") + "%'",
//                            null, IMAGES[3] + " DESC");
//                }
//            }
            return new CursorLoader(mContext, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGES, null, null, IMAGES[3] + " DESC");
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String path = cursor.getString(0);
                    String bucketName = cursor.getString(1);
                    String mimeType = cursor.getString(2);
                    long addDate = cursor.getLong(3);
                    float latitude = cursor.getFloat(4);
                    float longitude = cursor.getFloat(5);
                    long size = cursor.getLong(6);

                    FileBean fileBean = new FileBean();
                    fileBean.setFileType(FileBean.FileType.TYPE_IMAGE);
                    fileBean.setChecked(false);
                    fileBean.setDisable(false);
                    fileBean.setmPath(path);
                    fileBean.setmBucketName(bucketName);
                    fileBean.setmMineType(mimeType);
                    fileBean.setmAddDate(addDate);
                    fileBean.setmLatitude(latitude);
                    fileBean.setmLongitude(longitude);
                    fileBean.setmSize(size);

                    allFolder.addFileBean(fileBean);

                    // 判断 Map 中是否存在当前 目录结构
                    FileFolderBean folderBean = folderBeanMap.get(bucketName);
                    if (folderBean != null) {
                        // 存在，添加文件至目录下
                        folderBean.addFileBean(fileBean);
                    } else {
                        // 不存在，添加结构至 Map 中
                        folderBean = new FileFolderBean();
                        folderBean.setFolderName(bucketName);
                        folderBean.addFileBean(fileBean);

                        folderBeanMap.put(bucketName, folderBean);
                    }
                }
            }
            if (listener != null) {
                listener.loadFinish(allFolder, folderBeanMap);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            if (listener != null) {
                listener.loadReset();
            }
        }
    }
}
