package com.inz.z.view.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.inz.z.R
import com.inz.z.base.AbsBaseRvAdapter
import com.inz.z.base.AbsBaseRvViewHolder
import com.inz.z.entity.constants.ExampleBean
import com.inz.z.view.activity.ExMainActivity

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/12/30 16:23.
 */
class ExampleAdapter :
    AbsBaseRvAdapter<ExampleBean, ExampleAdapter.ExampleRvHolder> {

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, exampleBeanList: List<ExampleBean>) : super(context) {
        refreshData(exampleBeanList)
    }

    override fun onCreateVH(parent: ViewGroup, viewType: Int): ExampleRvHolder {
        val view: View = mLayoutInflater
            .inflate(R.layout.base_operation_text_layout, parent, false)
        return ExampleRvHolder(view)
    }

    override fun onBindVH(holder: ExampleRvHolder, position: Int) {
        val exampleBean: ExampleBean = list[position]
        if (exampleBean.isHaveLeftIcon) {
            holder.leftIconIv?.visibility = View.VISIBLE
            holder.leftIconIv?.setImageDrawable(
                ContextCompat.getDrawable(mContext, exampleBean.leftIconRes)
            )
        } else {
            holder.leftIconIv?.visibility = View.GONE
        }

        holder.contentTextTv?.text = exampleBean.contentStr
        holder.contentHintTv?.text = exampleBean.contentHintStr
        holder.itemView.setOnClickListener {
            val intent = Intent()
            intent.setClass(mContext, ExMainActivity::class.java)
            val bundle = Bundle()
            bundle.putString("clzName", exampleBean.clzName)
            bundle.putInt("showType", ExMainActivity.ShowType.FULL_SCREEN.ordinal)
            intent.putExtras(bundle)
            mContext.startActivity(intent)

        }
    }

    inner class ExampleRvHolder(item: View) : AbsBaseRvViewHolder(item) {
        /**
         * 顶部线
         */
        var topLineV: View? = null
        var bottomLineV: View? = null
        var contentRl: RelativeLayout? = null
        var leftIconIv: ImageView? = null
        var contentTextTv: TextView? = null
        var contentHintTv: TextView? = null
        var rightIconIv: ImageView? = null


        init {
            topLineV = item.findViewById(R.id.base_op_text_top_line_v)
            bottomLineV = item.findViewById(R.id.base_op_text_bottom_line_v)
            contentRl = item.findViewById(R.id.base_op_text_rl)
            leftIconIv = item.findViewById(R.id.base_op_text_content_left_icon_iv)
            contentTextTv = item.findViewById(R.id.base_op_text_content_tv)
            contentHintTv = item.findViewById(R.id.base_op_text_content_hint_tv)
            rightIconIv = item.findViewById(R.id.base_op_text_content_right_icon_iv)
        }

    }
}