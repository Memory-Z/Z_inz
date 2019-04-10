package com.inz.z.view.fragment.example.bean

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/31 10:35.
 */
class CityPinyin : BaseIndexPinyinBean {
    var cityName: String = ""

    constructor() : super()

    constructor(cityName: String) : super() {
        this.cityName = cityName
    }


    override fun getTarget(): String {
        return cityName
    }

    override fun isShowSuspension(): Boolean {
        return true
    }
}