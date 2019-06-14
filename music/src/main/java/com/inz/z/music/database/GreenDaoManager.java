package com.inz.z.music.database;

import android.content.Context;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/06 09:21.
 */
public class GreenDaoManager {

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static GreenDaoManager greenDaoManager;
    private Context mContext;
    private static final String dbName = "music.db";


    private GreenDaoManager() {
        if (greenDaoManager == null) {
            DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(mContext, dbName, null);
        }
    }

    public static GreenDaoManager getInstance() {
        return greenDaoManager;
    }


    public void init(Context context) {
        this.mContext = context;
        greenDaoManager = new GreenDaoManager();
    }
}
