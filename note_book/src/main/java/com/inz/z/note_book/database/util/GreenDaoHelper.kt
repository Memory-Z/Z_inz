package com.inz.z.note_book.database.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.inz.z.note_book.database.DaoMaster
import com.inz.z.note_book.database.DaoSession

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/01 14:32.
 */
class GreenDaoHelper private constructor() {

    companion object {
        private var mDaoSession: DaoSession? = null
        private var mDaoMaster: DaoMaster? = null
        private var database: SQLiteDatabase? = null

        private var helper: GreenDaoHelper? = null
            get() {
                if (field == null) {
                    field = GreenDaoHelper()
                }
                return field
            }

        @Synchronized
        fun getInstance(): GreenDaoHelper {
            return helper!!
        }


    }

    /**
     * 初始化 GreenDao 工具类
     */
    fun initGreenDaoHelper(context: Context) {
        synchronized(GreenDaoHelper) {
            // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
            // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
            // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
            // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
            // 此处 note_book 表示数据库名称 可以任意填写
            val mDaoHelper = DaoMaster.DevOpenHelper(context, "note_book", null)
            database = mDaoHelper.writableDatabase
            // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
            mDaoMaster = DaoMaster(database)
            mDaoSession = mDaoMaster?.newSession()
        }
    }

    fun getDaoSession(): DaoSession? {
        return mDaoSession
    }

    fun getDatabase(): SQLiteDatabase? {
        return database
    }
}