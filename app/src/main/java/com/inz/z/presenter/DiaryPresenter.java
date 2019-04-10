package com.inz.z.presenter;

import com.inz.z.base.BasePresenter;
import com.inz.z.base.IBaseLoadListener;
import com.inz.z.entity.ApiDiaryInfo;
import com.inz.z.model.IDiaryModel;
import com.inz.z.model.impl.DiaryModelImpl;
import com.inz.z.view.IDiaryView;

import java.io.File;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/11/20 15:09.
 */
public class DiaryPresenter extends BasePresenter<IDiaryView> {

    public void addDiaryInfo(String userId, String diaryContent,
                             String diaryWeather, String diaryAddress, File[] files) {
        IDiaryModel diaryModel = new DiaryModelImpl();
        diaryModel.addDiaryInfo(userId, diaryContent, diaryWeather, diaryAddress, files, null,
                new IBaseLoadListener<ApiDiaryInfo>() {
                    @Override
                    public void loadStart() {
                        System.out.println("start");
                    }

                    @Override
                    public void loadSuccess(ApiDiaryInfo apiDiaryInfo) {
                        System.out.println(apiDiaryInfo.toString());
                    }

                    @Override
                    public void loadFailure(Throwable e) {

                    }

                    @Override
                    public void loadComplete() {
                        System.out.println("'");
                    }
                });
    }
}
