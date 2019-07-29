package com.inz.z.music.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.inz.z.base.base.AbsBaseRvAdapter;
import com.inz.z.base.base.AbsBaseRvViewHolder;
import com.inz.z.music.R;
import com.inz.z.music.database.ItemAlbumBean;

import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/14 15:32.
 */
public class AlbumRvAdapter extends AbsBaseRvAdapter<ItemAlbumBean, AlbumRvAdapter.AlbumRvViewHolder> {
    private static final int DEFAULT_BASE_MAX_NUM = 10;
    private int baseMax = DEFAULT_BASE_MAX_NUM;

    private enum AlbumViewType {
        BASE,
        MORE;
    }

    private RequestOptions options;

    public AlbumRvAdapter(Context mContext) {
        super(mContext);
        initGlideOption();
    }

    public AlbumRvAdapter(Context mContext, int baseMax) {
        super(mContext);
        this.baseMax = baseMax;
        initGlideOption();
    }

    private void initGlideOption() {
        options = new RequestOptions()
                .error(R.drawable.ic_error_red_24dp)
                .placeholder(R.drawable.image_holder)
                .centerCrop()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .timeout(60 * 1000);
    }

    @Override
    public int getItemViewType(int position) {
        return (position < baseMax) ? AlbumViewType.BASE.ordinal() : AlbumViewType.MORE.ordinal();
    }

    @SuppressLint("InflateParams")
    @Override
    public AlbumRvViewHolder onCreateVH(@NonNull ViewGroup parent, int viewType) {
        if (viewType == AlbumViewType.BASE.ordinal()) {
            View view = mLayoutInflater.inflate(R.layout.item_album_layout, null, false);
            return new BaseAlbumRvViewHolder(view);
        } else {
            View view = mLayoutInflater.inflate(R.layout.item_album_more, null, false);
            return new MoreAlbumRvViewHolder(view);
        }
    }

    @Override
    public void onBindVH(@NonNull AlbumRvViewHolder holder, int position) {
        if (holder instanceof BaseAlbumRvViewHolder) {
            BaseAlbumRvViewHolder baseHolder = (BaseAlbumRvViewHolder) holder;
            ItemAlbumBean bean = list.get(position);
            Glide.with(mContext).load(bean.getAlbumSrc()).apply(options).into(baseHolder.albumIv);
            baseHolder.titleTv.setText(bean.getTitleName());
            baseHolder.playerTv.setText(bean.getPlayerName());
        }
    }

    @Override
    public void refreshData(List<ItemAlbumBean> list) {
        int dif = list.size() - baseMax;
        if (dif > 1) {
            for (int i = 1; i < dif; i++) {
                list.remove(baseMax);
            }
        }
        super.refreshData(list);
    }

    @Override
    public void loadMoreData(List<ItemAlbumBean> list) {
        int dif = this.list.size() + list.size() - baseMax;
        if (dif > 1) {
            int d = dif - this.list.size();
            for (int i = 1; i < d; i++) {
                list.remove(baseMax - this.list.size());
            }
        }
        super.loadMoreData(list);
    }

    class AlbumRvViewHolder extends AbsBaseRvViewHolder {
        AlbumRvViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class BaseAlbumRvViewHolder extends AlbumRvViewHolder {
        ImageView albumIv;
        TextView titleTv, playerTv;

        BaseAlbumRvViewHolder(@NonNull View itemView) {
            super(itemView);
            albumIv = itemView.findViewById(R.id.music_item_album_iv);
            titleTv = itemView.findViewById(R.id.music_item_album_title_tv);
            playerTv = itemView.findViewById(R.id.music_item_album_player_tv);
        }
    }

    class MoreAlbumRvViewHolder extends AlbumRvViewHolder {
        MoreAlbumRvViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
