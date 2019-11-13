package com.inz.z.note_book.view.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDialog
import com.inz.z.note_book.R

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/12 11:20.
 */
@Deprecated("miss")
class AddNoteInfoDialog : AppCompatDialog {
    constructor(context: Context?) : this(context, R.style.NoteBookAppTheme_Dialog_BottomToTop)
    constructor(context: Context?, theme: Int) : super(context, theme) {
        initView()
    }

    constructor(
        context: Context?,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener) {
        initView()
    }

    /**
     * 初始化视图
     */
    private fun initView() {
        setContentView(R.layout.note_info_add_sample_layout)
    }

    open class Builder {


        private lateinit var context: Context
        private var theme: Int = R.style.NoteBookAppTheme_Dialog_BottomToTop

        private var dialogView: View? = null
        private var commitLl: LinearLayout? = null
        private var noteTitleEt: EditText? = null

        constructor(context: Context) {
            Builder(context, R.style.NoteBookAppTheme_Dialog_BottomToTop)
        }

        constructor(context: Context, theme: Int) {
            this.context = context
            this.theme = theme
            initView()
        }

        @SuppressLint("InflateParams")
        private fun initView() {
            dialogView = LayoutInflater.from(context)
                .inflate(R.layout.note_info_add_sample_layout, null, false)
            dialogView?.apply {
                commitLl = findViewById(R.id.note_info_add_sample_add_ll)
                noteTitleEt = findViewById(R.id.note_info_add_sample_title_et)
            }

        }

        fun setCommitClickListener(listener: View.OnClickListener): Builder {
            commitLl?.setOnClickListener(listener)
            return this
        }

        fun create(): AddNoteInfoDialog {
            val dialog = AddNoteInfoDialog(context, theme)
            dialog.apply {
                setContentView(dialogView!!)
                setCancelable(true)
                setCanceledOnTouchOutside(true)
            }
            return dialog
        }

    }

    override fun onWindowAttributesChanged(params: WindowManager.LayoutParams?) {
        super.onWindowAttributesChanged(params)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }


}