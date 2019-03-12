package com.inz.z.view.fragment.example;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inz.z.R;
import com.inz.z.view.fragment.AbsBaseFragment;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/1/3 18:37.
 */
public class ItemCategoryFragment extends AbsBaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.ex_item_shop_category;
    }

    @Override
    public void initView() {
        ImageView bg01 = mView.findViewById(R.id.ex_item_shop_category_model_bg01_iv);
        Glide.with(mContext).load(R.drawable.beautiful_blond_blonde_1071162).into(bg01);
        ImageView itemIv0 = mView.findViewById(R.id.ex_item_shop_category_model_0_civ);
        Glide.with(mContext).load(R.drawable.beautiful_blond_blonde_1071162).into(itemIv0);
        ImageView itemIv1 = mView.findViewById(R.id.ex_item_shop_category_model_1_civ);
        Glide.with(mContext).load(R.drawable.beautiful_blond_blonde_1071162).into(itemIv1);
        ImageView itemIv2 = mView.findViewById(R.id.ex_item_shop_category_model_2_civ);
        Glide.with(mContext).load(R.drawable.beautiful_blond_blonde_1071162).into(itemIv2);
    }

    @Override
    public void initData() {

    }
}
