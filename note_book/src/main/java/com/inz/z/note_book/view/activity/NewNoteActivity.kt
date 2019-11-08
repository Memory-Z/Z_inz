package com.inz.z.note_book.view.activity

import com.inz.z.base.util.BaseTools
import com.inz.z.base.util.L
import com.inz.z.base.view.AbsBaseActivity
import com.inz.z.note_book.R
import com.inz.z.note_book.bean.NoteInfo
import com.inz.z.note_book.database.controller.NoteInfoController
import kotlinx.android.synthetic.main.note_info_add_layout.*
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

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
     * 笔记ID
     */
    private var noteInfoId = ""
    /**
     * 笔记
     */
    private var noteInfo: NoteInfo? = null
    /**
     * 已保存笔记内容
     */
    private var oldNoteContent = ""
    private var executorService: ScheduledExecutorService

    init {
        executorService = Executors.newScheduledThreadPool(3)
    }

    override fun initWindow() {

    }

    override fun getLayoutId(): Int {
        return R.layout.note_info_add_layout
    }


    override fun initView() {
        note_info_add_top_finish_tv.setOnClickListener {
            val newContent = note_info_add_content_schedule_layout.getContent()
            if (!oldNoteContent.equals(newContent)) {
                if (noteInfo != null) {
                    noteInfo!!.apply {
                        noteContent = newContent
                        updateDate = Date()
                    }
                    saveNoteInfo(noteInfo!!)
                } else {
                    L.w(TAG, "note_info is null. ")
                }
            }
        }
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
                note_info_add_content_top_time_tv.text =
                    BaseTools.getBaseDateFormat().format(updateDate)
                oldNoteContent = noteContent
            }
        }
        // 每 5 S 检测一次
        executorService.scheduleAtFixedRate(checkNoteRunnable, 5, 5, TimeUnit.SECONDS)
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

    /**
     * 保存笔录信息
     */
    private fun saveNoteInfo(noteInfo: NoteInfo) {
        NoteInfoController.updateNoteInfo(noteInfo)
    }

    /**
     * 检测笔记线程
     */
    private var checkNoteRunnable = object : Runnable {
        override fun run() {
            val noteInfo = NoteInfoController.findById(noteInfoId)
            L.i(
                TAG,
                "noteInfo ${System.currentTimeMillis()} -- ${noteInfo?.noteContent} + {${Thread.currentThread().name}}"
            )
        }
    }
}