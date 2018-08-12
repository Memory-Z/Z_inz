package com.inz.z_inz.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * @date Create By 2018/8/12 11:02
 */
public abstract class AbsBaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    /**
     * 上下文
     */
    public Context mContext;

    /**
     * 数据列表
     */
    public List<T> list;

    /**
     * 布局填充
     */
    public LayoutInflater mLayoutInflater;

    public AbsBaseRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
        mLayoutInflater = (LayoutInflater) mContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return onCreateVH(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        onBindVH(holder, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 刷新数据
     *
     * @param list 数据列表
     */
    public void refreshData(List<T> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 加载更多数据
     *
     * @param list 数据列表
     */
    public void loadMoreData(List<T> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 创建 ViewHolder
     *
     * @param parnet
     * @param viewType
     * @return
     */
    abstract VH onCreateVH(@NonNull ViewGroup parnet, int viewType);

    /**
     * 绑定 ViewHolder
     *
     * @param holder
     * @param position
     */
    abstract void onBindVH(@NonNull VH holder, int position);
}
