package com.inz.choosefile.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inz.choosefile.R;
import com.inz.choosefile.bean.FileBean;
import com.inz.choosefile.bean.FileFolderBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/29 11:40.
 */
public class BaseFolderViewAdapter extends RecyclerView.Adapter<BaseFolderViewAdapter.BaseFolderViewHolder> {
    private static final String TAG = "BaseFolderViewAdapter";
    private Context mContext;

    private List<FileFolderBean> folderBeanList;

    public BaseFolderViewAdapter(Context mContext) {
        this.mContext = mContext;
        folderBeanList = new ArrayList<>();
    }

    @NonNull
    @Override
    public BaseFolderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_folder_base, null, false);
        return new BaseFolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseFolderViewHolder baseFolderViewHolder, int i) {
        FileFolderBean folderBean = folderBeanList.get(i);
        FileBean fileBean = folderBean.getFileBeanList().get(0);
        String path = fileBean.getmPath();
        Glide.with(mContext).load(path).into(baseFolderViewHolder.folderIv);
        baseFolderViewHolder.folderNameTv.setText(folderBean.getFolderName());
    }

    @Override
    public int getItemCount() {
        return folderBeanList != null ? folderBeanList.size() : 0;
    }

    class BaseFolderViewHolder extends RecyclerView.ViewHolder {

        ImageView folderIv;
        TextView folderNameTv;

        BaseFolderViewHolder(@NonNull View itemView) {
            super(itemView);
            folderIv = itemView.findViewById(R.id.choose_file_item_folder_iv);
            folderNameTv = itemView.findViewById(R.id.choose_file_item_folder_name_tv);
        }
    }


    /**
     * 添加文件目录
     *
     * @param fileFolderBeanList 文件目录列表
     */
    public void addFileFolderList(List<FileFolderBean> fileFolderBeanList) {
        this.folderBeanList.addAll(fileFolderBeanList);
        notifyDataSetChanged();
    }
}
