package com.inz.choosefile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inz.choosefile.R;
import com.inz.choosefile.bean.FileBean;
import com.inz.choosefile.bean.ImageBean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/26 15:17.
 */
public class BasePhotoViewAdapter extends RecyclerView.Adapter<BasePhotoViewAdapter.BasePhotoViewHolder> {

    private static final String TAG = "BasePhotoViewAdapter";
    private Context mContext;
    private List<FileBean> fileBeanList;
    private List<FileBean> chooseFileList;

    public BasePhotoViewAdapter(Context mContext) {
        this.mContext = mContext;
        fileBeanList = new ArrayList<>();
        chooseFileList = new ArrayList<>();
    }

    @NonNull
    @Override
    public BasePhotoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo_base, viewGroup, false);
        return new BasePhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasePhotoViewHolder basePhotoViewHolder, int i) {
        FileBean bean = fileBeanList.get(i);
        if (bean.getFileType().equals(FileBean.FileType.TYPE_IMAGE)) {
            Glide.with(mContext).load(bean.getmPath()).into(basePhotoViewHolder.baseIv);
            basePhotoViewHolder.baseCb.setChecked(bean.isChecked());
            basePhotoViewHolder.shadeV.setVisibility(bean.isChecked() ? View.VISIBLE : View.GONE);
            basePhotoViewHolder.videoTv.setVisibility(View.GONE);
        } else if (bean.getFileType().equals(FileBean.FileType.TYPE_VIDEO)) {
            // TODO: 2019/4/28 视频文件
            Log.i(TAG, "onBindViewHolder: ");
            basePhotoViewHolder.videoTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return fileBeanList != null ? fileBeanList.size() : 0;
    }

    class BasePhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView baseIv;
        CheckBox baseCb;
        View shadeV;
        TextView videoTv;

        BasePhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            baseIv = itemView.findViewById(R.id.choose_file_item_photo_base_iv);
            baseCb = itemView.findViewById(R.id.choose_file_item_photo_base_check_box);
            baseCb.setOnClickListener(this);
            shadeV = itemView.findViewById(R.id.choose_file_item_photo_base_shade_view);
            videoTv = itemView.findViewById(R.id.choose_file_item_photo_base_video_time_tv);
        }

        @Override
        public void onClick(View v) {
            int position = this.getLayoutPosition();
            if (position <= fileBeanList.size()) {
                FileBean bean = fileBeanList.get(position);
                boolean isChecked = baseCb.isChecked();
                if (isChecked) {
                    chooseFileList.add(bean);
                } else {
                    chooseFileList.remove(bean);
                }
                bean.setChecked(isChecked);
                shadeV.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        }
    }


    public void addFileList(List<FileBean> fileBeanList) {
        this.fileBeanList.addAll(fileBeanList);
        notifyDataSetChanged();
    }
}
