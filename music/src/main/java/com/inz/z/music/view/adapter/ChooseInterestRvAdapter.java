package com.inz.z.music.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.inz.z.base.base.AbsBaseRvAdapter;
import com.inz.z.base.base.AbsBaseRvViewHolder;
import com.inz.z.music.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/04 17:53.
 */
public class ChooseInterestRvAdapter extends AbsBaseRvAdapter<ItemChooseInterestBean, ChooseInterestRvAdapter.BaseChooseInterestRvHolder> {

    private static final String TAG = "ChooseInterestRvAdapter";
    private RequestOptions options;

    enum ViewType {
        CONTENT,
        BOTTOM;
    }

    public ChooseInterestRvAdapter(Context mContext) {
        super(mContext);
        options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_error_red_24dp)
                .placeholder(R.drawable.image_holder)
                .timeout(10 * 1000)
                .override(120, 120)
                .priority(Priority.NORMAL)
                .encodeQuality(60);
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return list.size() == position ? ViewType.BOTTOM.ordinal() : ViewType.CONTENT.ordinal();
    }

    @SuppressLint("InflateParams")
    @Override
    public BaseChooseInterestRvHolder onCreateVH(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ViewType.CONTENT.ordinal()) {
            View view = mLayoutInflater.inflate(R.layout.item_choose_interest, null, false);
            return new ChooseInterestRvViewHolder(view);
        } else {
            View view = mLayoutInflater.inflate(R.layout.item_choose_interest_bottom, null, false);
            return new ChooseInterestBottomRvViewHolder(view);
        }
    }

    @Override
    public void onBindVH(@NonNull BaseChooseInterestRvHolder holder, int position) {
        if (holder instanceof ChooseInterestRvViewHolder) {
            ChooseInterestRvViewHolder contentHolder = (ChooseInterestRvViewHolder) holder;
            ItemChooseInterestBean bean = list.get(position);
            switchChooseItemStatus(contentHolder, bean.isChose());
            String src = bean.getSrc();
            if ("".equals(src)) {
                Glide.with(mContext).load(R.drawable.paper).apply(options).into(contentHolder.civ);
            } else {
                Glide.with(mContext).load(src).apply(options).into(contentHolder.civ);
            }
            contentHolder.nameTv.setText(bean.getName());
            contentHolder.detailTv.setText(bean.getDetail());
        } else if (holder instanceof ChooseInterestBottomRvViewHolder) {
            ChooseInterestBottomRvViewHolder bottomHolder = (ChooseInterestBottomRvViewHolder) holder;

        }
    }

    class BaseChooseInterestRvHolder extends AbsBaseRvViewHolder {
        BaseChooseInterestRvHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class ChooseInterestRvViewHolder extends BaseChooseInterestRvHolder implements View.OnClickListener {
        private CircleImageView civ;
        private CircleImageView hintCiv;
        private TextView nameTv;
        private TextView detailTv;

        ChooseInterestRvViewHolder(@NonNull View itemView) {
            super(itemView);
            civ = itemView.findViewById(R.id.music_item_choose_interest_civ);
            hintCiv = itemView.findViewById(R.id.music_item_choose_interest_hint_civ);
            nameTv = itemView.findViewById(R.id.music_item_choose_interest_name_tv);
            detailTv = itemView.findViewById(R.id.music_item_choose_interest_detail_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ItemChooseInterestBean bean = list.get(position);
            if (bean != null) {
                boolean isChoose = bean.isChose();
                bean.setChose(!isChoose);
                switchChooseItemStatus(this, !isChoose);
            }
        }
    }

    class ChooseInterestBottomRvViewHolder extends BaseChooseInterestRvHolder {
        ChooseInterestBottomRvViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    /**
     * 切换选中状态
     *
     * @param holder   ItemView
     * @param isChoose 是否选中
     */
    private void switchChooseItemStatus(ChooseInterestRvViewHolder holder, boolean isChoose) {
        if (isChoose) {
            holder.hintCiv.setVisibility(View.VISIBLE);
            holder.nameTv.setTextColor(ContextCompat.getColor(mContext, R.color.musicRed));
            holder.detailTv.setTextColor(ContextCompat.getColor(mContext, R.color.musicLitterRed));
        } else {
            holder.hintCiv.setVisibility(View.GONE);
            holder.nameTv.setTextColor(ContextCompat.getColor(mContext, R.color.musicWhite));
            holder.detailTv.setTextColor(ContextCompat.getColor(mContext, R.color.musicGray));
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Glide.with(mContext).pauseAllRequests();
    }

}
