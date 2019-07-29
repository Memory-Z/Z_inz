package com.inz.z.form.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.inz.z.form.view.widget.BaseLineChartView

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/24 18:09.
 */
@Route(path = "/from/MainActivity", group = "from")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(BaseLineChartView(this))
    }
}