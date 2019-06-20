package com.inz.z.music.view.adapter;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/18 16:49.
 */
public interface ItemSongsTouchHelperAdapter {
    /**
     * 数据交换
     *
     * @param formPosition 来自
     * @param toPosition   前往
     */
    void onItemMove(int formPosition, int toPosition);

    /**
     * 移除项
     *
     * @param position 移除位置
     */
    void onItemDismiss(int position);
}
