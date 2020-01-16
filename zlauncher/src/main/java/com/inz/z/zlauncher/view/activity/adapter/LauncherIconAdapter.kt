package com.inz.z.zlauncher.view.activity.adapter

import android.content.Context
import android.content.pm.ResolveInfo
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.inz.z.base.base.AbsBaseRvAdapter
import com.inz.z.base.base.AbsBaseRvViewHolder
import com.inz.z.zlauncher.R

/**
 * 桌面 图标适配器
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/12/31 10:52.
 */
class LauncherIconAdapter(mContext: Context) :
    AbsBaseRvAdapter<ResolveInfo, LauncherIconAdapter.LauncherIconVH>(
        mContext
    ) {

    companion object {
        var launcherIconAdapterListener: LauncherIconAdapterListener? = null
    }

    fun setLauncherIconAdapterListener(listener: LauncherIconAdapterListener) {
        launcherIconAdapterListener = listener
    }

    /**
     * 桌面图标监听事件
     */
    interface LauncherIconAdapterListener {
        /**
         * 项点击
         */
        fun onItemClick(v: View?, position: Int)
    }

    private var options: RequestOptions = RequestOptions()
        .timeout(15000)

    override fun onCreateVH(parent: ViewGroup, viewType: Int): LauncherIconVH {
        val view = mLayoutInflater.inflate(R.layout.item_launcher_icon, parent, false)
        return LauncherIconVH(view)
    }

    override fun onBindVH(holder: LauncherIconVH, position: Int) {
        val resolveInfo = list[position]
        Glide.with(mContext).load(resolveInfo.loadIcon(mContext.packageManager)).apply(options)
            .into(holder.iconIv)
        val appName =
            mContext.packageManager.getApplicationLabel(resolveInfo.activityInfo.applicationInfo)
        holder.appNameTv.text = appName
    }

    /**
     * 图标
     */
    class LauncherIconVH(itemView: View) : AbsBaseRvViewHolder(itemView), View.OnClickListener {
        var iconIv: ImageView
        var appNameTv: TextView

        init {
            iconIv = itemView.findViewById(R.id.item_launcher_icon_iv)
            appNameTv = itemView.findViewById(R.id.item_launcher_name_tv)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            launcherIconAdapterListener?.onItemClick(v, adapterPosition)
        }
    }
}