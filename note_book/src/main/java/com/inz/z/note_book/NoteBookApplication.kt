package com.inz.z.note_book

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import com.inz.z.base.util.L
import com.inz.z.note_book.database.DaoMaster
import com.inz.z.note_book.database.DaoSession

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/17 14:39.
 */
class NoteBookApplication : Application() {

    private var mDaoHelper: DaoMaster.DevOpenHelper? = null
    private var database: SQLiteDatabase? = null
    private var mDaoMaster: DaoMaster? = null
    private var mDaoSession: DaoSession? = null

    override fun onCreate() {
        super.onCreate()
        L.initL(applicationContext)
        setDatabase()
    }

    /**
     * 设置GreenDao Database
     */
    private fun setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        // 此处 note_book 表示数据库名称 可以任意填写
        mDaoHelper = DaoMaster.DevOpenHelper(applicationContext, "note_book", null)
        database = mDaoHelper?.writableDatabase
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = DaoMaster(database)
        mDaoSession = mDaoMaster?.newSession()
    }

    public fun getDaoSession(): DaoSession? {
        return mDaoSession
    }

    public fun getDatabase(): SQLiteDatabase? {
        return database
    }
}