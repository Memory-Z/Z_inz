package com.inz.z.http;

import com.inz.z.entity.ApiDiaryInfo;
import com.inz.z.entity.ApiFileInfo;
import com.inz.z.entity.ApiString;
import com.inz.z.entity.ApiUserInfo;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * 网络请求地址
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create By 2018/8/12 14:06
 */
public interface RetrofitInterface {

    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return 用户信息
     */
    @FormUrlEncoded
    @POST("login")
    Observable<ApiUserInfo> postLogin(@Field("userName") String userName,
                                      @Field("password") String password);

    /**
     * 用户注册
     *
     * @param userName 用户名
     * @param password 密码
     * @return 注册状态
     */
    @FormUrlEncoded
    @POST("register")
    Observable<ApiString> register(@Field("userName") String userName,
                                   @Field("password") String password);

    /**
     * 更新用户邮箱
     *
     * @param userId    用户ID
     * @param userEmail 用户邮箱
     * @return 邮件验证码发送状态
     */
    @FormUrlEncoded
    @POST("{userId}/updateUserEmail")
    Observable<ApiString> updateEmailCode(@Path("userId") String userId,
                                          @Field("userEmail") String userEmail);

    /**
     * 验证验证码
     *
     * @param userId    用户Id
     * @param emailCode 邮箱验证码
     * @param userEmail 用户邮箱
     * @return 邮箱更新状态
     */
    @FormUrlEncoded
    @POST("{userId}/updateUserEmail/{emailCode}")
    Observable<ApiString> sendUserEmailCode(@Path("userId") String userId,
                                            @Path("emailCode") String emailCode,
                                            @Field("userEmail") String userEmail);

    /**
     * 更新用户头像
     *
     * @param userId 用户ID
     * @param photo  头像文件
     * @return 状态信息
     */
    @Multipart
    @POST("{userId}/photo")
    Observable<ApiString> updateUserPhoto(@Path("userId") String userId,
                                          @Part("photo") MultipartBody.Part photo);

    /**
     * 更新用户信息
     *
     * @param userId       用户ID
     * @param userSex      用户性别： 1：男，0：女
     * @param birthdayDate 生日
     * @param userIntro    用户简介
     * @param userMemo     用户备注
     * @param userPhone    用户手机号
     * @return 用户信息
     */
    @FormUrlEncoded
    @POST("{userId}/update")
    Observable<ApiUserInfo> updateUserInfo(@Path("userId") String userId,
                                           @Field("userSex") String userSex,
                                           @Field("createDate") String birthdayDate,
                                           @Field("userIntro") String userIntro,
                                           @Field("userMemo") String userMemo,
                                           @Field("userPhone") String userPhone);

    /**
     * 修改用户密码
     *
     * @param userId       用户ID
     * @param userPassword 用户新密码
     * @return 修改状态
     */
    @FormUrlEncoded
    @POST("{userId}/password")
    Observable<ApiString> changePassword(@Path("userId") String userId,
                                         @Field("userPassword") String userPassword);

    /**
     * 获取日志信息
     *
     * @param userId   用户ID
     * @param start    开始页数
     * @param pageSize 页面大小
     * @return 日志信息
     */
    @FormUrlEncoded
    @POST("{userId}/diary")
    Observable<List<ApiDiaryInfo>> getDiaryList(@Path("userId") String userId,
                                                @Field("start") String start,
                                                @Field("pageSize") String pageSize);

    /**
     * 获取用户某一日志的文件信息
     *
     * @param userId  用户ID
     * @param diaryId 日志ID
     * @return 日志文件信息
     */
    @POST("{userId}/diary/{diaryId}/file")
    Observable<List<ApiFileInfo>> getDiaryFileList(@Path("userId") String userId,
                                                   @Path("diaryId") String diaryId);

    /**
     * 日志添加
     *
     * @param userId       用户ID
     * @param diaryContent 日志内容
     * @param diaryWeather 日志天气
     * @param diaryAddress 日志地址
     * @param diaryPhoto   日志图片
     * @return 日志信息
     */
    @Multipart
    @POST("{userId}/diary/add")
    Observable<ApiDiaryInfo> addDiaryInfo(@Path("userId") String userId,
                                          @Part("diaryContent") String diaryContent,
                                          @Part("diaryWeather") String diaryWeather,
                                          @Part("diaryAddress") String diaryAddress,
                                          @Part("diaryPhoto") MultipartBody diaryPhoto);

    /**
     * 日志删除
     *
     * @param userId  用户Id
     * @param diaryId 日志Id
     * @return 日志删除状态
     */
    @POST("{userId}/diary/{diaryId}/delete")
    Observable<ApiString> deleteDiaryInfo(@Path("userId") String userId,
                                          @Path("diaryId") String diaryId);
}
