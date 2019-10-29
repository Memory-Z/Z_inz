package com.inz.z.note_book.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.inz.z.base.util.BaseTools
import com.inz.z.base.util.L
import com.inz.z.base.util.LauncherHelper
import com.inz.z.base.view.AbsBaseFragment
import com.inz.z.note_book.NoteBookApplication
import com.inz.z.note_book.R
import com.inz.z.note_book.bean.NoteGroup
import com.inz.z.note_book.bean.NoteInfo
import com.inz.z.note_book.database.NoteInfoDao
import com.inz.z.note_book.view.activity.GroupActivity
import com.inz.z.note_book.view.widget.ItemNoteGroupLayout
import com.inz.z.note_book.view.widget.ItemSampleNoteInfoLayout
import kotlinx.android.synthetic.main.note_nav_hint_layout.*
import kotlinx.android.synthetic.main.note_nav_layout.*
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

    override fun initWindow() {

    }

    override fun getLayoutId(): Int {
        return R.layout.note_nav_layout
    }

    override fun initView() {

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

//        note_nav_add_fab.setOnClickListener {
//            val tv = TextView(mContext)
//            val str = "notification . ! ${System.currentTimeMillis()}"
//            tv.text = str
//            tv.setPadding(4, 4, 4, 4)
//            note_nav_content_base_rl.showHeaderNotification(tv, -1)
//
//        }

        setNoteInfoListView()

        note_nav_group_right_iv.setOnClickListener {
            L.i(TAG, "note_nav_group_right_iv  is Click ! ")
            val intent = Intent(mContext, GroupActivity::class.java)
            val bundle = Bundle()
            bundle.apply {
                putBoolean("addNewGroup", true)
                putString("groupId", "")
            }
            intent.putExtras(bundle)
            startActivity(intent)
        }

    }


    override fun onStart() {
        super.onStart()
        setNoteGroupListView()
    }

    /**
     * 设置笔记信息列表
     */
    private fun setNoteInfoListView() {
        val noteInfoList = getFiveNoteInfo(5)
        if (noteInfoList.isNotEmpty()) {
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            lp.topMargin = BaseTools.dp2px(mContext, 8F)
            for (noteInfo in noteInfoList) {
                if (mContext != null) {
                    val itemSampleNoteInfoLayout = ItemSampleNoteInfoLayout(mContext)
                    itemSampleNoteInfoLayout.setSampleNoteInfo(noteInfo)
                    itemSampleNoteInfoLayout.setSampleOnClickListener(object :
                        View.OnClickListener {
                        override fun onClick(v: View?) {
                            L.i(TAG, "itemSampleNoteInfoLayout is click. ")
                        }
                    })
                    note_nav_near_five_note_ll.addView(itemSampleNoteInfoLayout, lp)
                }
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

    /**
     * 设置组信息列表 View
     */
    private fun setNoteGroupListView() {
        val noteGroupList = getAllNoteGroup()
        if (noteGroupList.isNotEmpty()) {
            val lp = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            for (noteGroup in noteGroupList) {
                if (mContext != null) {
                    val itemNoteGroupLayout = ItemNoteGroupLayout(mContext)
                    itemNoteGroupLayout.setGroupData(noteGroup)
                    itemNoteGroupLayout.setGroupOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            L.i(TAG, "itemNoteGroupLayout is click ! ")
                        }
                    })
                    L.i(TAG, " itemNoteGroupLayout height = ${itemNoteGroupLayout.height} -- ${note_nav_group_ll.height}")
                    note_nav_group_ll.addView(itemNoteGroupLayout, lp)
                }
            }
        }
    }

    /**
     * 查询全部分组
     */
    private fun getAllNoteGroup(): List<NoteGroup> {
        val application = activity?.applicationContext as NoteBookApplication
        val noteGroupDao = application.getDaoSession()?.noteGroupDao
        if (noteGroupDao != null) {
            return noteGroupDao.loadAll()
        }
        return emptyList()
    }

}