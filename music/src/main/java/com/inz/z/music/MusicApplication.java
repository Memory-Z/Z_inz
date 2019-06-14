package com.inz.z.music;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.inz.z.base.BaseApplication;
import com.inz.z.music.database.DaoMaster;
import com.inz.z.music.database.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/04 10:04.
 */
public class MusicApplication extends BaseApplication {

    private DaoMaster.DevOpenHelper openHelper;
    private static DaoSession mDaoSession;

    public MusicApplication() {
    }

    public MusicApplication(Context context) {
        setMContext(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setDaoMaster(getMContext());
    }

    private void setDaoMaster(Context context) {
        openHelper = new DaoMaster.DevOpenHelper(context, "music.db", null);
        SQLiteDatabase database = openHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        mDaoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }

}
