package com.inz.z.music.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.inz.z.base.base.AbsBaseRvAdapter;
import com.inz.z.base.base.AbsBaseRvViewHolder;
import com.inz.z.music.R;
import com.inz.z.music.view.widget.PlayerCircleImageView;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/12 10:57.
 */
public class TopPlayerAdapter extends AbsBaseRvAdapter<ItemTopPlayerBean, TopPlayerAdapter.TopPlayerViewHolder> {

    private RequestOptions options;

    public TopPlayerAdapter(Context mContext) {
        super(mContext);
        options = new RequestOptions()
                .timeout(3000)
                .placeholder(R.drawable.image_holder)
                .error(R.drawable.ic_error_red_24dp)
                .format(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public TopPlayerViewHolder onCreateVH(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = mLayoutInflater.inflate(R.layout.item_top_player_photo, null, false);
        return new TopPlayerViewHolder(view);
    }

    @Override
    public void onBindVH(@NonNull TopPlayerViewHolder holder, int position) {
        ItemTopPlayerBean bean = list.get(position);
        Glide.with(mContext).load(bean.getPhotoUrlStr()).apply(options).into(holder.playerCircleImageView);
        switch (bean.getMinPhotoStyle()) {
            case NULL:
                holder.playerCircleImageView.setMinImageDrawable(null);
                break;
            case MUSIC:
                holder.playerCircleImageView.setMinImageResource(R.drawable.image_holder);
                break;
            case RED_DOT:
                holder.playerCircleImageView.setMinImageResource(R.drawable.ic_error_red_24dp);
                break;
        }
        holder.playerName.setText(bean.getPhotoName());
    }

    class TopPlayerViewHolder extends AbsBaseRvViewHolder {
        PlayerCircleImageView playerCircleImageView;
        TextView playerName;

        TopPlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            playerCircleImageView = itemView.findViewById(R.id.music_item_top_player_photo_pciv);
            playerName = itemView.findViewById(R.id.music_item_top_play_user_photo_detail_tv);
        }
    }
}
