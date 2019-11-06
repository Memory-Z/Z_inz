package com.inz.z.note_book.view.activity

import com.inz.z.base.util.L
import com.inz.z.base.view.AbsBaseActivity
import com.inz.z.note_book.R
import com.inz.z.note_book.bean.NoteInfo
import com.inz.z.note_book.database.controller.NoteInfoController
import com.inz.z.note_book.view.widget.ScheduleLayout
import kotlinx.android.synthetic.main.note_info_add_layout.*

/**
 * 新笔记
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/25 16:36.
 */
class NewNoteActivity : AbsBaseActivity() {

    companion object {
        const val TAG = "NewNoteActivity"
    }

    /**
     * 列表内容
     */
    private var noteTextList: List<ScheduleLayout> = mutableListOf()
    private var noteInfoId = ""
    private var noteInfo: NoteInfo? = null

    override fun initWindow() {

    }

    override fun getLayoutId(): Int {
        return R.layout.note_info_add_layout
    }


    override fun initView() {
//        note_info_add_content_content_nsv.setOnClickListener {
//            if (noteTextList.isNotEmpty()) {
//                val lastScheduleLayout = noteTextList.last()
//                lastScheduleLayout.apply {
//                    requestFocus()
//                    performClick()
//                }
//            }
//        }

    }

    override fun initData() {
        val bundle = intent.extras
        if (bundle != null) {
            noteInfoId = bundle.getString("noteInfoId", "")
        }
        if (noteInfoId.isNotEmpty()) {
            noteInfo = NoteInfoController.findById(noteInfoId)
            noteInfo?.apply {
                note_info_add_top_title_tv.text = noteTitle
                note_info_add_content_schedule_layout.setContent(noteContent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val nslHeight = note_info_add_content_content_nsv.height
        val topHeight = note_info_add_content_top_time_tv.height

        L.i(
            TAG,
            "onResume : ------ $nslHeight -------- $topHeight ----- ${window.attributes.height}"
        )
    }
}