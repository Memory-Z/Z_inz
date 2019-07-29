package com.inz.z.music;

import android.content.Context;

import com.inz.z.music.database.DaoMaster;
import com.inz.z.music.database.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/04 10:04.
 */
public class MusicApplication {

    private DaoMaster.DevOpenHelper openHelper;
    private static DaoSession mDaoSession;
    private Context mContext;

    public MusicApplication() {
    }

    public MusicApplication(Context context) {
        super();
        mContext = context;
        setDaoMaster(mContext);
    }

    private void setDaoMaster(Context context) {
        openHelper = new DaoMaster.DevOpenHelper(context, "music.db", null);
        Database database = openHelper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(database);
        mDaoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }

}
