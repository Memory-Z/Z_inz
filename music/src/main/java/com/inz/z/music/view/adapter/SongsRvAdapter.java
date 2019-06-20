package com.inz.z.music.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.inz.z.base.base.AbsBaseRvAdapter;
import com.inz.z.base.base.AbsBaseRvViewHolder;
import com.inz.z.music.R;

import java.util.Collections;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/18 16:48.
 */
public class SongsRvAdapter extends AbsBaseRvAdapter<ItemSongsBean, SongsRvAdapter.SongsViewHolder> implements ItemSongsTouchHelperAdapter {

    private ShowMode showMode;

    /**
     * 显示模式。（普通，切换）
     */
    public enum ShowMode {
        NORMAL,
        CHANGE;
    }

    public SongsRvAdapter(Context mContext) {
        super(mContext);
        showMode = ShowMode.NORMAL;
    }

    public SongsRvAdapter(Context mContext, ShowMode showMode) {
        super(mContext);
        this.showMode = showMode;
    }

    @SuppressLint("InflateParams")
    @Override
    public SongsViewHolder onCreateVH(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_songs_layout, null, false);
        return new SongsViewHolder(view);
    }

    @Override
    public void onBindVH(@NonNull SongsViewHolder holder, int position) {
        ItemSongsBean bean = list.get(position);
//        switch (showMode) {
//            case CHANGE:
//                holder.moveIBtn.setVisibility(View.VISIBLE);
//                holder.endLl.setVisibility(View.GONE);
//                holder.checkRBtn.setVisibility(View.VISIBLE);
//                break;
//            case NORMAL:
//            default:
//                holder.moveIBtn.setVisibility(View.GONE);
//                holder.endLl.setVisibility(View.VISIBLE);
//                holder.checkRBtn.setVisibility(View.GONE);
//                break;
//        }
        holder.titleTv.setText(bean.getTitle());
    }

    @Override
    public void onItemMove(int formPosition, int toPosition) {
        // 先移除 fromPosition 再添加到 toPosition
        Collections.swap(list, formPosition, toPosition);
        notifyItemMoved(formPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    class SongsViewHolder extends AbsBaseRvViewHolder {

        private TextView titleTv, detailTv;
        private RadioButton checkRBtn;
        private LinearLayout iconLl, endLl;
        private ImageButton playIBtn, moreIBtn, moveIBtn;


        public SongsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.music_item_songs_title_tv);
            detailTv = itemView.findViewById(R.id.music_item_songs_detail_tv);
            checkRBtn = itemView.findViewById(R.id.music_item_songs_check_rbtn);
            iconLl = itemView.findViewById(R.id.music_item_songs_detail_icon_ll);
            endLl = itemView.findViewById(R.id.music_item_songs_end_ll);
            playIBtn = itemView.findViewById(R.id.music_item_songs_end_play_ibtn);
            moreIBtn = itemView.findViewById(R.id.music_item_songs_end_more_ibtn);
            moveIBtn = itemView.findViewById(R.id.music_item_songs_move_ibtn);
        }
    }
}
