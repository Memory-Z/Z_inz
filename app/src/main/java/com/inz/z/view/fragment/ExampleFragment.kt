package com.inz.z.view.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import com.inz.z.R
import com.inz.z.entity.constants.ExampleBean
import com.inz.z.util.FileUtils
import com.inz.z.view.adapter.ExampleAdapter

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/12/30 16:12.
 */
class ExampleFragment : AbsBaseFragment() {
    private val _tag = "ExampleFragment"

    private var exampleRv: androidx.recyclerview.widget.RecyclerView? = null
    private var exampleSearchRl: RelativeLayout? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_example
    }

    override fun initView() {
        exampleRv = mView.findViewById(R.id.fragment_example_rv)
        exampleRv?.addOnScrollListener(ScrollLis())
        exampleSearchRl = mView.findViewById(R.id.fragment_example_search_rl)
    }

    private var exampleAdapter: ExampleAdapter? = null

    override fun initData() {
        val xmlParse = mContext.resources.getXml(R.xml.example_bean)
        exampleAdapter = ExampleAdapter(mContext)
        exampleRv?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(mContext)
        Log.i(_tag, "-----" + System.currentTimeMillis())
        // 通过 xml 文件 获取 列表数据
        val exampleBeanList: MutableList<ExampleBean> = FileUtils.parseExampleXmlWithPull(xmlParse)
        exampleAdapter?.refreshData(exampleBeanList)
        exampleRv?.adapter = exampleAdapter
        Log.i(_tag, "=====" + System.currentTimeMillis())
    }

    inner class ScrollLis : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            // 改变后 的如果大于改变前，手指从上向下移动
            if (dy < 0) {
                exampleSearchRl?.visibility = View.VISIBLE
            } else {
                exampleSearchRl?.visibility = View.GONE
            }
        }
    }


}