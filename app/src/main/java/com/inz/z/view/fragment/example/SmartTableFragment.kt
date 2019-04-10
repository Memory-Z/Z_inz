package com.inz.z.view.fragment.example

import com.bin.david.form.core.SmartTable
import com.bin.david.form.data.column.Column
import com.bin.david.form.data.table.TableData
import com.inz.z.R
import com.inz.z.view.fragment.AbsBaseFragment

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/31 9:49.
 */
class SmartTableFragment : AbsBaseFragment() {
    private var smartTable: SmartTable<String>? = null

    override fun getLayoutId(): Int {
        return R.layout.ex_fragment_smart_table
    }

    override fun initView() {
        smartTable = mView.findViewById(R.id.ex_fragment_smart_table_st)
    }

    override fun initData() {
        val dataList: MutableList<String> = ArrayList()

        val col1 = Column<String>("姓名", "name")
        val col2 = Column<String>("年龄", "year")
        val col3 = Column<String>("班级", "claz")
        val col4 = Column<String>("爱好", "love")
        val col5 = Column<String>("目标", "mou")
        val col45 = Column<String>("其他", col4, col5)
        val tabData = TableData<String>("表格名", dataList, col1)

        smartTable?.tableData = tabData
        smartTable?.setZoom(false)

    }
}