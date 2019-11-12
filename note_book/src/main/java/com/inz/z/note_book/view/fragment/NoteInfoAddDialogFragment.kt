package com.inz.z.note_book.view.fragment

import android.graphics.Point
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.inz.z.base.view.AbsBaseDialogFragment
import com.inz.z.note_book.R

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
    }

    private var commitLl: LinearLayout? = null
    private var noteTitleEt: EditText? = null

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

        }
    }

    override fun initData() {

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


}