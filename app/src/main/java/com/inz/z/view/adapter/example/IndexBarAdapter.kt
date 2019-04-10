package com.inz.z.view.adapter.example

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.inz.z.R
import com.inz.z.base.AbsBaseRvAdapter
import com.inz.z.base.AbsBaseRvViewHolder
import com.inz.z.view.fragment.example.bean.CityPinyin

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/31 11:07.
 */
class IndexBarAdapter :
    AbsBaseRvAdapter<CityPinyin, IndexBarAdapter.IndexBarHolderView> {
    private var mDatas: MutableList<CityPinyin> = ArrayList()

    constructor(mContext: Context?) : super(mContext)

    constructor(mContext: Context?, mDatas: MutableList<CityPinyin>) : super(mContext) {
        this.mDatas = mDatas
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    override fun onCreateVH(parent: ViewGroup, viewType: Int): IndexBarHolderView {
        val mView = mLayoutInflater.inflate(R.layout.ex_item_index_bar, parent, false)
        return IndexBarHolderView(mView)
    }

    override fun onBindVH(holder: IndexBarHolderView, position: Int) {
        val cityPinyin = mDatas[position]
        holder.textView?.text = cityPinyin.cityName
    }

    override fun loadMoreData(list: MutableList<CityPinyin>?) {
        if (list != null) {
            this.mDatas.addAll(list)
        }
        notifyDataSetChanged()
    }

    inner class IndexBarHolderView(itemView: View) : AbsBaseRvViewHolder(itemView) {
        var textView: TextView? = null

        init {
            textView = itemView.findViewById(R.id.exx_item_index_bar_tv)
        }

    }

}