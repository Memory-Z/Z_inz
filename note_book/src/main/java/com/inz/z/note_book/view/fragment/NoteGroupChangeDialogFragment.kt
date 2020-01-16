package com.inz.z.note_book.view.fragment

import android.view.Gravity
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.inz.z.base.view.AbsBaseDialogFragment
import com.inz.z.note_book.R

/**
 * 更改AppWidget 所选分组
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/13 10:32.
 */
class NoteGroupChangeDialogFragment : AbsBaseDialogFragment() {
    override fun initWindow() {
        val theme = R.style.NoteBookAppTheme_Dialog_BottomToTop
        setStyle(DialogFragment.STYLE_NO_FRAME, theme)
    }

    override fun getLayoutId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        if (window != null) {
            val attr = window.attributes
            attr.gravity = Gravity.BOTTOM
            attr.width = WindowManager.LayoutParams.MATCH_PARENT
            window.attributes = attr
            window.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
}