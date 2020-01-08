package com.inz.z.list_form.view.widget

import java.io.Serializable

/**
 * 列表实体
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2020/01/08 11:40.
 */
object ListFormBean : Serializable {
    /**
     * 内容
     */
    var content: String = ""
    /**
     * 级别 0 > 1 > 2;
     */
    var level: Int = 0
    /**
     * 子元素
     */
    var childList: MutableList<ListFormBean> = mutableListOf()

}