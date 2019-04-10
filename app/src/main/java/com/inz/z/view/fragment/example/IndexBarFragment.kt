package com.inz.z.view.fragment.example

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.inz.z.view.fragment.AbsBaseFragment
import com.inz.z.R
import com.inz.z.view.adapter.example.IndexBarAdapter
import com.inz.z.view.fragment.example.bean.CityPinyin
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/31 10:10.
 */
class IndexBarFragment : AbsBaseFragment() {
    private var indexBar: IndexBar? = null
    private var indexBarAdapter: IndexBarAdapter? = null
    private var rv: RecyclerView? = null
    private var mDatas: MutableList<CityPinyin> = ArrayList()
    private var d: SuspensionDecoration? = null
    override fun getLayoutId(): Int {
        return R.layout.ex_fragment_index_bar
    }

    override fun initView() {
        indexBar = mView?.findViewById(R.id.ex_fragment_index_bar_ib)
        rv = mView?.findViewById(R.id.ex_fragment_index_bar_rv)
        val hintTv: TextView? = mView?.findViewById(R.id.ex_fragment_index_bar_hint_tv)
        val manager = LinearLayoutManager(mContext)
        rv?.layoutManager = manager
        indexBarAdapter = IndexBarAdapter(mContext)
        rv?.adapter = indexBarAdapter
        d = SuspensionDecoration(mContext, mDatas)
        rv?.addItemDecoration(d!!)
        indexBar
            ?.setmPressedShowTextView(hintTv)
            ?.setNeedRealIndex(true)
            ?.setmLayoutManager(manager)
    }

    override fun initData() {
        val datas = resources.getStringArray(R.array.provinces)
        for (city in datas) {
            mDatas.add(CityPinyin(city))
        }
        indexBar?.dataHelper?.sortSourceDatas(mDatas)
        indexBarAdapter?.loadMoreData(mDatas)
//        indexBarAdapter?.notifyDataSetChanged()

        indexBar?.setmSourceDatas(mDatas)?.invalidate()
        d?.setmDatas(mDatas)

    }
}