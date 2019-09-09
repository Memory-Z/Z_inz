package com.inz.z.view;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.inz.z.other_module.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/10 13:06.
 */
public class ReadImageRecyclerAdapter extends RecyclerView.Adapter<ReadImageRecyclerAdapter.ReadImageRecyclerViewHolder> {

    private static final String TAG = "ReadImageRecyclerAdapte";

    private Context mContext;
    private List<String> imageList;
    private RequestOptions options;

    public ReadImageRecyclerAdapter(Context mContext) {
        this(mContext, new ArrayList<String>());
    }

    public ReadImageRecyclerAdapter(Context mContext, List<String> imageList) {
        this.mContext = mContext;
        this.imageList = imageList;
        initGlide();
    }

    private void initGlide() {
        options = new RequestOptions()
                .placeholder(R.drawable.pager_2)
                .timeout(1000 * 30)
                .centerInside();
    }

    @NonNull
    @Override
    public ReadImageRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.item_read_page, null, false);
        return new ReadImageRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadImageRecyclerViewHolder readImageRecyclerViewHolder, int i) {
        String src = imageList.get(i);
        Glide.with(mContext).load(src).apply(options).into(readImageRecyclerViewHolder.previewIv);
    }

    @Override
    public int getItemCount() {
        return imageList == null ? 0 : imageList.size();
    }

    public void refreshImageList(List<String> strings) {
        this.imageList.clear();
        this.imageList.addAll(strings);
        notifyDataSetChanged();
    }

    public void addImageList(List<String> strings) {
        this.imageList.addAll(strings);
        notifyDataSetChanged();
    }

    public void replaceImage(int position, String src) {
        if (this.imageList.size() > position && position >= 0) {
            this.imageList.set(position, src);
            notifyDataSetChanged();
        }
    }

    class ReadImageRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // 预览图
        private ImageView previewIv;

        ReadImageRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            previewIv = itemView.findViewById(R.id.item_image_read_iv);
            previewIv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (imageListener != null) {
                imageListener.onPageClick(v, position);
            }
        }
    }

    private ReadImageListener imageListener;

    public void setImageListener(ReadImageListener imageListener) {
        this.imageListener = imageListener;
    }

    public interface ReadImageListener {
        void onPageClick(View view, int position);
    }
}
