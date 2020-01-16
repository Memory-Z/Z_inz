package com.inz.z.note_book.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.inz.z.base.util.BaseTools
import com.inz.z.base.util.L
import com.inz.z.note_book.R
import com.inz.z.note_book.bean.NoteInfo
import com.inz.z.note_book.databinding.WidgetItemNoteSampleLayoutBinding

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/29 10:17.
 */
class ItemSampleNoteInfoLayout : LinearLayout {
    private var mView: View? = null
    private var mItemSampleNoteInfoLayoutBinding: WidgetItemNoteSampleLayoutBinding? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        if (context == null) {
            L.i("ItemSampleNoteInfoLayout", "init ItemSampleNoteInfoLayout:  context is null !!! ")
            return
        }
        initView(context)
    }

    private fun initView(context: Context) {
        if (mView == null) {
            mItemSampleNoteInfoLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.widget_item_note_sample_layout,
                this,
                true
            )
            mView = mItemSampleNoteInfoLayoutBinding?.root
//            if (mView != null) {
//                val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
//                addView(mView, lp)
//            }
        }
    }

    /**
     * 设置笔记内容
     * @param noteInfo 笔记
     */
    fun setSampleNoteInfo(noteInfo: NoteInfo) {
        if (mItemSampleNoteInfoLayoutBinding != null) {
            mItemSampleNoteInfoLayoutBinding!!.noteInfo = noteInfo
            val updateDateStr = BaseTools.getBaseDateFormat().format(noteInfo.updateDate)
            mItemSampleNoteInfoLayoutBinding!!.noteInfoUpdateDateStr = updateDateStr
            mItemSampleNoteInfoLayoutBinding!!.notifyChange()
        }
    }

    /**
     * 设置点击事件监听
     * @param listener 点击事件
     */
    fun setSampleOnClickListener(listener: OnClickListener) {
        if (mView != null) {
            mView!!.setOnClickListener(listener)
        }
    }
}