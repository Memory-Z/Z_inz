package com.inz.z.note_book.view.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.inz.z.note_book.R
import com.inz.z.note_book.bean.NoteGroup
import com.inz.z.note_book.database.controller.NoteGroupWithInfoService
import com.inz.z.note_book.databinding.ItemNoteGroupLayoutBinding

/**
 * 单项组布局
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/29 09:39.
 */
class ItemNoteGroupLayout : LinearLayout {
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
//            LayoutInflater.from(context).inflate(R.layout.item_note_group_layout, this, true)
//            mView = findViewById(R.id.item_note_group_bnl)
            itemNoteGroupBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.item_note_group_layout,
                this,
                true
            )
            mView = itemNoteGroupBinding?.root
//            if (mView != null) {
//                itemNoteGroupBinding = DataBindingUtil.bind(mView!!)
//            }
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
            val noteGroupNumber =
                NoteGroupWithInfoService.getGroupChildCountByGroupId(noteGroup.noteGroupId)
            itemNoteGroupBinding!!.noteGroupSize =
                if (noteGroupNumber == 0L) "" else noteGroupNumber.toString()
            itemNoteGroupBinding!!.notifyChange()
        }
    }
}