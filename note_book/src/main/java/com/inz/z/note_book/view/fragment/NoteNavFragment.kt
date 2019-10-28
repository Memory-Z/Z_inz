package com.inz.z.note_book.view.fragment

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.inz.z.base.util.L
import com.inz.z.base.util.LauncherHelper
import com.inz.z.base.view.AbsBaseFragment
import com.inz.z.note_book.NoteBookApplication
import com.inz.z.note_book.R
import com.inz.z.note_book.bean.NoteGroup
import com.inz.z.note_book.bean.NoteInfo
import com.inz.z.note_book.database.NoteGroupDao
import com.inz.z.note_book.database.NoteInfoDao
import com.inz.z.note_book.databinding.ItemNoteSampleLayoutBinding
import kotlinx.android.synthetic.main.note_nav_hint_layout.*
import kotlinx.android.synthetic.main.note_nav_layout.*
import org.greenrobot.greendao.query.WhereCondition
import java.lang.Exception
import java.util.*

/**
 * 首页导航页
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/25 11:48.
 */
class NoteNavFragment : AbsBaseFragment() {

    companion object {
        private const val TAG = "NoteNavFragment"
    }

    private val nearNoteInfoViewList: MutableList<ItemNoteSampleLayoutBinding?> = mutableListOf()

    override fun initWindow() {

    }

    override fun getLayoutId(): Int {
        return R.layout.note_nav_layout
    }

    override fun initView() {
        nearNoteInfoViewList.apply {
            note_nav_near_five_0_include.visibility = View.GONE
            note_nav_near_five_1_include.visibility = View.GONE
            note_nav_near_five_2_include.visibility = View.GONE
            note_nav_near_five_3_include.visibility = View.GONE
            note_nav_near_five_4_include.visibility = View.GONE
            add(DataBindingUtil.getBinding(note_nav_near_five_0_include))
            add(DataBindingUtil.getBinding(note_nav_near_five_1_include))
            add(DataBindingUtil.getBinding(note_nav_near_five_2_include))
            add(DataBindingUtil.getBinding(note_nav_near_five_3_include))
            add(DataBindingUtil.getBinding(note_nav_near_five_4_include))
        }
    }

    override fun initData() {
        val date = Calendar.getInstance(Locale.CHINA).time
        note_nav_hint_year_tv.text =
            String.format(getString(R.string.base_format_year_month), date)
        note_nav_hint_data_tv.text =
            String.format(getString(R.string.base_format_day), date)
        note_nav_hint_week_tv.text =
            String.format(getString(R.string.base_format_week), date)

        note_nav_hint_ibtn.setOnClickListener {
            val packageName = "com.netease.cloudmusic"
            try {
                LauncherHelper.launcherPackageName(mContext, packageName)
            } catch (e: Exception) {
                Toast.makeText(mContext, "启动 $packageName. 失败", Toast.LENGTH_SHORT).show()
            }
        }

        note_nav_add_fab.setOnClickListener {
            val tv = TextView(mContext)
            tv.text = "notifaction . !"
            note_nav_content_base_rl.addHeader(tv)
        }

        setNoteInfoListView()

    }

    /**
     * 设置笔记信息列表
     */
    private fun setNoteInfoListView() {
        val noteInfoList = getFiveNoteInfo(5)
        if (noteInfoList.size <= 0) {
            return
        }
        var maxCount: Int = nearNoteInfoViewList.size
        if (noteInfoList.size < maxCount) {
            maxCount = noteInfoList.size
        }
        for (i in 0..maxCount) {
            val noteInfo = noteInfoList[i]
            val dataBinding = nearNoteInfoViewList[i]
            if (dataBinding != null) {
                dataBinding.noteInfo = noteInfo
                dataBinding.root.visibility = View.VISIBLE
            }
        }
    }

    /**
     * 查询最近信息。
     * @param limit 查询条数
     */
    private fun getFiveNoteInfo(limit: Int): List<NoteInfo> {
        val application = activity?.applicationContext as NoteBookApplication
        val noteInfoDao = application.getDaoSession()?.noteInfoDao
        if (noteInfoDao != null) {
            return noteInfoDao.queryBuilder()
                .where(NoteInfoDao.Properties.NoteInfoId.isNotNull)
                .orderDesc(NoteInfoDao.Properties.UpdateDate)
                .limit(limit)
                .list()
        }
        return emptyList()
    }

}