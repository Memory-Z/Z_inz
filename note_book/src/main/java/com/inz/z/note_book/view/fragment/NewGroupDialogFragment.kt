package com.inz.z.note_book.view.fragment

import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.inz.z.base.view.AbsBaseDialogFragment
import com.inz.z.note_book.R
import kotlinx.android.synthetic.main.dialog_add_group.*

/**
 * 新分组弹窗
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/29 15:46.
 */
class NewGroupDialogFragment : AbsBaseDialogFragment() {

    companion object {
        /**
         * 获取弹窗实例。
         * @param listener 监听
         */
        fun getInstance(listener: NewGroupDialogFragmentListener): NewGroupDialogFragment {
            val mNewGroupDialogFragment = NewGroupDialogFragment()
            mNewGroupDialogFragment.mNewGroupDialogFragmentListener = listener
            return mNewGroupDialogFragment
        }
    }

    /**
     * 新分组状态监听
     */
    interface NewGroupDialogFragmentListener {
        /**
         * 取消创建
         */
        fun cancelCreate()

        /**
         * 创建新分组
         */
        fun createNewGroup(groupName: String)
    }

    private var mNewGroupDialogFragmentListener: NewGroupDialogFragmentListener? = null


    override fun initWindow() {
        val style = R.style.NoteBookAppTheme_Dialog
        setStyle(DialogFragment.STYLE_NO_FRAME, style)
    }

    override fun getLayoutId(): Int {
        return R.layout.dialog_add_group
    }

    override fun initView() {
        dialog_add_group_title_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.isNotBlank()) {
                        setCreateGroupTextViewStatus(true)
                        return
                    }
                }
                setCreateGroupTextViewStatus(false)

            }
        })

        dialog_add_group_bottom_create_group_tv.setOnClickListener {
            if (dialog_add_group_title_et.text.isNullOrEmpty()) {
                return@setOnClickListener
            }
            mNewGroupDialogFragmentListener?.createNewGroup(dialog_add_group_title_et.text.toString())
        }

        dialog_add_group_bottom_cancel_tv.setOnClickListener {
            mNewGroupDialogFragmentListener?.cancelCreate()
        }

    }

    override fun initData() {

    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        if (window != null) {
            val lp = window.attributes
            lp.gravity = Gravity.CENTER
            window.attributes = lp
            window.setBackgroundDrawableResource(android.R.color.transparent)
        }
        isCancelable = false
    }

    /**
     * 设置 【创建分组】状态
     * @param enable 是否用
     */
    private fun setCreateGroupTextViewStatus(enable: Boolean) {
        if (dialog_add_group_bottom_create_group_tv != null) {
            if (enable) {
                dialog_add_group_bottom_create_group_tv.apply {
                    setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.nearColorBlue
                        )
                    )
                    isFocusable = true
                    isClickable = true
                }
            } else {
                dialog_add_group_bottom_create_group_tv.apply {
                    setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.textColor50
                        )
                    )
                    isFocusable = false
                    isClickable = false
                }
            }
        }
    }
}