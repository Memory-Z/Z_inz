package com.inz.z.note_book.view.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.inz.z.base.view.widget.BaseNavLayout
import com.inz.z.note_book.R
import com.inz.z.note_book.bean.NoteGroup
import com.inz.z.note_book.databinding.ItemNoteGroupLayoutBinding

/**
 * 单项组布局
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/29 09:39.
 */
class ItemNoteGroupLayout : BaseNavLayout {
    private var mView: View? = null
    private var itemNoteGroupBinding: ItemNoteGroupLayoutBinding? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        if (context == null) {
            Log.i("ItemNoteGroupLayout", "init ItemNoteGroupLayout : Context is null. !!! ")
            return
        }
        initView(context)
    }

    /**
     * 初始化布局
     */
    private fun initView(context: Context) {
        if (mView == null) {
//            mView =
//                LayoutInflater.from(context).inflate(R.layout.item_note_group_layout, null, false)
            itemNoteGroupBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.item_note_group_layout,
                null,
                false
            )
            mView = itemNoteGroupBinding?.root
            if (mView != null) {
//                itemNoteGroupBinding = DataBindingUtil.findBinding(mView!!)
                val layoutParams =
                    LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                addView(mView, layoutParams)
            }
        }
    }

    /**
     * 设置组点击事件
     */
    fun setGroupOnClickListener(listener: OnClickListener) {
        if (mView != null) {
            mView!!.setOnClickListener(listener)
        }
    }

    /**
     * 设置组数据
     * @param noteGroup 组
     */
    fun setGroupData(noteGroup: NoteGroup) {
        if (itemNoteGroupBinding != null) {
            itemNoteGroupBinding!!.noteGroup = noteGroup
            val noteGroupNumber = noteGroup.noteInfoList?.size
            itemNoteGroupBinding!!.noteGroupSize =
                noteGroupNumber?.toString() ?: ""
            itemNoteGroupBinding!!.notifyChange()
        }
    }
}