package com.inz.z.note_book.view.app_widget.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.inz.z.note_book.R
import com.inz.z.note_book.bean.NoteInfo

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/08 15:25.
 */
class WidgetNoteInfoListAdapter : BaseAdapter {

    private var mContext: Context? = null
    private var noteInfoList: List<NoteInfo> = mutableListOf()
    private var layoutInflater: LayoutInflater? = null

    private constructor() : super()

    constructor(mContext: Context?) : super() {
        this.mContext = mContext
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var viewHolder: NoteInfoSampleViewHolder? = null
        if (convertView == null) {
            view = layoutInflater!!.inflate(R.layout.widget_item_note_sample_layout, parent, false)

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
        var noteTitleTv: TextView? = null
        var noteContentTv: TextView? = null
    }

}