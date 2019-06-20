package com.inz.z.music.view.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.inz.z.base.view.AbsBaseFragment;
import com.inz.z.base.view.widget.RoundImageView;
import com.inz.z.music.R;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/19 17:50.
 */
public class BottomPlayFragment extends AbsBaseFragment {

    private RoundImageView startRiv;
    private TextView titleTv, detailTv;

    private String imageSrc, titleStr, detailStr;

    @Override
    protected void initWindow() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_main_bottom_play;
    }

    @Override
    protected void initView() {
        startRiv = mView.findViewById(R.id.music_item_bottom_play_photo_riv);
        titleTv = mView.findViewById(R.id.music_item_bottom_center_title_tv);
        detailTv = mView.findViewById(R.id.music_item_bottom_center_detail_tv);
    }

    @Override
    protected void initData() {
        RequestOptions options = new RequestOptions()
                .timeout(20 * 1000)
                .placeholder(R.drawable.image_holder)
                .error(R.drawable.image_holder)
                .centerCrop();
        Bundle bundle = getArguments();
        if (bundle != null) {
            imageSrc = bundle.getString("imageSrc", "");
            titleStr = bundle.getString("title", "");
            detailStr = bundle.getString("detail", "");
        }
        Glide.with(mContext).load(imageSrc).apply(options).into(startRiv);
        titleTv.setText(titleStr);
        detailTv.setText(detailStr);
    }

}
