package com.inz.z.note_book.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.inz.z.base.base.AbsBaseRvAdapter
import com.inz.z.base.base.AbsBaseRvViewHolder
import com.inz.z.base.util.BaseTools
import com.inz.z.note_book.R
import com.inz.z.note_book.bean.NoteInfo
import com.inz.z.note_book.databinding.ItemNoteLayoutBinding

/**
 * 笔记信息列表 Recycler View 适配器
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/29 11:35.
 */
class NoteInfoRecyclerAdapter(mContext: Context?) :
    AbsBaseRvAdapter<NoteInfo, NoteInfoRecyclerAdapter.NoteInfoRecyclerViewHolder>(mContext) {

    companion object {
        var noteInfoRvAdapterListener: NoteInfoRvAdapterListener? = null
    }

    interface NoteInfoRvAdapterListener {
        fun onItemClickListener(v: View, position: Int)
    }

    override fun onCreateVH(parent: ViewGroup, viewType: Int): NoteInfoRecyclerViewHolder {
        val mView = mLayoutInflater.inflate(R.layout.item_note_layout, parent, false)
        return NoteInfoRecyclerViewHolder(mView)
    }

    override fun onBindVH(holder: NoteInfoRecyclerViewHolder, position: Int) {
        val noteInfo = list[position]
        holder.mItemNoteLayoutBinding?.noteInfo = noteInfo
        holder.mItemNoteLayoutBinding?.noteInfoUpdateDateStr =
            BaseTools.getBaseDateFormat().format(noteInfo.updateDate)
    }

    class NoteInfoRecyclerViewHolder(itemView: View) : AbsBaseRvViewHolder(itemView) {

        var mItemNoteLayoutBinding: ItemNoteLayoutBinding? = null

        init {
            mItemNoteLayoutBinding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener {
                noteInfoRvAdapterListener?.onItemClickListener(it, adapterPosition)
            }
        }
    }

    /**
     * 设置笔记信息列表适配器监听
     */
    fun setNoteInfoRvAdapterListener(listener: NoteInfoRvAdapterListener) {
        noteInfoRvAdapterListener = listener
    }

    /**
     * 添加单项数据
     */
    fun addNoteInfo(noteInfo: NoteInfo) {
        list.add(noteInfo)
        notifyDataSetChanged()
    }

    /**
     * 添加列表数据
     */
    fun addNoteInfoList(noteInfoList: List<NoteInfo>) {
        list.addAll(noteInfoList)
        notifyDataSetChanged()
    }

    /**
     * 替换数据列表
     */
    fun replaceNoteInfoList(noteInfoList: List<NoteInfo>) {
        list.clear()
        list.addAll(noteInfoList)
        notifyDataSetChanged()
    }
}