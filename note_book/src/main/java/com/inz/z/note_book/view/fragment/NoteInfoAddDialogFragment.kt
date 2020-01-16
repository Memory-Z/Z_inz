package com.inz.z.note_book.view.fragment

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.inz.z.base.util.BaseTools
import com.inz.z.base.util.L
import com.inz.z.base.view.AbsBaseDialogFragment
import com.inz.z.note_book.R
import com.inz.z.note_book.bean.NoteInfo
import com.inz.z.note_book.database.controller.NoteController
import java.util.*

/**
 * 显示添加新笔记弹窗
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/12 11:07.
 */
class NoteInfoAddDialogFragment : AbsBaseDialogFragment() {

    companion object {
        const val TAG = "NoteInfoAddDialogFragment"
        var addDialogListener: NoteInfoAddDialogListener? = null
    }

    /**
     * 笔记添加弹窗监听
     */
    interface NoteInfoAddDialogListener {
        /**
         * 笔记提交
         */
        fun onCommitNote(groupId: String)

        /**
         * 界面销毁
         */
        fun onDestroy()
    }

    /**
     * 提交框
     */
    private var commitLl: LinearLayout? = null
    private var commitAddNoteIv: ImageView? = null
    /**
     * 笔记标题编辑内容
     */
    private var noteTitleEt: EditText? = null
    /**
     * 当前组ID
     */
    private var currentGroupId = ""

    override fun initWindow() {
        val theme = R.style.NoteBookAppTheme_Dialog_BottomToTop
        setStyle(DialogFragment.STYLE_NO_FRAME, theme)
    }

    override fun getLayoutId(): Int {
        return R.layout.note_info_add_sample_layout
    }

    override fun initView() {
        commitLl = mView.findViewById(R.id.note_info_add_sample_add_ll)
        commitLl!!.setOnClickListener {
            val titleStr = noteTitleEt?.text.toString()
            if (titleStr.isEmpty() || currentGroupId.isEmpty()) {
                L.i(
                    TAG,
                    "commit return, have empty, input > $titleStr, groupId > $currentGroupId"
                )
                return@setOnClickListener
            }

            val added = addNoteInfo(currentGroupId, titleStr)
            if (added) {
                // 添加数据成功
                targetAddNoteView(false)
                val imm =
                    mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm.isActive) {
                    imm.hideSoftInputFromWindow(mView.windowToken, 0)
                }
                addDialogListener?.onCommitNote(currentGroupId)
                this@NoteInfoAddDialogFragment.dismiss()
            }

        }

        commitAddNoteIv = mView.findViewById(R.id.note_info_add_sample_add_iv)

        noteTitleEt = mView.findViewById(R.id.note_info_add_sample_title_et)
        noteTitleEt!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                targetAddNoteView(s.isNullOrBlank())
            }
        })

    }

    override fun initData() {
        val bundle = arguments
        if (bundle != null) {
            currentGroupId = bundle.getString("groupId", "")
        }
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        if (window != null) {
            val attr = window.attributes
            attr.width = WindowManager.LayoutParams.MATCH_PARENT
            attr.gravity = Gravity.BOTTOM
            window.attributes = attr
            window.setBackgroundDrawableResource(android.R.color.transparent)
            window.setWindowAnimations(R.style.NoteBookAppTheme_Dialog_BottomToTop)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        addDialogListener?.onDestroy()
    }

    /**
     * 切换添加笔记视图
     */
    private fun targetAddNoteView(isShow: Boolean) {
        if (isShow) {
            commitLl?.isClickable = false
            commitAddNoteIv?.apply {
                background = ContextCompat.getDrawable(mContext, R.drawable.bg_card_gray)
            }
        } else {
            commitLl?.isClickable = true
            commitAddNoteIv?.apply {
                background =
                    ContextCompat.getDrawable(mContext, R.drawable.bg_card_main_color)
            }
        }

    }

    /**
     * 添加笔记
     * @param noteGroupId 组id
     * @param noteTitle 标题
     */
    private fun addNoteInfo(noteGroupId: String, noteTitle: String): Boolean {
        val noteInfo = NoteInfo()
            .apply {
                noteInfoId = BaseTools.getUUID()
                setNoteTitle(noteTitle)
                noteContent = ""
                noteStatus = NoteInfo.Status.UNFINISHED
                createDate = Date()
                updateDate = Date()
            }
        return NoteController.addNoteInfo(noteGroupId, noteInfo)
    }

    /**
     * 设置监听
     */
    fun setNoteInfoAddDialogListener(listener: NoteInfoAddDialogListener) {
        addDialogListener = listener
    }


}