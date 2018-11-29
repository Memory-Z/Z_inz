package com.inz.z.model;

import com.inz.z.base.IBaseLoadListener;
import com.inz.z.entity.ApiDiaryInfo;

import okhttp3.MultipartBody;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/11/20 15:44.
 */
public interface IDiaryModel {
    void addDiaryInfo(String userId, String diaryContent,
                      String diaryWeather, String diaryAddress, MultipartBody multipartBody,
                      IBaseLoadListener<ApiDiaryInfo> loadListener);
}
