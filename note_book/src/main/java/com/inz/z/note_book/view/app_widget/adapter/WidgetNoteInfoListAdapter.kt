package com.inz.z.note_book.view.app_widget.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
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
 * Create by inz in 2019/11/08 15:25.
 */
class WidgetNoteInfoListAdapter : BaseAdapter {

    companion object {
        const val TAG = "WidgetNoteInfoListAdapter"
    }

    private var mContext: Context? = null
    private var noteInfoList: List<NoteInfo> = mutableListOf()
    private var layoutInflater: LayoutInflater? = null

    private constructor() : super()

    constructor(mContext: Context?) : super() {
        this.mContext = mContext
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val viewHolder: NoteInfoSampleViewHolder?
        if (convertView == null) {
            viewHolder = NoteInfoSampleViewHolder()
            view = layoutInflater!!.inflate(R.layout.widget_item_note_sample_layout, parent, false)
            val binding = DataBindingUtil.bind<WidgetItemNoteSampleLayoutBinding>(view)
            if (binding == null) {
                L.w(TAG, "data binding not bind. check this. ----> ${this}")
            }
            viewHolder.binding = binding
            view!!.tag = viewHolder
        } else {
            viewHolder = convertView.tag as NoteInfoSampleViewHolder?
        }
        if (viewHolder != null) {
            viewHolder.binding?.apply {
                val info = noteInfoList[position]
                noteInfo = info
                val dateStr = BaseTools.getBaseDateFormat().format(info.updateDate)
                noteInfoUpdateDateStr = dateStr
            }
        } else {
            L.i(TAG, "viewHolder is null ! -- ${convertView}")
        }


        return view!!
    }

    override fun getItem(position: Int): Any {
        return noteInfoList[position]
    }

    override fun getItemId(position: Int): Long {
        return noteInfoList[position].hashCode().toLong()
    }

    override fun getCount(): Int {
        return noteInfoList.size
    }

    inner class NoteInfoSampleViewHolder {
        var binding: WidgetItemNoteSampleLayoutBinding? = null
//        var noteTitleTv: TextView? = null
//        var noteContentTv: TextView? = null
    }

}