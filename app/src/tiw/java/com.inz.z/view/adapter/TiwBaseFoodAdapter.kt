package com.inz.z.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.inz.z.R
import com.inz.z.base.AbsBaseRvAdapter
import com.inz.z.base.AbsBaseRvViewHolder
import com.inz.z.bean.TiwBean
import com.inz.z.bean.TiwFood
import de.hdodenhof.circleimageview.CircleImageView

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/27 20:02.
 */
class TiwBaseFoodAdapter(mContext: Context?) :
    AbsBaseRvAdapter<TiwBean, AbsBaseRvViewHolder>(mContext) {

    private var viewHolderType: HolderType = HolderType.All
    private var requestOptions: RequestOptions? = null

    private var tiwFoodList: MutableList<TiwFood> = ArrayList()
    var bottomShowText: Boolean = false

    private enum class HolderType {
        All, M, L, XL, XXL, BOTTOM
    }

    init {
        requestOptions = RequestOptions()
            .error(R.drawable.tiw_ic_vd_tulie)
            .timeout(60000)
            .skipMemoryCache(false)
            .useAnimationPool(true)
            .placeholder(R.drawable.tiw_ic_vd_image_holder)
    }

    override fun getItemCount(): Int {
        return tiwFoodList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position >= tiwFoodList.size) {
            return HolderType.BOTTOM.ordinal
        }
        return HolderType.All.ordinal
    }

    override fun onCreateVH(parent: ViewGroup, viewType: Int): AbsBaseRvViewHolder {
        if (viewType == HolderType.All.ordinal) {
            val mView = mLayoutInflater.inflate(R.layout.tiw_item_base_food, parent, false)
            return TiwBaseFoodViewHolder(mView)
        } else {
            val mView = mLayoutInflater.inflate(R.layout.tiw_item_food_bottom, parent, false)
            return TiwBottomViewHolder(mView)
        }

    }

    override fun onBindVH(holder: AbsBaseRvViewHolder, position: Int) {
        if (holder is TiwBaseFoodViewHolder) {
            val tiwFood: TiwFood = tiwFoodList[position]
            val foodImageView = holder.foodImageCiv;
            Glide.with(mContext)
                .load(tiwFood.foodImageUrl)
                .apply(requestOptions!!)
                .into(foodImageView!!)
            holder.foodNameTv!!.text = tiwFood.foodName
            holder.foodDetailTv!!.text = tiwFood.foodDetail
        } else if (holder is TiwBottomViewHolder) {
            val bottomProgressBar = holder.bottomProgressbar
            bottomProgressBar?.visibility = if (bottomShowText) View.GONE else View.VISIBLE
            val textLl = holder.bottomTextContentRl
            textLl?.visibility = if (bottomShowText) View.VISIBLE else View.GONE
        }
    }

    /**
     * 添加
     */
    public fun addTiwFoodList(tiwFoodList: List<TiwFood>) {
        this.tiwFoodList.addAll(tiwFoodList)
        notifyDataSetChanged()
    }

    public fun replaceRiwFoodList(tiwFoodList: List<TiwFood>) {
        this.tiwFoodList.removeAll {
            true
        }
        this.tiwFoodList.addAll(tiwFoodList)
        notifyDataSetChanged()
    }


    /**
     * 食品ViewHolder
     */
    inner class TiwBaseFoodViewHolder(itemView: View) : AbsBaseRvViewHolder(itemView) {
        var foodImageCiv: CircleImageView? = null
        var foodNameTv: TextView? = null
        var foodDetailTv: TextView? = null
        var buyBtn: Button? = null
        var cartBtn: Button? = null

        init {
            foodImageCiv = itemView.findViewById(R.id.tiw_item_base_food_left_civ)
            foodNameTv = itemView.findViewById(R.id.tiw_item_base_food_right_name_tv)
            foodDetailTv = itemView.findViewById(R.id.tiw_item_base_food_right_detail_tv)
            buyBtn = itemView.findViewById(R.id.tiw_item_base_food_right_one_btn)
            cartBtn = itemView.findViewById(R.id.tiw_item_base_food_right_two_btn)
        }
    }

    /**
     * 底部视图
     */
    inner class TiwBottomViewHolder(itemView: View) : AbsBaseRvViewHolder(itemView) {

        var bottomProgressbar: ProgressBar? = null
        var bottomTextContentRl: RelativeLayout? = null
        var bottomTextContextTv: TextView? = null

        init {
            bottomProgressbar = itemView.findViewById(R.id.tiw_item_food_bottom_progress_bar)
            bottomTextContentRl = itemView.findViewById(R.id.tiw_item_food_bottom_text_rl)
            bottomTextContextTv = itemView.findViewById(R.id.tiw_item_food_bottom_text_content_tv)
        }
    }

}
