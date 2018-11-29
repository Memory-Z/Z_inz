package com.inz.z.presenter;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/11/20 19:54.
 */
public class DiaryPresenterTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void addDiaryInfo() {
        DiaryPresenter diaryPresenter = new DiaryPresenter();
        File file = new File("/storage");
        File[] files = new File[]{file};
        diaryPresenter.addDiaryInfo("uiuiu", "content", "weather", "address", files);
    }
}